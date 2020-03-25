package com.ispp.EcoRenter.controller.administrator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.form.AdminForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.service.ActorService;

@Controller
@RequestMapping("/actor/administrator")
public class ActorAdministratorController {

	private static final Log log = LogFactory.getLog(ActorAdministratorController.class);
	
	@Autowired
	private ActorService actorService;
	
	
	public ActorAdministratorController() {
		super();
	}
	
	@GetMapping(value = "/ban")
	public ModelAndView ban(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;
		
		try {
			actor = this.actorService.findOne(actorId);
			
			this.actorService.ban(actor);
			
			result = new ModelAndView("redirect:/actor/display?actorId=" + actorId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
			
			log.info("Algo salio mal al banear un actor.");
		}

		return result;
	}
	
	@GetMapping(value = "/unban")
	public ModelAndView unBan(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;
		
		try {
			actor = this.actorService.findOne(actorId);
			
			this.actorService.unBan(actor);
			
			result = new ModelAndView("redirect:/actor/display?actorId=" + actorId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
			
			log.info("Algo salio mal al banear un actor.");
		}

		return result;
	}
	
	// Metodos auxiliares ---------------------------------------------------
	public ModelAndView createEditModelAndView(AdminForm adminForm) {
		ModelAndView result;
		
		result = new ModelAndView("/actor/edit");
		result.addObject("objectForm", adminForm);
		result.addObject("buttonName", "saveAdmin");
		
		return result;
	}
	
	public ModelAndView createEditModelAndView(AdminForm adminForm, String messageName, String messageValue) {
		ModelAndView result;
		
		result = new ModelAndView("actor/edit");
		result.addObject("objectForm", adminForm);
		result.addObject(messageName, messageValue);
		result.addObject("buttonName", "saveAdmin");
	
		return result;
	}
	
}
