package com.ispp.EcoRenter.controller.renter;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.CreditCard;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.service.CreditCardService;
import com.ispp.EcoRenter.service.RenterService;

@Controller
@RequestMapping("/credit-card/renter")
public class CreditCardRenterController {

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private RenterService renterService;
	
	public CreditCardRenterController() {
		super();
	}
	
	@GetMapping("/list")
	public ModelAndView list() {
		Collection<CreditCard> creditCards;
		ModelAndView result;
		Renter principal;
		
		principal = this.renterService.findByPrincipal();
		
		creditCards = principal.getCreditCards();
		
		result = new ModelAndView("creditCard/list");
		result.addObject("creditCards", creditCards);
		result.addObject("renter", principal);
		
		return result;
	}
	
	@GetMapping("/create")
	public ModelAndView create() {
		CreditCard creditCard;
		ModelAndView result;
		
		creditCard = this.creditCardService.create();
		
		result = this.createEditModelAndView(creditCard);
		
		return result;
	}
	
	@GetMapping("/edit")
	public ModelAndView edit(@RequestParam int creditCardId) {
		ModelAndView result;
		CreditCard creditCard;
		
		try {
			creditCard = this.creditCardService.findOneToEdit(creditCardId);
			
			result = this.createEditModelAndView(creditCard);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}
		
		return result;
	}
	
	@PostMapping(value = "/edit", params = "save")
	public ModelAndView save(@Valid CreditCard creditCard, BindingResult binding) {
		ModelAndView result;
		String message;
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(creditCard);
		} else {
			try {
				this.creditCardService.save(creditCard);
				
				result = new ModelAndView("redirect:/actor/authenticated/display");
			} catch (Throwable oops) {
				message = oops.getMessage();
				
				if (message.equals("Acceso inválido")) {
					result = this.createEditModelAndView(creditCard, message);
				} else {
					result = this.createEditModelAndView(creditCard, "No se pudo completar la operación");
				}
			}
		}
		
		return result;
	}
	
	
	protected ModelAndView createEditModelAndView(CreditCard creditCard) {
		return this.createEditModelAndView(creditCard, null);
	}
	
	protected ModelAndView createEditModelAndView(CreditCard creditCard, String messageCode) {
		ModelAndView result;
		
		result = new ModelAndView("creditCard/edit");
		result.addObject("creditCard", creditCard);
		result.addObject("messageCode", messageCode);
		
		return result;
	}
	
}
