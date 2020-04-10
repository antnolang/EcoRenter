package com.ispp.EcoRenter.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.security.test.context.support.WithMockUser;

import com.ispp.EcoRenter.model.EcoTruki;
import com.ispp.EcoRenter.service.EcoTrukiService;

@SpringBootTest
public class EcoTrukiServiceTest {

	// Service under testing -------------------
	@Autowired
	private EcoTrukiService ecoTrukiService;
	
	@WithMockUser("admin1")
	@Test
	@Transactional
	public void test_findOne() {
		int ecoTrukiId;
		EcoTruki ecoTruki;
		
		ecoTrukiId = 800;
		ecoTruki = this.ecoTrukiService.findOne(ecoTrukiId);
		
		assertNotNull(ecoTruki);
	}
	
	@WithMockUser("admin1")
	@Test
	public void test_findAll() {
		Page<EcoTruki> all;
		
		all = this.ecoTrukiService.findAll(1);
		
		assertNotNull(all);
		assertTrue(all.hasContent());
	}
	
}
