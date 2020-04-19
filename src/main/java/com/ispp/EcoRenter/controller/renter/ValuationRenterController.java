package com.ispp.EcoRenter.controller.renter;

import javax.websocket.server.PathParam;

import com.ispp.EcoRenter.model.Valuation;
import com.ispp.EcoRenter.service.ValuationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/renter/valuation")
public class ValuationRenterController {

    // Servicio

    @Autowired
    private ValuationService valuationService;

    // Constructor

    public ValuationRenterController(){
        super();
    }

    // Create

    @GetMapping("/create")
    public ModelAndView create(@RequestParam int rentOutId){
        ModelAndView result;
		Valuation valuation;

		try {
			valuation = this.valuationService.create();
			result = this.createEditModelAndView(valuation, rentOutId);
			this.valuationService.checkNew(rentOutId);
		} catch(Exception oops){
			result = new ModelAndView("redirect:/miscellaneous/error");
		}
		

		return result;
    }

    // Save

	@PostMapping(value = "/edit", params = "save")
	public ModelAndView save(@PathParam("rentOutId") int rentOutId, Valuation valuation, final BindingResult binding) {
		ModelAndView result;
		Valuation valuationRec;

		valuationRec = this.valuationService.reconstruct(valuation, binding);
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(valuation, rentOutId);
		} else {
			try {
                this.valuationService.save(valuationRec, rentOutId);
                result = new ModelAndView("redirect:/rentOut/list");
			} catch (final Throwable oops) {
                result = this.createEditModelAndView(valuationRec, rentOutId, "No se pudo realizar la operación");
			}
		}

		return result;
    }


	// Ancillary methods
	
	protected ModelAndView createEditModelAndView(final Valuation valuation, int rentOutId) {
		ModelAndView result;

		result = this.createEditModelAndView(valuation, rentOutId,null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Valuation valuation, int rentOutId, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("valuation/edit");
        result.addObject("valuation", valuation);
        result.addObject("rentOutId", rentOutId);
		result.addObject("messageCode", messageCode);

		return result;
	}

}