package com.ispp.EcoRenter.service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.ispp.EcoRenter.form.CreditCardForm;
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

        RentOut result;
        Renter principal = this.renterService.findByPrincipal();

        Assert.notNull(rent, "No debe ser nulo.");
        Assert.isTrue(rent.getRenter().equals(principal), "No la está alquilando el usuario logeado.");

        result = this.rentOutRepository.save(rent);

        this.stripePayment(rent.getSmallholding().getPrice(), rent.getSmallholding().getMaxDuration());
        result.getSmallholding().setAvailable(false);

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

    public RentOut reconstruct(Smallholding smallholding, CreditCardForm creditCardForm, BindingResult binding){
        RentOut result;
        CreditCard creditCard;

        result = this.create(smallholding);
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

    public Integer findRentOutByCreditCard(int creditCardId) {
        Integer result;

        result = this.rentOutRepository.findRentOutByCreditCard(creditCardId);

        return result;
    }
    
    public Collection<RentOut> findByOwnerAndSmallholding(int ownerId, int smallholdingId){
    	
    	return this.rentOutRepository.findRentOutByOwnerAndSmallholding(ownerId, smallholdingId);
    	
    	
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
    
}