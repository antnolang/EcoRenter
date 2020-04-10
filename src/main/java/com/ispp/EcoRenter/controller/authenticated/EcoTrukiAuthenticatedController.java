package com.ispp.EcoRenter.controller.authenticated;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.EcoTruki;
import com.ispp.EcoRenter.service.EcoTrukiService;
import com.ispp.EcoRenter.service.PhotoService;
import com.ispp.EcoRenter.service.UtilityService;

@Controller
@RequestMapping("/eco-truki/authenticated")
public class EcoTrukiAuthenticatedController {

	@Autowired
	private EcoTrukiService ecoTrukiService;
	
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private UtilityService utilityService;
	
	public EcoTrukiAuthenticatedController() {
		super();
	}
	
	@GetMapping("/display")
	public ModelAndView display(@RequestParam int ecoTrukiId) {
		Map<Integer,String> mapa;
		ModelAndView result;
		EcoTruki ecoTruki;
		
		ecoTruki = this.ecoTrukiService.findOne(ecoTrukiId);
		
		mapa = this.photoService.getEncodedDataByPhoto(ecoTruki.getPhotos());
		
		result = new ModelAndView("ecoTruki/display");
		result.addObject("ecoTruki", ecoTruki);
		result.addObject("mapa", mapa);
		
		return result;
	}
	
	@GetMapping("/list")
	public ModelAndView list(Optional<Integer> page) {
		Map<Integer, List<String>> mapa;
		ModelAndView result;
		Page<EcoTruki> ecoTrukis;
		int val_page, totalPages;
		boolean hasAccess;
		List<Integer> pages;
		
		val_page = this.utilityService.getValidSelectedPage(page);
		
		ecoTrukis = this.ecoTrukiService.findAll(val_page);
		
		totalPages = ecoTrukis.getTotalPages();
		pages = this.utilityService.getPages(totalPages);
		
		mapa = this.ecoTrukiService.getPhotosByEcoTruki(ecoTrukis.getContent());
		
		hasAccess = this.utilityService.hasAccessToExclusiveContent();
		
		result = new ModelAndView("ecoTruki/list");
		result.addObject("ecoTrukis", ecoTrukis);
		result.addObject("hasAccess", hasAccess);
		result.addObject("pages", pages);
		result.addObject("mapa", mapa);
		result.addObject("tam", ecoTrukis.getTotalElements());
		result.addObject("selectedPage", val_page);
		
		return result;
	}
	
}
