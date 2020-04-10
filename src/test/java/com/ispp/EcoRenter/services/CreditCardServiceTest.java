package com.ispp.EcoRenter.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.ispp.EcoRenter.form.CreditCardForm;
import com.ispp.EcoRenter.model.CreditCard;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.service.CreditCardService;
import com.ispp.EcoRenter.service.RenterService;

@SpringBootTest
public class CreditCardServiceTest {

	// Service under testing --------------------
	@Autowired
	private CreditCardService creditCardService;
	
	// Supporting services ---------------------
	@Autowired
	private RenterService renterService;
	
	@Test
	@Transactional
	@WithMockUser("renter1")
	public void positiveTest_findOneToEdit() {
		CreditCard creditCard;
		int creditCardId;
		
		creditCardId = 900;
		creditCard = this.creditCardService.findOneToEdit(creditCardId);
		
		assertNotNull(creditCard);
	}
	
	@Test
	@Transactional
	@WithMockUser("renter2")
	public void negativeTest_findOneToEdit() {
		CreditCard creditCard;
		int creditCardId;
		
		creditCardId = 900;
		
		try {
			creditCard = this.creditCardService.findOneToEdit(creditCardId);
		} catch (IllegalArgumentException e) {
			creditCard = null;
		}
		
		assertTrue(creditCard == null);
	}
	
	
	@Test
	@Transactional
	@WithMockUser("renter2")
	public void positiveTest_create() {
		CreditCard creditCard;
		
		creditCard = this.creditCardService.create();
		
		assertNotNull(creditCard);
	}
	
	/*
	 * Un renter registra una tarjeta de credito
	 */
	@Test
	@Transactional
	@WithMockUser("renter2")
	public void positiveTest_save() {
		CreditCardForm creditCardForm;
		CreditCard creditCard;
		Renter principal;
		String holderName;
		String brandName;
		String number;
		String expirationMonth;
		String expirationYear;
		int cvvCode;
		int id;
		int version;
		
		id = 0;
		version = 0;
		principal = this.renterService.findByPrincipal();
		holderName = "holder name test";
		brandName = "VISA";
		number = "3569273774576284";
		expirationMonth = "05";
		expirationYear = "24";
		cvvCode = 323;
		
		creditCardForm = new CreditCardForm(id, version, holderName, brandName,
											number, expirationMonth, expirationYear,
											cvvCode, principal);
		
		creditCard = this.creditCardService.save(creditCardForm);
		
		assertNotNull(creditCard);
		assertTrue(principal.getCreditCards().contains(creditCard));
	}
	
	
	/*
	 * Un renter registra una tarjeta de credito.
	 * El principal no coincide con el propietario de la tarjeta
	 */
	@Test
	@Transactional
	@WithMockUser("renter2")
	public void negativeTest_save_uno() {
		CreditCardForm creditCardForm;
		CreditCard creditCard;
		Renter renter;
		String holderName;
		String brandName;
		String number;
		String expirationMonth;
		String expirationYear;
		int cvvCode;
		int id;
		int version;
		int renterId;
		
		renterId = 300;
		renter = this.renterService.findOne(renterId);
		
		id = 0;
		version = 0;
		holderName = "holder name test";
		brandName = "VISA";
		number = "3569273774576284";
		expirationMonth = "05";
		expirationYear = "24";
		cvvCode = 323;
		
		creditCardForm = new CreditCardForm(id, version, holderName, brandName,
											number, expirationMonth, expirationYear,
											cvvCode, renter);
		
		try {
			creditCard = this.creditCardService.save(creditCardForm);
		} catch (IllegalArgumentException e) {
			creditCard = null;
		}
		
		assertTrue(creditCard == null);
	}
	
	
	/*
	 * Un renter registra una tarjeta de credito.
	 * La tarjeta de credito esta expirada
	 */
	@Test
	@Transactional
	@WithMockUser("renter2")
	public void negativeTest_save_dos() {
		CreditCardForm creditCardForm;
		CreditCard creditCard;
		Renter principal;
		String holderName;
		String brandName;
		String number;
		String expirationMonth;
		String expirationYear;
		int cvvCode;
		int id;
		int version;
		
		principal = this.renterService.findByPrincipal();
		
		id = 0;
		version = 0;
		holderName = "holder name test";
		brandName = "VISA";
		number = "3569273774576284";
		expirationMonth = "03";
		expirationYear = "20";
		cvvCode = 323;
		
		creditCardForm = new CreditCardForm(id, version, holderName, brandName,
											number, expirationMonth, expirationYear,
											cvvCode, principal);
		
		try {
			creditCard = this.creditCardService.save(creditCardForm);
		} catch (IllegalArgumentException e) {
			creditCard = null;
		}
		
		assertTrue(creditCard == null);
	}
	
	/*
	 * Un renter registra una tarjeta de credito.
	 * Marca desconocida
	 */
	@Test
	@Transactional
	@WithMockUser("renter2")
	public void negativeTest_save_tres() {
		CreditCardForm creditCardForm;
		CreditCard creditCard;
		Renter principal;
		String holderName;
		String brandName;
		String number;
		String expirationMonth;
		String expirationYear;
		int cvvCode;
		int id;
		int version;
		
		principal = this.renterService.findByPrincipal();
		
		id = 0;
		version = 0;
		holderName = "holder name test";
		brandName = "UNKONWN";
		number = "3569273774576284";
		expirationMonth = "03";
		expirationYear = "20";
		cvvCode = 323;
		
		creditCardForm = new CreditCardForm(id, version, holderName, brandName,
											number, expirationMonth, expirationYear,
											cvvCode, principal);
		
		try {
			creditCard = this.creditCardService.save(creditCardForm);
		} catch (IllegalArgumentException e) {
			creditCard = null;
		}
		
		assertTrue(creditCard == null);
	}
	
	/*
	 * Un renter borra una tarjeta de credito
	 * Esa tarjeta de credito ha sido usada
	 */
	@Test
	@Transactional
	@WithMockUser("renter1")
	public void negativeTest_delete_uno() {
		CreditCard creditCard;
		int creditCardId;
		
		creditCardId = 900;
		creditCard = this.creditCardService.findOne(creditCardId);
		
		try {
			this.creditCardService.delete(creditCard);
		} catch (IllegalArgumentException e) {
		}
		
		creditCard = this.creditCardService.findOne(creditCardId);
		
		assertNotNull(creditCard);
	}
	
	/*
	 * Un renter borra una tarjeta de credito
	 * El renter trata de borrar una tarjeta que no le pertenece
	 */
	@Test
	@Transactional
	@WithMockUser("renter2")
	public void negativeTest_delete_dos() {
		CreditCard creditCard;
		int creditCardId;
		
		creditCardId = 900;
		creditCard = this.creditCardService.findOne(creditCardId);
		
		try {
			this.creditCardService.delete(creditCard);
		} catch (IllegalArgumentException e) {
		}
		
		creditCard = this.creditCardService.findOne(creditCardId);
		
		assertNotNull(creditCard);
	}
	
}
