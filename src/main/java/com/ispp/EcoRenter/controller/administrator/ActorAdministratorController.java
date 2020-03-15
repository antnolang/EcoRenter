package com.ispp.EcoRenter.controller.administrator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	
}
