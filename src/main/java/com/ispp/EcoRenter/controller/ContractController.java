package com.ispp.EcoRenter.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/miscellaneous")
public class ContractController {
	
	
	@RequestMapping("/condiciones-arrendamiento")
	public String contract() {

		return "miscellaneous/condiciones-arrendamiento";
	}
	
	@RequestMapping("/terminos-y-condiciones")
	public String terminos() {

		return "miscellaneous/terminos-y-condiciones";
	}
	
	

}
