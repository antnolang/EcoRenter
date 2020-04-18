package com.ispp.EcoRenter.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.ispp.EcoRenter.form.RenterForm;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.service.RenterService;

@SpringBootTest
public class RenterServiceTest {

	// Service under testing --------------------
	@Autowired
	private RenterService renterService;


	/*
	 * Un renter edita su perfil
	 */
	@WithMockUser("renter2")
	@Transactional
	@Test
	public void positiveTest_edit_one() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch, iban;
		RenterForm renterForm;
		Renter renter;
		
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "renter2Edited";
		password = username;
		passwordMatch = password;
		iban = "ES1501287541639316325975";
	
		renterForm = this.getRenterForm(name, surname, email, telephoneNumber, username, password, passwordMatch, iban);
		
		renter = this.renterService.edit(renterForm);
		
		assertNotNull(renter);
		assertTrue(renter.getId() > 0);
	}
	

	/*
	 * Un renter edita su perfil
	 * Las contraseñas no coinciden
	 */
	@WithMockUser("renter2")
	@Transactional
	@Test
	public void negativeTest_edit_one() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch, iban;
		RenterForm renterForm;
		Renter renter;
		
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "renter2";
		password = username;
		passwordMatch = "Obviamente no coinciden";
		iban = "ES1501287541639316325975";
			
		renterForm = this.getRenterForm(name, surname, email, telephoneNumber, username, password, passwordMatch, iban);
		
		try {
			renter = this.renterService.edit(renterForm);
		} catch (Throwable oops) {
			renter = null;
		}
		
		assertTrue(renter == null);
	}
	
	
	/*
	 * Un renter edita su perfil
	 * El usuario decide actualizar su username, pero dicho username ya esta
	 * siendo usado por otro usuario
	 */
	@WithMockUser("renter2")
	@Transactional
	@Test
	public void negativeTest_edit_dos() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch, iban;
		RenterForm renterForm;
		Renter renter;
		
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "renter1";
		password = "renter2";
		passwordMatch = "renter2";
		iban = "ES1501287541639316325975";
			
		renterForm = this.getRenterForm(name, surname, email, telephoneNumber, username, password, passwordMatch, iban);
		
		try {
			renter = this.renterService.edit(renterForm);
		} catch (Throwable oops) {
			renter = null;
		}
		
		assertTrue(renter == null);
	}
	
	/*
	 * Un renter edita su perfil
	 * Datos no validos en atributos
	 */
	@WithMockUser("renter2")
	@Transactional
	@Test
	public void negativeTest_edit_tres() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch, iban;
		RenterForm renterForm;
		Renter renter;
		
		name = "";
		surname = "55";
		email = null;
		telephoneNumber = "";
		username = "renter1";
		password = "renter2";
		passwordMatch = "renter2";
		iban = "ES1501287541639316325975";
			
		renterForm = this.getRenterForm(name, surname, email, telephoneNumber, username, password, passwordMatch, iban);
		
		try {
			renter = this.renterService.edit(renterForm);
		} catch (Throwable oops) {
			renter = null;
		}
		
		assertTrue(renter == null);
	}
	
	private RenterForm getRenterForm(String name, String surname, String email, String telephoneNumber, String username,
			String password, String passwordMatch, String iban) {
		RenterForm result;

		result = new RenterForm(name, surname, email, telephoneNumber, username);
		result.setPassword(password);
		result.setPasswordMatch(passwordMatch);
		result.setIban(iban);

		return result;
	}

}
