package com.ispp.EcoRenter.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.ispp.EcoRenter.form.OwnerForm;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.service.OwnerService;

@SpringBootTest
public class OwnerServiceTest {

	@Autowired
	private OwnerService ownerService;
	
	
	/*
	 * Un owner edita su perfil
	 */
	@WithMockUser("owner2")
	@Transactional
	@Test
	public void positiveTest_edit_one() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch, iban;
		OwnerForm ownerForm;
		Owner owner;
		
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "admin2Edited";
		password = username;
		passwordMatch = password;
		iban = "ES1501287541639316325975";
	
		ownerForm = this.getOwnerForm(name, surname, email, telephoneNumber, username, password, passwordMatch, iban);
		
		owner = this.ownerService.edit(ownerForm);
		
		assertNotNull(owner);
		assertTrue(owner.getId() > 0);
	}
	
	/*
	 * Un owner edita su perfil
	 * La contraseñas no coinciden.
	 */
	@WithMockUser("owner2")
	@Transactional
	@Test
	public void negativeTest_edit_one() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch, iban;
		OwnerForm ownerForm;
		Owner owner;
		
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "owner2";
		password = username;
		passwordMatch = "Las contraseñas no coinciden";
		iban = "ES1501287541639316325975";
	
		ownerForm = this.getOwnerForm(name, surname, email, telephoneNumber, username, password, passwordMatch, iban);
		
		try {
			owner = this.ownerService.edit(ownerForm);
		} catch (Throwable oops) {
			owner = null;
		}
		
		assertTrue(owner == null);
	}
	
	/*
	 * Un owner edita su perfil
	 * La username ya existe.
	 */
	@WithMockUser("owner2")
	@Transactional
	@Test
	public void negativeTest_edit_dos() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch, iban;
		OwnerForm ownerForm;
		Owner owner;
		
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "owner1";
		password = "owner2";
		passwordMatch = "owner2";
		iban = "ES1501287541639316325975";
	
		ownerForm = this.getOwnerForm(name, surname, email, telephoneNumber, username, password, passwordMatch, iban);
		
		try {
			owner = this.ownerService.edit(ownerForm);
		} catch (Throwable oops) {
			owner = null;
		}
		
		assertTrue(owner == null);
	}
	
	/*
	 * Un owner edita su perfil
	 * Datos no validos en atributos.
	 */
	@WithMockUser("owner2")
	@Transactional
	@Test
	public void negativeTest_edit_tres() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch, iban;
		OwnerForm ownerForm;
		Owner owner;
		
		name = "";
		surname = "";
		email = "jules##";
		telephoneNumber = "";
		username = "owner1";
		password = "owner2";
		passwordMatch = "owner2";
		iban = "ES1501287541639316325975";
	
		ownerForm = this.getOwnerForm(name, surname, email, telephoneNumber, username, password, passwordMatch, iban);
		
		try {
			owner = this.ownerService.edit(ownerForm);
		} catch (Throwable oops) {
			owner = null;
		}
		
		assertTrue(owner == null);
	}
	
	private OwnerForm getOwnerForm(String name, String surname, String email, String telephoneNumber, String username,
			String password, String passwordMatch, String iban) {
		OwnerForm result;

		result = new OwnerForm(name, surname, email, telephoneNumber, username);
		result.setPassword(password);
		result.setPasswordMatch(passwordMatch);
		result.setIban(iban);

		return result;
	}
	
}
