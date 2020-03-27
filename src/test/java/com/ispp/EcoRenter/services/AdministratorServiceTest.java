package com.ispp.EcoRenter.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import com.ispp.EcoRenter.form.AdminForm;
import com.ispp.EcoRenter.model.Administrator;
import com.ispp.EcoRenter.service.AdministratorService;

@SpringBootTest
public class AdministratorServiceTest {

	// Service under testing ----------------
	@Autowired
	private AdministratorService administratorService;
	
	/*
	 * Un administrador edita su perfil
	 */
	@WithMockUser("admin2")
	@Transactional
	@Test
	public void positiveTest_edit_one() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch;
		Administrator administrator;
		AdminForm adminForm;
		
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "admin2Edited";
		password = username;
		passwordMatch = password;
		
		adminForm = this.getAdminForm(name, surname, email, telephoneNumber,
									  username, password, passwordMatch);
			
		administrator = this.administratorService.edit(adminForm);
		
		assertNotNull(administrator);
		assertTrue(administrator.getId() > 0);
	}
	
	/*
	 * Un administrador edita su perfil
	 * Las contraseñas no coinciden
	 */
	@WithMockUser("admin2")
	@Transactional
	@Test
	public void negativeTest_edit_one() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch;
		Administrator administrator;
		AdminForm adminForm;
				
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "admin2Edited";
		password = username;
		passwordMatch = "Obviamente no coinciden";
						
		adminForm = this.getAdminForm(name, surname, email, telephoneNumber,
									  username, password, passwordMatch);
			
		try {
			administrator = this.administratorService.edit(adminForm);
		} catch (Throwable oops) {
			administrator = null;
		}
		
		assertTrue(administrator == null);
	}
	
	
	/*
	 * Un administrador edita su perfil
	 * El usuario decide actualizar su username, pero dicho username ya existe
	 */
	@WithMockUser("admin2")
	@Transactional
	@Test
	public void negativeTest_edit_dos() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch;
		Administrator administrator;
		AdminForm adminForm;
				
		name = "Julia";
		surname = "Arenas Mellado";
		email = "jules@hotmail.com";
		telephoneNumber = "+34 696465208";
		username = "admin1";
		password = "admin2";
		passwordMatch = "admin2";
		
		adminForm = this.getAdminForm(name, surname, email, telephoneNumber,
									  username, password, passwordMatch);
			
		try {
			administrator = this.administratorService.edit(adminForm);
		} catch (Throwable oops) {
			administrator = null;
		}
		
		assertTrue(administrator == null);
	}

	/*
	 * Un administrador edita su perfil
	 * Valores no validos en los atributos
	 */
	@WithMockUser("admin2")
	@Transactional
	@Test
	public void negativeTest_edit_tres() {
		String name, surname, email, telephoneNumber, username, password, passwordMatch;
		Administrator administrator;
		AdminForm adminForm;
				
		name = "55";
		surname = "";
		email = "jules# fdfdf";
		telephoneNumber = "";
		username = "admin2";
		password = "admin2";
		passwordMatch = "admin2";
		
		adminForm = this.getAdminForm(name, surname, email, telephoneNumber,
									  username, password, passwordMatch);
			
		try {
			administrator = this.administratorService.edit(adminForm);
		} catch (Throwable oops) {
			administrator = null;
		}
		
		assertTrue(administrator == null);
	}
	

	private AdminForm getAdminForm(String name, String surname, String email, String telephoneNumber, String username, String password, String passwordMatch) {
		AdminForm result;
		
		result = new AdminForm(name, surname, email, telephoneNumber, username);
		result.setPassword(password);
		result.setPasswordMatch(passwordMatch);
		
		return result;
	}
	
}
