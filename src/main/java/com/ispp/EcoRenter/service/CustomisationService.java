package com.ispp.EcoRenter.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.repository.CustomisationRepository;

@Service
@Transactional
public class CustomisationService {

	// Repositorio -----------------------------------------------
	@Autowired
	private CustomisationRepository customisationRepository;
	
	// Otros servicios -------------------------------------------
	
	public CustomisationService() {
		super();
	}
	
	
	public Customisation find() {
		Customisation result;
		Customisation[] all;

		all = this.customisationRepository.find();
		Assert.isTrue(all.length == 1, "Solo un objeto customisation");

		result = all[0];

		return result;
	}
		
	public String getLevelByOwner(Owner owner) {
		String result;
		Customisation customisation;
		int silverLevel, goldLevel, months;
		
		customisation = this.find();
		
		months = owner.getAccumulatedMonths();
		
		silverLevel = customisation.getSilverLevel();
		goldLevel = customisation.getGoldLevel();
		
		if (months < silverLevel) {
			result = "Bronce";
		} else if (months >= silverLevel && months < goldLevel) {
			result = "Plata";
		} else {
			result = "Oro";
		}
		
		return result;
	}
	
}
