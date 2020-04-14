package com.ispp.EcoRenter.controller;

import java.util.Collection;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Administrator;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.AdministratorService;
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
	private AdministratorService administratorService;

	@Autowired
	private RentOutService rentoutService;
	
	@Autowired
	private SmallholdingService smallholdingService;


	@PostMapping(value = "/make", params="makeDispute")
	public ModelAndView make(@RequestParam final int smallholdingId, @PathParam("tipo") String tipo, @PathParam("descripcion") String descripcion) {
		ModelAndView result = new ModelAndView("redirect:/");

		Actor principal = this.actorService.findByPrincipal();
		Collection<Administrator> administrators = this.administratorService.findAll();
		Administrator admin = administrators.iterator().next();

		if(principal instanceof Owner) {


			RentOut rentOut = this.rentoutService.findByOwnerAndSmallholding(principal.getId(),smallholdingId);



			this.mailService.sendEmailDispute(principal, rentOut.getRenter(), admin, tipo, descripcion);

		}else {
			
			Smallholding smallholding = this.smallholdingService.findOne(smallholdingId);
			

			this.mailService.sendEmailDispute(principal, smallholding.getOwner(), admin, tipo, descripcion);

		}


		return result;

	}

}
