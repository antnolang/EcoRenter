package com.ispp.EcoRenter.controller.renter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.register.RenterRegister;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.RenterService;

@Controller
@RequestMapping("/actor/renter")
public class ActorRenterController {

	private static final Log log = LogFactory.getLog(ActorRenterController.class);
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private RenterService renterService;
	
	
	public ActorRenterController() {
		super();
	}

	@GetMapping(value = "/display")
	public ModelAndView findOne(@RequestParam(required = false, defaultValue = "0") int actorId) {
		ModelAndView result;
		Actor actor;
		Renter principal;
		boolean isMyProfile;
		String iban;
		
		principal = this.renterService.findByPrincipal();
		
		isMyProfile = actorId == principal.getId();
		
		actor = this.actorService.findOne(principal.getId());
		
		result = new ModelAndView("actor/display");
		
		if (isMyProfile) {
			iban = this.actorService.getEncodedIban(principal.getIban());
		
			result.addObject("iban", iban);
			
			log.info("Es un arrendatario.");
		}
		
		result.addObject("actor", actor);
		
		return result;
	}
	
	@GetMapping(value = "/register")
	public String register(Model model) {
		String result = "actor/renterRegister";
		
		RenterRegister renter = new RenterRegister();
		
		model.addAttribute("user",renter);
		
		return result;
	}
	
	
}
