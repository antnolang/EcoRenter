package com.ispp.EcoRenter.controller;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.AdministratorService;
import com.ispp.EcoRenter.service.CustomisationService;
import com.ispp.EcoRenter.service.MailService;
import com.ispp.EcoRenter.service.RentOutService;
import com.ispp.EcoRenter.service.SmallholdingService;

@Controller
@RequestMapping("/dispute")
public class DisputeController {

	@Autowired
	private ActorService actorService;

	@Autowired
	private MailService mailService;

	

	@Autowired
	private RentOutService rentoutService;

	@Autowired
	private SmallholdingService smallholdingService;
	
	@Autowired
	private CustomisationService customService;


	@PostMapping(value = "/make", params="makeDispute")
	public ModelAndView make(@RequestParam final int smallholdingId, @PathParam("tipo") String tipo, @PathParam("descripcion") String descripcion) {
		ModelAndView result;

		Actor principal = this.actorService.findByPrincipal();
		Customisation custom = this.customService.find();
		
		String admin = custom.getEmail();
		Smallholding smallholding = this.smallholdingService.findOne(smallholdingId);
		
		
		if(principal instanceof Owner) {


			RentOut rentOut = this.rentoutService.findByOwnerAndSmallholding(principal.getId(),smallholdingId);

			try {
				
				this.mailService.sendEmailDispute(principal, rentOut.getRenter(), admin, tipo, descripcion,smallholdingId);
				result = new ModelAndView("redirect:/smallholding/display?smallholdingId="+smallholdingId);
				smallholding.setArgumented(true);
				
				this.smallholdingService.saveDispute(smallholding);
				
				result.addObject("isArgumented", smallholding.isArgumented());
			
				
			}catch(Throwable oops) {
				
				result = new ModelAndView("redirect:/smallholding/display?smallholdingId="+smallholdingId);
				result.addObject("errorDispute", "No se pudo abrir la disputa, intentelo de nuevo.");
				result.addObject("isArgumented", smallholding.isArgumented());
			}


		}else {

			

			try {
				this.mailService.sendEmailDispute(principal, smallholding.getOwner(), admin, tipo, descripcion,smallholdingId);
				result = new ModelAndView("redirect:/smallholding/display?smallholdingId="+smallholdingId);
				smallholding.setArgumented(true);
				this.smallholdingService.saveDispute(smallholding);
				
				
			}catch(Throwable oops) {
				result = new ModelAndView("redirect:/smallholding/display?smallholdingId="+smallholdingId);
				
			}

		}
		return result;

	}
}
