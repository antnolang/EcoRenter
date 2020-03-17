package com.ispp.EcoRenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.helper.Utils;

@Controller
@RequestMapping("/")
public class HomeController{

	@Autowired
	private Utils utils;

	@GetMapping("home")
	public ModelAndView auth() {
	
		Object principal = this.utils.findByPrincipal();
		
		ModelAndView mav = new ModelAndView("miscellaneous/home");
		mav.addObject("principal", principal);
		
		return mav;

	}
	
	
	@GetMapping("login")
	public ModelAndView login() {
		
		Object principal = this.utils.findByPrincipal();
		
		ModelAndView mav = new ModelAndView("login");
	
		mav.addObject("principal", principal);
		
		
		return mav;
		
		
	}
	
	
	@GetMapping("logout")
	public ModelAndView logout() {
		
		Object principal = this.utils.findByPrincipal();
		
		ModelAndView mav = new ModelAndView("sign");
		
		
		mav.addObject("principal", principal);
		
		
		return mav;
	}
	
	
	

}

