package com.ispp.EcoRenter.controller.renter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.controller.SmallholdingController;
import com.ispp.EcoRenter.form.CreditCardForm;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.CreditCardService;
import com.ispp.EcoRenter.service.PhotoService;
import com.ispp.EcoRenter.service.RentOutService;
import com.ispp.EcoRenter.service.RenterService;
import com.ispp.EcoRenter.service.SmallholdingService;

@Controller
@RequestMapping("/renter/smallholding")
public class SmallholdingRenterController {

	@Autowired
	private RentOutService rentoutService;

	@Autowired
	private SmallholdingService smallholdingService;

	@Autowired
	private RenterService renterService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private SmallholdingController smallholdingController;

	@Autowired
	private CreditCardService creditCardService;

	// Constructor

	public SmallholdingRenterController(){
		super();
	}

	// Alquiler

	@PostMapping(value = "/rent", params = "saveRent")
	public ModelAndView checkout(@Valid CreditCardForm creditCardForm, BindingResult binding,
	@RequestParam final int smallholdingId) {
		ModelAndView result;
		RentOut rentOut;
		Smallholding smallholding;

		if(binding.hasErrors()){
			result = this.createEditModelAndView(smallholdingId,creditCardForm);
		} else {
			try {
				smallholding = this.smallholdingService.findOne(smallholdingId);
				rentOut = this.rentoutService.reconstruct(smallholding, creditCardForm, binding);
				if(binding.hasErrors())
					throw new IllegalArgumentException();
				this.rentoutService.save(rentOut);
				result = new ModelAndView("redirect:/renter/smallholding/list");
			} catch(Throwable oops){
				result = this.smallholdingController.display(smallholdingId);
				result.addObject("messageCode", "No se ha podido realizar el alquiler correctamente");
			}
		}

		return result;
		
	}

	// List

	@GetMapping("/list")
	public ModelAndView list(@RequestParam("page") final Optional<Integer> page, @RequestParam("size") final Optional<Integer> size) {
		ModelAndView result;
		Collection<Smallholding> smallholdings;
		Renter principal;
		Map<Integer,List<String>> sh_photo;
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);

		sh_photo = new HashMap<Integer,List<String>>();

		try {
			result = new ModelAndView("smallholding/list");

			principal = this.renterService.findByPrincipal();
			
			smallholdings = this.smallholdingService.findSmallholdingsByActiveRentOut(principal.getId());

			Page<Smallholding> shPage = this.smallholdingService.findPaginated(PageRequest.of(currentPage - 1, pageSize), smallholdings);
			int totalPages = shPage.getTotalPages();

			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				result.addObject("pageNumbers", pageNumbers);
			}

			result.addObject("smallholdingPage", shPage);

			for(Smallholding sh: smallholdings){
				List<Photo> photos = new ArrayList<Photo>(this.photoService.findPhotosBySmallholdingId(sh.getId()));
				Photo photo = photos.get(0);
				List<String> photoAttr = new ArrayList<String>();
				photoAttr.add(photo.getName());
				photoAttr.add(photo.getSuffix());
				photoAttr.add(this.photoService.getImageBase64(photo));
				sh_photo.put(sh.getId(), photoAttr);
			}

			result.addObject("sh_photo", sh_photo);
			result.addObject("requestURI", "renter/smallholding/list");
		} catch (Exception e) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

	// Filtro
	@PostMapping(value = "/filter", params = "filtra")
	public ModelAndView filtra(@RequestParam("keyword") String keyword){
		return this.smallholdingController.list(Optional.empty(), Optional.empty(), keyword);
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(int smallholdingId, CreditCardForm creditCardForm) {
		return this.createEditModelAndView(smallholdingId,creditCardForm, null);
	}
	
	protected ModelAndView createEditModelAndView(int smallholdingId, CreditCardForm creditCardForm, 
												  String messageCode) {
		Collection<String> creditCardMakes;
		ModelAndView result;
		
		creditCardMakes = this.creditCardService.getCreditCardMakes();
				
		result = this.smallholdingController.display(smallholdingId);
		result.addObject("creditCardForm", creditCardForm);
		result.addObject("creditCardMakes", creditCardMakes);
		result.addObject("messageCode", messageCode);
		
		return result;
	}

}
