package com.ispp.EcoRenter.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.CustomisationService;
import com.ispp.EcoRenter.service.OwnerService;
import com.ispp.EcoRenter.service.RenterService;

@Controller
@RequestMapping("/actor")
public class ActorController {

	private static final Log log = LogFactory.getLog(ActorController.class);
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private CustomisationService customisationService;
	
	@Autowired
	private RenterService renterService;
	
	public ActorController() {
		super();
	}
	
	@GetMapping(value = "/display")
	public ModelAndView findOne(@RequestParam(required = false, defaultValue = "0") int actorId) {
		ModelAndView result;
		Customisation customisation;
		Actor actor, principal;
		boolean isMyProfile;
		Renter renter;
		Owner owner;
		int principalId;
		String iban, role, level, discountCodes, ecoTruki;
		
		iban = "";
		
		try {
			result = new ModelAndView("actor/display");
			
			principal = this.actorService.findByPrincipal();
			principalId = principal.getId();
			
			/* Si actorId == 0 es cero, significa que el principal quiere mostrar su perfil o bien
			 * puede ser que un usuario haya indicado en la url el valor cero para actorId. En ambos
			 * casos cargaremos en el modelo al actor principal.
			 * 
			 * Si actorId != 0, entonces un usuario estará tratando de visualizar el perfil de otro usuario
			 * o bien habrá introducido su id. En cuyo caso habrá que comprobar si principal y actor coinciden.
			 * 
			 */ 
			isMyProfile = actorId == 0 || principalId == actorId;
			
			if (isMyProfile) {
				renter = this.renterService.findOne(principalId);
				owner = this.ownerService.findOne(principalId);
				
				if (renter != null) {
					iban = this.actorService.getEncodedIban(renter.getIban());
				} else if (owner != null){
					iban = this.actorService.getEncodedIban(owner.getIban());
					level = this.customisationService.getLevelByOwner(owner);
					
					result.addObject("level", level);
				} else {
					iban = "";
				}
				
				result.addObject("iban", iban);
			}
			
			actor = (actorId == 0) ? principal : this.actorService.findOne(actorId);
			role = this.actorService.getRole(actor);
			
//			if (role == Authority.RENTER) {
//				customisation = this.customisationService.find();
//				
//				ecoTruki = customisation.getEcoTruki();
//				discountCodes = customisation.getDiscountCodes();
//				
//				
//			}
			
			result.addObject("actor", actor);
			result.addObject("role", role);
			result.addObject("isMyProfile", isMyProfile);
		} catch (Throwable oops) {
			result = new ModelAndView("actor/display");
			
			log.info("ActorController::display - Error al procesar la petición.");
		}
		
		log.info("Actor enviado desde el controlador a la vista.");
		
		return result;
	}
	
}
