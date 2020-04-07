package com.ispp.EcoRenter.controller.renter;

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.form.CreditCardForm;
import com.ispp.EcoRenter.model.CreditCard;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.service.CreditCardService;
import com.ispp.EcoRenter.service.RentOutService;
import com.ispp.EcoRenter.service.RenterService;

@Controller
@RequestMapping("/credit-card/renter")
public class CreditCardRenterController {

	@Autowired
	private CreditCardService creditCardService;
	
	@Autowired
	private RenterService renterService;
	
	@Autowired
	private RentOutService rentOutService;
	
	public CreditCardRenterController() {
		super();
	}
	
	@GetMapping("/list")
	public ModelAndView list() {
		Collection<CreditCard> creditCards;
		Map<Integer, String> mapa;
		ModelAndView result;
		Renter principal;
		
		principal = this.renterService.findByPrincipal();
		
		creditCards = principal.getCreditCards();
		
		mapa = this.creditCardService.getCreditCardEncodedNumber(creditCards);
		
		result = new ModelAndView("creditCard/list");
		result.addObject("creditCards", creditCards);
		result.addObject("renter", principal);
		result.addObject("mapa", mapa);
		
		return result;
	}
	
	@GetMapping("/create")
	public ModelAndView create() {
		CreditCardForm creditCardForm;
		Renter principal;
		ModelAndView result;
		
		principal = this.renterService.findByPrincipal();
		
		creditCardForm = new CreditCardForm();
		creditCardForm.setRenter(principal);
		
		result = this.createEditModelAndView(creditCardForm);
		
		return result;
	}
	
	@GetMapping("/edit")
	public ModelAndView edit(@RequestParam int creditCardId) {
		CreditCardForm creditCardForm;
		ModelAndView result;
		CreditCard creditCard;
		Renter principal;
		
		try {
			creditCard = this.creditCardService.findOneToEdit(creditCardId);
		
			principal = this.renterService.findByPrincipal();
			
			creditCardForm = new CreditCardForm(creditCard.getId(),
												creditCard.getVersion(),
												creditCard.getHolderName(),
												creditCard.getBrandName(),
												creditCard.getNumber(),
												creditCard.getExpirationMonth(),
												creditCard.getExpirationYear(),
												creditCard.getCvvCode(),
												principal);
			
			result = this.createEditModelAndView(creditCardForm);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}
		
		return result;
	}
	
	@PostMapping(value = "/edit", params = "save")
	public ModelAndView save(@Valid CreditCardForm creditCardForm,
							 BindingResult binding) {
		ModelAndView result;
		String message;
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(creditCardForm);
		} else {
			try {
				this.creditCardService.save(creditCardForm);
				
				result = new ModelAndView("redirect:list");
			} catch (Throwable oops) {
				message = oops.getMessage();
				
				if (message.equals("Acceso inválido")) {
					result = this.createEditModelAndView(creditCardForm, message);
				} else if (message.equals("La tarjeta de crédito está expirada")) {
					result = this.createEditModelAndView(creditCardForm, message);	
				} else if (message.equals("Marca desconocida")) {
					result = this.createEditModelAndView(creditCardForm, message);
				} else {
					result = this.createEditModelAndView(
									creditCardForm, 
							       "No se pudo completar la operación"
					);
				}
			}
		}
		
		return result;
	}
	
	@PostMapping(value = "/edit", params = "delete")
	public ModelAndView delete(CreditCardForm creditCardForm, BindingResult binding) {
		ModelAndView result;
		CreditCard creditCard;
		
		try {
			creditCard = this.creditCardService.findOneToEdit(creditCardForm.getId());
			
			this.creditCardService.delete(creditCard);
			
			result = new ModelAndView("redirect:list");
		} catch (Throwable oops) {
			result = this.createEditModelAndView(
					 creditCardForm,
					"No se pudo completar la operación"
			);
		}
		
		return result;
	}
	
	protected ModelAndView createEditModelAndView(CreditCardForm creditCardForm) {
		return this.createEditModelAndView(creditCardForm, null);
	}
	
	protected ModelAndView createEditModelAndView(CreditCardForm creditCardForm, 
												  String messageCode) {
		Collection<String> creditCardMakes;
		ModelAndView result;
		int count, creditCardId;
		
		creditCardId = creditCardForm.getId();
		
		count = (creditCardId > 0) 
				? this.rentOutService.findRentOutByCreditCard(creditCardId)
				: 0;
		
		creditCardMakes = this.creditCardService.getCreditCardMakes();
				
		result = new ModelAndView("creditCard/edit");
		result.addObject("creditCardForm", creditCardForm);
		result.addObject("creditCardMakes", creditCardMakes);
		result.addObject("messageCode", messageCode);
		result.addObject("count", count);
		
		return result;
	}
	
}
