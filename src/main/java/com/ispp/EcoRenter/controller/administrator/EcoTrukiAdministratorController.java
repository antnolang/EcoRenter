package com.ispp.EcoRenter.controller.administrator;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.form.EcoTrukiForm;
import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.EcoTruki;
import com.ispp.EcoRenter.service.CustomisationService;
import com.ispp.EcoRenter.service.EcoTrukiService;

@Controller
@RequestMapping("/eco-truki/administrator")
public class EcoTrukiAdministratorController {

	@Autowired
	private EcoTrukiService ecoTrukiService;
	
	@Autowired
	private CustomisationService customisationService;
	
	public EcoTrukiAdministratorController() {
		super();
	}
	
	
	@GetMapping("/create")
	public ModelAndView create() {
		ModelAndView result;
		EcoTrukiForm ecoTrukiForm;
		
		ecoTrukiForm = new EcoTrukiForm();
		
		result = this.createEditModelAndView(ecoTrukiForm);
		
		return result;
	}
	
	@GetMapping("/edit")
	public ModelAndView edit(@RequestParam int ecoTrukiId) {
		ModelAndView result;
		EcoTruki ecoTruki;
		EcoTrukiForm ecoTrukiForm;
		
		result = null;
		
		try {
			ecoTruki = this.ecoTrukiService.findOne(ecoTrukiId);
			
			if (ecoTruki != null) {
				ecoTrukiForm = new EcoTrukiForm(ecoTrukiId,
												ecoTruki.getTitle(),
												ecoTruki.getDescription());
				
				result = this.createEditModelAndView(ecoTrukiForm);
			}
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}
		
		return result;
	}
	
	@PostMapping(value = "/edit", params = "save")
	public ModelAndView save(@Valid EcoTrukiForm ecoTrukiForm, BindingResult binding) {
		ModelAndView result;
		String message;
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(ecoTrukiForm);
		} else {
			try {
				this.ecoTrukiService.save(ecoTrukiForm);
				
				result = new ModelAndView("redirect:/eco-truki/authenticated/list");
			} catch (Throwable oops) {
				message = oops.getMessage();
				
				if (message.equals("No es una imagen")) {
					result = this.createEditModelAndView(ecoTrukiForm, 
														 "messagePhoto",
														 message);
				} else if (message.equals("La imagen supera el tamaño máximo")) {
					result = this.createEditModelAndView(ecoTrukiForm, 
							 							 "messagePhoto",
							 							 message);
				} else {
					result = this.createEditModelAndView(ecoTrukiForm,
														 "messageCode",
														 message);
				}
				
			}
		}
		
		return result;
	}
	
	@PostMapping(value = "/edit", params = "delete")
	public ModelAndView delete(EcoTrukiForm ecoTrukiForm) {
		ModelAndView result;
		EcoTruki ecoTruki;
		
		try {
			ecoTruki = this.ecoTrukiService.findOne(ecoTrukiForm.getId());
			
			if (ecoTruki != null) {
				this.ecoTrukiService.delete(ecoTruki);
			}
			
			result = new ModelAndView("redirect:/eco-truki/authenticated/list");
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}
		
		return result;
	}
	
	// Ancillary methods ------------------------
	protected ModelAndView createEditModelAndView(EcoTrukiForm ecoTrukiForm) {
		ModelAndView result;
		Customisation custo;
		
		custo = this.customisationService.find();
		
		result = new ModelAndView("ecoTruki/edit");
		result.addObject("ecoTrukiForm", ecoTrukiForm);
		result.addObject("maxSizePhoto", custo.getMaxSizePhoto());
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(EcoTrukiForm ecoTrukiForm, String messageName,
			String messageValue) {
		ModelAndView result;
		
		result = this.createEditModelAndView(ecoTrukiForm);
		result.addObject(messageName, messageValue);
		
		return result;
	}
	
}
