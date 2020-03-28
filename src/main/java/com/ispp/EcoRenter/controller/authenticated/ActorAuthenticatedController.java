package com.ispp.EcoRenter.controller.authenticated;

import java.util.Collection;

import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.controller.administrator.ActorAdministratorController;
import com.ispp.EcoRenter.controller.owner.ActorOwnerController;
import com.ispp.EcoRenter.controller.renter.ActorRenterController;
import com.ispp.EcoRenter.form.AdminForm;
import com.ispp.EcoRenter.form.OwnerForm;
import com.ispp.EcoRenter.form.RenterForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.AdministratorService;
import com.ispp.EcoRenter.service.CustomisationService;
import com.ispp.EcoRenter.service.OwnerService;
import com.ispp.EcoRenter.service.PhotoService;
import com.ispp.EcoRenter.service.RenterService;
import com.ispp.EcoRenter.service.SmallholdingService;

@Controller
@RequestMapping("/actor/authenticated")
public class ActorAuthenticatedController {

	private static final Log log = LogFactory.getLog(ActorAuthenticatedController.class);
	
	@Autowired
	private ActorService actorService;
	
	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private AdministratorService administratorService;
	
	@Autowired
	private CustomisationService customisationService;
	
	@Autowired
	private RenterService renterService;
	
	@Autowired
	private SmallholdingService smallholdingService;
	
	@Autowired
	private ActorOwnerController actorOwnerController;
	
	@Autowired
	private ActorAdministratorController actorAdministratorController;
	
	@Autowired
	private ActorRenterController actorRenterController;
	
	@Autowired
	private PhotoService photoService;
	
	public ActorAuthenticatedController() {
		super();
	}
	
	@GetMapping("/edit")
	public ModelAndView edit() {
		AdminForm adminForm;
		OwnerForm ownerForm;
		RenterForm renterForm;
		ModelAndView result;
		Actor principal;
		String role;
		
		principal = this.actorService.findByPrincipal();
		role = this.actorService.getRole(principal);
		
		if (role.equals("RENTER")) {
			renterForm = new RenterForm(principal.getName(), principal.getSurname(),
										principal.getEmail(), principal.getTelephoneNumber(),
										principal.getUserAccount().getUsername());
		
			result = this.actorRenterController.createEditModelAndView(renterForm);
		} else if (role.equals("OWNER")) {
			ownerForm = new OwnerForm(principal.getName(), principal.getSurname(),
									  principal.getEmail(), principal.getTelephoneNumber(),
									  principal.getUserAccount().getUsername());
			
			result = this.actorOwnerController.createEditModelAndView(ownerForm);
		} else {
			adminForm = new AdminForm(principal.getName(), principal.getSurname(),
									  principal.getEmail(), principal.getTelephoneNumber(),
									  principal.getUserAccount().getUsername());
			
			result = this.actorAdministratorController.createEditModelAndView(adminForm);
		}
		
		return result;
	}
	
	@PostMapping(value = "/edit", params = "saveOwner")
	public ModelAndView editOwner(@ModelAttribute("objectForm") @Valid OwnerForm ownerForm, BindingResult binding) {
		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = this.actorOwnerController.createEditModelAndView(ownerForm);
		} else {
			try {
				this.ownerService.edit(ownerForm);
				
				result = new ModelAndView("redirect:/actor/authenticated/display");
			} catch (Throwable oops) {
				String message = oops.getMessage();
	
				if(message.equals("Las contraseñas no coinciden.")) {
					result = this.actorOwnerController.createEditModelAndView(ownerForm,
																			  "noMatchPass",
																			  message);
				}else if(message.equals("El usuario elegido ya existe.")) {
					result = this.actorOwnerController.createEditModelAndView(ownerForm,
																			  "noValidUser",
																			  message);
				}else if(message.equals("Iban incorrecto.")) {
					result = this.actorOwnerController.createEditModelAndView(ownerForm,
							 												  "noValidIban",
							 												  message);
				} else if (message.equals("No es una imagen")) {
					result = this.actorOwnerController.createEditModelAndView(ownerForm,
							  												  "selImage",
							  												  message);
				} else {
					result = this.actorOwnerController.createEditModelAndView(ownerForm,
																			  "invalidOperation",
																			  "No se pudo completar la operación");
				}
			}
		}
			
		return result;
	}
	
	@PostMapping(value = "/edit", params = "saveRenter")
	public ModelAndView editRenter(@ModelAttribute("objectForm") @Valid RenterForm renterForm, BindingResult binding) {
		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = this.actorRenterController.createEditModelAndView(renterForm);
		} else {
			try {
				this.renterService.edit(renterForm);
				
				result = new ModelAndView("redirect:/actor/authenticated/display");
			} catch (Throwable oops) {
				String message = oops.getMessage();
	
				if(message.equals("Las contraseñas no coinciden.")) {
					result = this.actorRenterController.createEditModelAndView(renterForm, 
																			   "noMatchPass",
																			   message);
				}else if(message.equals("El usuario elegido ya existe.")) {
					result = this.actorRenterController.createEditModelAndView(renterForm,
																			   "noValidUser",
																			    message);
				}else if(message.equals("Iban incorrecto.")) {
					result = this.actorRenterController.createEditModelAndView(renterForm,
							                               					   "noValidIban",
							                               					    message);
				} else if (message.equals("No es una imagen")) {
						result = this.actorRenterController.createEditModelAndView(renterForm,
								  												  "selImage",
								  												  message);
				} else {
					result = this.actorRenterController.createEditModelAndView(renterForm,
																			   "invalidOperation",
																			   "No se pudo completar la operación");
				}
			}
		}
		
		return result;
	}
	
	@PostMapping(value = "/edit", params = "saveAdmin")
	public ModelAndView editAdmin(@ModelAttribute("objectForm") @Valid AdminForm adminForm, BindingResult binding) {
		ModelAndView result;
		
		if (binding.hasErrors()) {
			result = this.actorAdministratorController.createEditModelAndView(adminForm);
		} else {
			try {
				this.administratorService.edit(adminForm);
				
				result = new ModelAndView("redirect:/actor/authenticated/display");
			} catch (Throwable oops) {
				String message = oops.getMessage();
	
				if(message.equals("Las contraseñas no coinciden.")) {
					result = this.actorAdministratorController.createEditModelAndView(adminForm,
																					  "noMatchPass",
																					   message);
				}else if(message.equals("El usuario elegido ya existe.")) {
					result = this.actorAdministratorController.createEditModelAndView(adminForm,
																					  "noValidUser",
																					  message);
				}else if(message.equals("Iban incorrecto.")) {
					result = this.actorAdministratorController.createEditModelAndView(adminForm,
																					  "noValidIban",
																					  message);
				} else if (message.equals("No es una imagen")) {
					result = this.actorAdministratorController.createEditModelAndView(adminForm,
							  														  "selImage",
							  														  message);	
				} else {
					result = this.actorAdministratorController.createEditModelAndView(adminForm,
																					  "invalidOperation",
																					  "No se pudo completar la operación");
				}
			}
		}
		
		return result;
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
		String iban, role, level, discountCodes;
		Collection<Smallholding> smallholdings;
		Photo photo;
		String imageData;
		
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
			role = this.actorService.getDisplayRole(actor);
			
			photo = actor.getPhoto();
			
			// Mostrar la foto si el actor tiene
			if (photo != null) {
				imageData = this.photoService.getImageBase64(photo);
				
				result.addObject("photo", photo);
				result.addObject("imageData", imageData);
			}
			
			// Req 9.6, Req 9.10, Req 9.11 ------------------------------
			if (isMyProfile && this.actorService.isASpecificRole(actor, "RENTER")) {
				smallholdings = this.smallholdingService.findSmallholdingsByActiveRentOut(actor.getId());
				
				result.addObject("smallholdings", smallholdings);
				
				if (!smallholdings.isEmpty()) {
					customisation = this.customisationService.find();
					
					discountCodes = customisation.getDiscountCodes();
					
					result.addObject("discountCodes", discountCodes);
				}
				
			}
			
			result.addObject("actor", actor);
			result.addObject("role", role);
			result.addObject("isMyProfile", isMyProfile);
		} catch (Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
			
			log.info("ActorController::display - Error al procesar la petición.");
		}
		
		log.info("Actor enviado desde el controlador a la vista.");
		
		return result;
	}
	
}
