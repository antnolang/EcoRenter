package com.ispp.EcoRenter.controller.authenticated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.service.PhotoService;

@Controller
@RequestMapping("/photo/authenticated")
public class PhotoAuthenticatedController {

	@Autowired
	private PhotoService photoService;

	public PhotoAuthenticatedController() {
		super();
	}
	
	
	@GetMapping(value = "/display")
	public ModelAndView findOne(@RequestParam int photoId) {
		ModelAndView result;
		Photo photo;
		String imageData;
		
		photo = this.photoService.findOne(photoId);
		imageData = this.photoService.getImageBase64(photo);
		
		result = new ModelAndView("photo/display");
		result.addObject("photo", photo);
		result.addObject("imageData", imageData);
		
		return result;
	}
	
	@GetMapping(value = "/create")
	public ModelAndView create() {
		ModelAndView result;

		result = new ModelAndView("photo/edit");

		return result;
	}
	
	@PostMapping(value = "/edit", params = "save")
	public ModelAndView edit(MultipartFile file) {
		ModelAndView result;
		Photo saved;

		try {
			saved = this.photoService.storeImage(file);

			result = new ModelAndView("redirect:/photo/authenticated/create");
		} catch (Throwable oops) {
			result = this.create();
		}

		return result;
	}
	
}
