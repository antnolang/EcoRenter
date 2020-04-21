package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.ispp.EcoRenter.form.CreditCardForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.CreditCard;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.repository.RentOutRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

@Service
@Transactional
public class RentOutService {

    // Repository

    @Autowired
    private RentOutRepository rentOutRepository;

    @Autowired
    private RenterService renterService;

    @Autowired
    private Validator validator;

    @Autowired
    private CreditCardService creditCardService;

    @Autowired
    private MailService mailService;

    // Constructor

    public RentOutService() {
        super();
    }

    // CRUD methods

    public RentOut create(Smallholding smallholding) {
        Assert.isTrue(smallholding.getId() != 0, "La parcela debe existir");
        Assert.notNull(smallholding, "La parcela debe existir");
        Assert.isTrue(smallholding.isAvailable(), "La parcela debe estar disponible");
        Assert.isTrue(smallholding.getStatus().equals("NO ALQUILADA"), "La parcela no debe estar alquilada");

        Renter principal = this.renterService.findByPrincipal();
        Date startDate = new Date(System.currentTimeMillis() - 1);

        RentOut result = new RentOut();

        result.setRenter(principal);
        result.setStartDate(startDate);
        result.setMonth(smallholding.getMaxDuration());
        result.setIsActive(true);
        result.setSmallholding(smallholding);

        return result;

    }

    public RentOut save(RentOut rent) throws StripeException {
        Assert.notNull(rent, "El alquiler debe existir");
        Assert.isTrue(rent.getIsActive() == true, "Esta parcela ya está alquilada");
        Assert.isTrue(rent.getSmallholding().getStatus().equals("NO ALQUILADA"), "Esta parcela ya está alquilada");
        
        
        RentOut result;
        Renter principal = this.renterService.findByPrincipal();
        List<Actor> recipients;

        Assert.notNull(rent, "No debe ser nulo.");
        Assert.isTrue(rent.getRenter().equals(principal), "No la está alquilando el usuario logeado.");

        result = this.rentOutRepository.save(rent);

        this.stripePayment(rent.getSmallholding().getPrice(), rent.getSmallholding().getMaxDuration());
        result.getSmallholding().setAvailable(false);
        result.getSmallholding().setStatus("ALQUILADA");

        recipients = new ArrayList<Actor>();
        recipients.add(result.getRenter());
        recipients.add(result.getSmallholding().getOwner());

        this.mailService.sendEmail(recipients, "Alquiler de parcela " + result.getSmallholding().getTitle(), 
            "Se ha realizado correctamente el alquiler de la parcela " + result.getSmallholding().getTitle() + 
            " con dirección " + result.getSmallholding().getAddress() + " y cuyo precio es " + result.getSmallholding().getPrice() + 
            "€ por el periodo de " + result.getSmallholding().getMaxDuration() + " meses");

        return result;
    }

    public void delete(RentOut rentout) {
        this.rentOutRepository.delete(rentout);
    }

    public RentOut findOne(int rentOutId){
        RentOut result;

        result = this.rentOutRepository.findById(rentOutId).get();
        Assert.notNull(result, "El alquiler debe existir");

        return result;
    }

    public RentOut findOneToDisplay(int rentOutId){
        RentOut result;

        result = this.findOne(rentOutId);
        Assert.isTrue(this.renterService.findByPrincipal().equals(result.getRenter()), "No se puede visualizar alquileres de otros arrendatarios");

        return result;
    }

    public RentOut reconstruct(Smallholding smallholding, CreditCardForm creditCardForm, BindingResult binding){
        RentOut result;
        CreditCard creditCard;

        result = this.create(smallholding);
        if(creditCardForm.getId() != 0){
            this.creditCardService.checkExpiredCreditCardForm(creditCardForm);
            creditCard = this.creditCardService.findOne(creditCardForm.getId());
        } else
            creditCard = this.creditCardService.save(creditCardForm);
        result.setCreditCard(creditCard);

        this.validator.validate(result, binding);

        return result;
    }

    // Other business method


    public Collection<RentOut> findRentOutsBySmallholdingAndRenter(int smallholdingId, int renterId) {
        Collection<RentOut> result;

        result = this.rentOutRepository.findRentOutBySmallholdingAndRenter(smallholdingId, renterId);
        Assert.notNull(result, "No instancia la colección vacía de alquileres");

        return result;
    }

    public Collection<RentOut> findRentOutsByRenter(int renterId) {
        Collection<RentOut> result;

        result = this.rentOutRepository.findRentOutByRenter(renterId);
        Assert.notNull(result, "No instancia la colección vacía de alquileres");

        return result;
    }

    public Collection<RentOut> findRentOutByOwner(int ownerId){
        Collection<RentOut> result;

        result = this.rentOutRepository.findRentOutByOwner(ownerId);

        return result;
    }

    public Integer findRentOutByCreditCard(int creditCardId) {
        Integer result;

        result = this.rentOutRepository.findRentOutByCreditCard(creditCardId);

        return result;
    }
    

    public RentOut findByOwnerAndSmallholding(int ownerId, int smallholdingId){
    	
    	return this.rentOutRepository.findRentOutByOwnerAndSmallholding(ownerId, smallholdingId);
    }

    public Collection<RentOut> findBySmallholding(int smallholdingId){
    	
    	return this.rentOutRepository.findBySmallholding(smallholdingId);

    	
    	
    }

    public void stripePayment(Double price, int monthsNumber) throws StripeException {
        Stripe.apiKey = "sk_test_DxeIjPSmKslD2tFg1b1CG2TU00Q4RigZkT";

        Long amount = (new Double(price)).longValue();

        PaymentIntentCreateParams paymentParams = PaymentIntentCreateParams.builder()
            .setCurrency("eur")
            .setAmount(amount*monthsNumber*100)
            .setReceiptEmail(this.renterService.findByPrincipal().getEmail())
            .putMetadata("integration_check", "accept_a_payment")
            .build();

        PaymentIntent intent = PaymentIntent.create(paymentParams);

        PaymentIntent paymentIntent = PaymentIntent.retrieve(intent.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", "pm_card_visa");

        paymentIntent.confirm(params);
    }

    public Page<RentOut> findPaginated(Pageable pageable, Collection<RentOut> rentOuts) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<RentOut> list;
        ArrayList<RentOut> roList= new ArrayList<>(rentOuts);
 
        if (roList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, roList.size());
            list = roList.subList(startItem, toIndex);
        }
 
        Page<RentOut> roPage
          = new PageImpl<RentOut>(list, PageRequest.of(currentPage, pageSize), roList.size());
 
        return roPage;
    }
    
}