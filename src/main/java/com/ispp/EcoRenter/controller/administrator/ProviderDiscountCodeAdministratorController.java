package com.ispp.EcoRenter.controller.administrator;

import javax.validation.Valid;

import com.ispp.EcoRenter.model.ProviderDiscountCode;
import com.ispp.EcoRenter.service.ProviderDiscountCodeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/administrator/providerDiscountCode")
public class ProviderDiscountCodeAdministratorController {

    // Service

    @Autowired
    private ProviderDiscountCodeService providerDiscountCodeService;

    // Constructor

    public ProviderDiscountCodeAdministratorController(){
        super();
	}
	
	// Create

	@GetMapping("/create")
	public ModelAndView create() {
		ModelAndView result;
		ProviderDiscountCode providerDiscountCode;

		providerDiscountCode = this.providerDiscountCodeService.create();

		result = this.createEditModelAndView(providerDiscountCode);

		return result;
	}

    // Edit

    @GetMapping("/edit")
    public ModelAndView edit(@RequestParam final int providerDiscountCodeId){
        ModelAndView result;
        ProviderDiscountCode providerDiscountCode;

        try {
            providerDiscountCode = this.providerDiscountCodeService.findOneToDisplay(providerDiscountCodeId);
            
            result = this.createEditModelAndView(providerDiscountCode);
        } catch (final Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

        return result;
    }

    // Save

	@PostMapping(value = "/edit", params = "save")
	public ModelAndView save(@Valid ProviderDiscountCode providerDiscountCode, final BindingResult binding) {
		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(providerDiscountCode);
		} else {
			try {
                this.providerDiscountCodeService.save(providerDiscountCode);
                result = new ModelAndView("redirect:/providerDiscountCode/list");
			} catch (final DataIntegrityViolationException oops) {
				providerDiscountCode.setId(0);
				result = this.createEditModelAndView(providerDiscountCode, "Ya existe un descuento para esa página");
			} catch (final Throwable oops) {
				providerDiscountCode.setId(0);
				if(oops.getMessage().equals("El código de descuento que se ha introducido no es válido"))
					result = this.createEditModelAndView(providerDiscountCode, oops.getMessage());
				else
					result = this.createEditModelAndView(providerDiscountCode, "No se pudo realizar la operación");
			}
		}

		return result;
    }
    
    // Save

	@PostMapping(value = "/edit", params = "delete")
	public ModelAndView delete(ProviderDiscountCode providerDiscountCode, final BindingResult binding) {
		ModelAndView result;
		
		try {
            this.providerDiscountCodeService.delete(providerDiscountCode);
            result = new ModelAndView("redirect:/providerDiscountCode/list");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(providerDiscountCode, "No se pudo realizar la operación");
		}

		return result;
	}

    // Ancillary methods

	protected ModelAndView createEditModelAndView(final ProviderDiscountCode providerDiscountCode) {
		ModelAndView result;

		result = this.createEditModelAndView(providerDiscountCode, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ProviderDiscountCode providerDiscountCode, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("providerDiscountCode/edit");
		result.addObject("providerDiscountCode", providerDiscountCode);
		result.addObject("messageCode", messageCode);

		return result;
	}

}