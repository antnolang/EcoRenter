package com.ispp.EcoRenter.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.CreditCard;
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
	
	public CreditCard save(CreditCard creditCard) {
		this.checkByPrincipal(creditCard);
		
		CreditCard result;
		
		result = this.creditCardRepository.saveAndFlush(creditCard);
		
		return result;
	}
	
	public void delete(CreditCard creditCard) {
		this.checkByPrincipal(creditCard);
		
		int count = this.rentOutService.findRentOutByCreditCard(creditCard.getId());
		
		Assert.isTrue(count == 0, "No se puede borrar");
		
		this.creditCardRepository.delete(creditCard);
	}
	
	private void checkByPrincipal(CreditCard creditCard) {
		Assert.notNull(creditCard, "Tarjeta desconocida");
		
		Renter renter, principal;
		
		renter = this.renterService.findRenterByCreditCard(creditCard.getId());
		principal = this.renterService.findByPrincipal();
		
		Assert.isTrue(principal.getId() == renter.getId(), "Acceso inválido");
	}
	
}
