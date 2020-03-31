package com.ispp.EcoRenter.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

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

	@Autowired
    private Validator validator;
	
	public CustomisationService() {
		super();
	}
	
	public Customisation save(Customisation customisation) {
		Assert.notNull(customisation, "No puede ser nulo");
		Assert.isTrue(this.customisationRepository.existsById(customisation.getId()) && customisation.equals(this.find()), "No existe la customisación en BD");

		Customisation result;

		result = this.customisationRepository.save(customisation);

		return result;
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

	public Customisation reconstruct(Customisation customisation, BindingResult binding) {
		Customisation custo;
		Customisation result;

		custo = this.find();

		result = new Customisation();

		result.setId(custo.getId());
		result.setVersion(custo.getVersion());

		result.setEmail(customisation.getEmail());
		result.setDiscountCodes(customisation.getDiscountCodes());
		result.setGoldLevel(customisation.getGoldLevel());
		result.setSilverLevel(customisation.getSilverLevel());

		this.validator.validate(result, binding);

		return result;
	}
	
}
