package com.ispp.EcoRenter.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.util.HtmlUtils;

import com.ispp.EcoRenter.form.CreditCardForm;
import com.ispp.EcoRenter.model.CreditCard;
import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.repository.CreditCardRepository;

@Service
@Transactional
public class CreditCardService {

	// Repositorio manejada -----------------------------
	@Autowired
	private CreditCardRepository creditCardRepository;
	
	
	// Servicios de apoyo ------------------------------
	@Autowired
	private RenterService renterService;
	
	@Autowired
	private RentOutService rentOutService;
	
	@Autowired
	private CustomisationService customisationService;
	
	// Constructores -----------------------------------
	public CreditCardService() {
		super();
	}
	
	
	// Metodos CRUD -----------------------------------
	public CreditCard findOne(int creditCardId) {
		Assert.isTrue(creditCardId > 0, "Identificador inválido");
		
		Optional<CreditCard> creditCard;
		CreditCard result;
		
		creditCard = this.creditCardRepository.findById(creditCardId);
		
		result = creditCard.get();
		
		return result;
	}
	
	public CreditCard findOneToEdit(int creditCardId) {
		CreditCard result;
		
		result = this.findOne(creditCardId);
		
		this.checkByPrincipal(result);
		
		return result;
	}
	
	public CreditCard create() {
		CreditCard result;
		
		result = new CreditCard();
		
		return result;
	}
	
	public CreditCard save(CreditCard creditCard, Renter renter) {
		this.checkByPrincipal(creditCard, renter);
		this.checkExpiredCreditCard(creditCard);
		Assert.isTrue(this.getCreditCardMakes().contains(creditCard.getBrandName()),
					  "Marca desconocida");
		
		CreditCard result;
			
		result = this.creditCardRepository.saveAndFlush(creditCard);
		
		// Actualizamos Renter::creditCards
		this.renterService.addCreditCard(renter, result);
		
		return result;
	}
	
	
	public CreditCard save(CreditCardForm creditCardForm) {
		CreditCard creditCard;
		CreditCard result;
		Renter renter;
		int creditCardId;
		String holderName;
		
		creditCardId = creditCardForm.getId();
		
		Assert.isTrue(creditCardId == 0, "No se puede editar una tarjeta de credito");
		
		creditCard = this.create();
		
		holderName = HtmlUtils.htmlEscape(creditCardForm.getHolderName().trim());
		
		creditCard.setHolderName(holderName);
		creditCard.setBrandName(creditCardForm.getBrandName().trim());
		creditCard.setNumber(creditCardForm.getNumber().trim());
		creditCard.setExpirationMonth(creditCardForm.getExpirationMonth().trim());
		creditCard.setExpirationYear(creditCardForm.getExpirationYear().trim());
		creditCard.setCvvCode(creditCardForm.getCvvCode());
		
		renter = creditCardForm.getRenter();
		
		result = this.save(creditCard, renter);
		
		return result;
	}
	
	public void delete(CreditCard creditCard) {
		this.checkByPrincipal(creditCard);
		
		Renter renter;
		int creditCardId;
		
		creditCardId = creditCard.getId();
		
		int count = this.rentOutService.findRentOutByCreditCard(creditCardId);
		
		Assert.isTrue(count == 0, "No se puede borrar");
		
		renter = this.renterService.findRenterByCreditCard(creditCardId);
		
		// Actualizamos Renter::creditCards
		this.renterService.removeCreditCard(renter, creditCard);
		
		this.creditCardRepository.delete(creditCard);
	}
	
	// Otros metodos -------------------------------------------
	public Map<Integer, String> getCreditCardEncodedNumber(Collection<CreditCard> creditCards) {
		Map<Integer, String> results;
		String encodedNumber;
		
		results = new HashMap<Integer, String>();
		
		for (CreditCard creditCard: creditCards) {
			encodedNumber = this.getEncodedNumber(creditCard);
			
			results.put(creditCard.getId(), encodedNumber);
		}
		
		return results;
	}
	
	private String getEncodedNumber(CreditCard creditCard) {
		String result, number;
		
		number = creditCard.getNumber();
		int n = number.length();
		
		result = "**** **** **** ";
		
		result = result + number.substring(n-4, n);
		
		return result;
	}
	
	private void checkByPrincipal(CreditCard creditCard, Renter renter) {
		Assert.notNull(creditCard, "Tarjeta desconocida");
		
		Renter principal;
		
		principal = this.renterService.findByPrincipal();
		
		Assert.isTrue(principal.getId() == renter.getId(), "Acceso inválido");
	}
	
	private void checkByPrincipal(CreditCard creditCard) {
		Renter renter;
		
		renter = this.renterService.findRenterByCreditCard(creditCard.getId());
		
		this.checkByPrincipal(creditCard, renter);
	}
	
	private void checkExpiredCreditCard(CreditCard creditCard) {
		String year, month, str_date;
		DateTimeFormatter formatter;
		LocalDate now, expiration;
		
		formatter = DateTimeFormatter.ofPattern("yy-MM-dd");
		
		year = creditCard.getExpirationYear();
		month = creditCard.getExpirationMonth();
		
		str_date = year + "-" + month + "-" + "01";
		
		expiration = LocalDate.parse(str_date, formatter);
		//expiration.plusMonths(1).minusDays(1);
		
		now = LocalDate.now();
		
		Assert.isTrue(now.isBefore(expiration), "La tarjeta de crédito está expirada");
	}
	
	public Collection<String> getCreditCardMakes() {
		List<String> results;
		String creditCardMakes;
		String[] fields;
		Customisation custo;
		
		custo = this.customisationService.find();
		creditCardMakes = custo.getCreditCardMakes();
		
		fields = creditCardMakes.split(",");
		
		results = new ArrayList<String>();
		for (String make: fields) {
			results.add(make.trim());
		}
		
		return results;
	}
	
	public Map<Integer, Boolean> getCreditCardByRentOutNumber(Collection<CreditCard> creditCards) {
		Map<Integer, Boolean> results;
		Integer count;
		int id;
		
		results = new HashMap<Integer, Boolean>();
		for (CreditCard cc: creditCards) {
			id = cc.getId();
			count = this.rentOutService.findRentOutByCreditCard(id);
			
			results.put(id, count == 0);
		}
		
		return results;
	}
	
}
