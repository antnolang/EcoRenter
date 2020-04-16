package com.ispp.EcoRenter.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.service.ActorService;

@SpringBootTest
public class ActorServiceTest {

	@Autowired
	private ActorService actorService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	@Test
	public void test_generateHashPassword() {
		String encodedPassword;
		List<String> pass;
		
		pass = new ArrayList<String>();
		pass.add("arrendatario1");
		pass.add("arrendatario2");
		pass.add("arrendatario3");
		pass.add("arrendatario4");
		pass.add("arrendatario5");
		pass.add("arrendatario6");
		
		pass.add("propietario1");
		pass.add("propietario2");
		pass.add("propietario3");
		pass.add("propietario4");
		pass.add("propietario5");
		pass.add("propietario6");
		
		for (String s: pass) {
			encodedPassword = this.passwordEncoder.encode(s);
			
			System.out.println("Password clara: " + s);
			System.out.println("Password cifrada: " + encodedPassword);
			System.out.println("---------------------");
		}
		
	}
	
	/*
	 * Un administrador puede visitar cualquier perfil
	 */
	@WithMockUser("admin2")
	@Transactional
	@Test
	public void positiveTest_findOne_one() {
		int actorId;
		Actor actor;
		
		// Id de un administrador (admin3)
		actorId = 102;
		actor = this.actorService.findOne(actorId);
		
		assertNotNull(actor);
	}
	
	/*
	 * Un administrador puede acceder a su perfil
	 */
	@WithMockUser("admin2")
	@Test
	@Transactional
	public void positiveTest_findOne_two() {
		int actorId;
		Actor actor;
		
		actorId = 101;
		actor = this.actorService.findOne(actorId);
		
		assertNotNull(actor);
	}
	
	/*
	 * Un administrador puede accede a un perfil
	 */
	@WithMockUser("admin2")
	@Test
	@Transactional
	public void positiveTest_findOne_three() {
		
		int actorId;
		Actor actor;
		
		// id de un propietario (owner2)
		actorId = 201;
		actor = this.actorService.findOne(actorId);
		
		assertNotNull(actor);
	}
	
	/*
	 * Un owner puede acceder al perfil de un arrendatario
	 */
	@WithMockUser("owner2")
	@Test
	@Transactional
	public void positiveTest_findOne_four() {
		int actorId;
		Actor actor;
		
		// id de un arrendatario (renter2)
		actorId = 301;
		actor = this.actorService.findOne(actorId);
		
		assertNotNull(actor);
	}
	
	
	/*
	 * Un renter puede acceder al perfil de un propietario
	 */
	@WithMockUser("renter2")
	@Test
	@Transactional
	public void positiveTest_findOne_five() {
		int actorId;
		Actor actor;
		
		// id de un arrendatario (owner2)
		actorId = 201;
		actor = this.actorService.findOne(actorId);
		
		assertNotNull(actor);
	}
	
	/*
	 * Un owner puede acceder al perfil de un arrendatario.
	 * Sin embargo, el owner está baneado.
	 */
	@WithMockUser("owner6")
	@Test
	@Transactional
	public void negativeTest_findOne_one() {
		int actorId;
		Actor actor;
		
		// id de un arrendatario (renter2)
		actorId = 301;
		
		try {
			actor = this.actorService.findOne(actorId);
		} catch (IllegalArgumentException e) {
			actor = null;
		}
		
		assertTrue(actor == null);
	}
	
	/*
	 * Un owner no puede acceder al perfil de otro propietario.
	 */
	@WithMockUser("owner3")
	@Test
	@Transactional
	public void negativeTest_findOne_two() {
		int actorId;
		Actor actor;
		
		// id de un propietario (owner2)
		actorId = 201;
		
		try {
			actor = this.actorService.findOne(actorId);
		} catch (IllegalArgumentException e) {
			actor = null;
		}
		
		assertTrue(actor == null);
	}
	
	/*
	 * Un arrendatario no puede acceder al perfil de otro arrendatario.
	 */
	@WithMockUser("renter2")
	@Test
	@Transactional
	public void negativeTest_findOne_three() {	
		int actorId;
		Actor actor;
		
		// id de un propietario (renter2)
		actorId = 302;
		
		try {
			actor = this.actorService.findOne(actorId);
		} catch (IllegalArgumentException e) {
			actor = null;
		}
		
		assertTrue(actor == null);
	}
	
	/*
	 * Req 10.1: Un administrador banea a un actor
	 */
	@WithMockUser("admin2")
	@Test
	@Transactional
	public void positiveTest_ban_uno() {
		int actorId;
		Actor actor;
		
		// Id deun propietario (owner2)
		actorId = 302;
		actor = this.actorService.findOne(actorId);
		
		try {
			this.actorService.ban(actor);
			
			actor = this.actorService.findOne(actorId);
			
			assertTrue(actor.getUserAccount().getIsBanned(), "Actor baneado");
		} catch (IllegalArgumentException e) {
			
			assertTrue(actor != null);
		}
		
	}
	
	/*
	 * Req 10.1: Un administrador banea a un actor
	 * El actor ya está baneado
	 * 
	 */
	@WithMockUser("admin2")
	@Test
	@Transactional
	public void negativeTest_ban_uno() {
		int actorId;
		Actor actor;
		
		// Id deun propietario (owner6)
		actorId = 205;
		actor = this.actorService.findOne(actorId);
		
		try {
			this.actorService.ban(actor);
			
			actor = this.actorService.findOne(actorId);
			
			assertTrue(actor.getUserAccount().getIsBanned(), "Actor ya esta baneado");
		} catch (IllegalArgumentException e) {
			
			assertTrue(actor.getUserAccount().getIsBanned(), "Actor ya esta baneado");
		
		}
		
	}
	
	/*
	 * Req 10.1: Un administrador banea a un actor
	 * El actor no es un administrator, es un renter
	 * 
	 */
	@WithMockUser("renter2")
	@Test
	@Transactional
	public void negativeTest_ban_dos() {
		int actorId;
		Actor actor;
		
		// Id de un propietario (owner3)
		actorId = 202;
		actor = this.actorService.findOne(actorId);
		
		try {
			this.actorService.ban(actor);
			
			actor = this.actorService.findOne(actorId);
			
			assertTrue(!actor.getUserAccount().getIsBanned(), "Actor ya esta baneado");
		} catch (IllegalArgumentException e) {
			
			assertTrue(!actor.getUserAccount().getIsBanned(), "Actor ya esta baneado");
		
		}
		
	}
	
	/*
	 * Req 10.1: Un administrador desbanea a un actor
	 */
	@WithMockUser("admin2")
	@Test
	@Transactional
	public void positiveTest_unban_uno() {
		int actorId;
		Actor actor;
		
		// Id deun propietario (owner6)
		actorId = 205;
		actor = this.actorService.findOne(actorId);
		
		try {
			this.actorService.unBan(actor);
			
			actor = this.actorService.findOne(actorId);
			
			assertTrue(!actor.getUserAccount().getIsBanned(), "Actor baneado");
		} catch (IllegalArgumentException e) {
			
			assertTrue(actor != null);
		}
		
	}
	
	/*
	 * Req 10.1: Un administrador desbanea a un actor
	 * No se puede desbanear a un actor que no esta baneado
	 * 
	 */
	@WithMockUser("admin2")
	@Test
	@Transactional
	public void negativeTest_unban_dos() {
		int actorId;
		Actor actor;
		
		// Id deun propietario (owner5)
		actorId = 204;
		actor = this.actorService.findOne(actorId);
		
		try {
			this.actorService.unBan(actor);
			
			actor = this.actorService.findOne(actorId);
			
			assertTrue(!actor.getUserAccount().getIsBanned(), "Actor ya esta baneado");
		} catch (IllegalArgumentException e) {
			
			assertTrue(!actor.getUserAccount().getIsBanned(), "Actor ya esta baneado");
		
		}
		
	}
	
	/*
	 * Req 10.1: Un administrador banea a un actor
	 * El actor no es un administrator, es un renter
	 * 
	 */
	@WithMockUser("renter2")
	@Test
	@Transactional
	public void negativeTest_unban_three() {
		int actorId;
		Actor actor;
		
		// Id deun propietario (owner6)
		actorId = 205;
		actor = this.actorService.findOne(actorId);
		
		try {
			this.actorService.unBan(actor);
			
			actor = this.actorService.findOne(actorId);
			
			assertTrue(actor.getUserAccount().getIsBanned(), "Actor ya esta baneado");
		} catch (IllegalArgumentException e) {
			
			assertTrue(actor.getUserAccount().getIsBanned(), "Actor ya esta baneado");
		
		}
		
	}
	
}
