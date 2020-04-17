package com.ispp.EcoRenter.controller.administrator;


import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.CustomisationService;
import com.ispp.EcoRenter.service.SmallholdingService;

@Controller
@RequestMapping("/administrator/customisation")
public class CustomisationAdministratorController {
    
    // Servicio

    @Autowired
    private CustomisationService customisationService;
    
    @Autowired
    private SmallholdingService smallholdingService;

    public CustomisationAdministratorController(){
        super();
    }

    @GetMapping("/display")
    public ModelAndView display(){
        ModelAndView result;
        Customisation customisation;

        customisation = this.customisationService.find();

        result = new ModelAndView("customisation/display");
        result.addObject("customisation", customisation);
        
        return result;
    }

    @GetMapping("/edit")
    public ModelAndView edit(){
        ModelAndView result;
        Customisation customisation;

        customisation = this.customisationService.find();

        result = this.editModelAndView(customisation);

        return result;
    }

    @PostMapping(value = "/edit", params = "save")
    public ModelAndView save(Customisation customisation, BindingResult binding) {
        ModelAndView result;
        Customisation custo;

        custo = this.customisationService.reconstruct(customisation, binding);

        if(binding.hasErrors())
            result = this.editModelAndView(customisation);
        else
            try {
                this.customisationService.save(custo);
                result = new ModelAndView("redirect:display");
            } catch (Throwable oops) {
                result = this.editModelAndView(custo, "No se pudo realizar la operación");
            }

        return result;
    }

    
    @PostMapping(value = "/close", params = "closeDispute")
    public ModelAndView close(@PathParam("smallholdingId") int smallholdingId) {
    	ModelAndView result;
    	Smallholding small = this.smallholdingService.findOne(smallholdingId);
    	try {
    		small.setArgumented(false);
    		this.smallholdingService.saveDispute(small);
    		
    		result = new ModelAndView("redirect:/");
    		
    	}catch(Throwable oops) {
    		result = new ModelAndView("redirect:/administrator/customisation/edit");
    		result.addObject("closeError", "No se pudo cerrar la disputa, intentelo de nuevo");
    	}
    	
    		
    	return result;
    	
        
    }

    // Ancillary methods

    protected ModelAndView editModelAndView(Customisation customisation) {
        ModelAndView result;

        result = this.editModelAndView(customisation, null);

        return result;
    }

    protected ModelAndView editModelAndView(final Customisation customisation, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("customisation/edit");
		result.addObject("customisation", customisation);
		result.addObject("messageCode", messageCode);

		return result;
	}
}