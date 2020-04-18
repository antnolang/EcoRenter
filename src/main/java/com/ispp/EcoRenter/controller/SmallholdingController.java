package com.ispp.EcoRenter.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.form.CreditCardForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.model.CreditCard;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.model.Valuation;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.CommentService;
import com.ispp.EcoRenter.service.CreditCardService;
import com.ispp.EcoRenter.service.PhotoService;
import com.ispp.EcoRenter.service.RenterService;
import com.ispp.EcoRenter.service.SmallholdingService;
import com.ispp.EcoRenter.service.ValuationService;

@Controller
@RequestMapping("/smallholding")
public class SmallholdingController {

	// Services

	@Autowired
	private SmallholdingService smallholdingService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ActorService actorService;

	@Autowired
	private PhotoService photoService;

	@Autowired
	private CreditCardService creditCardService;

	@Autowired
	private RenterService renterService;

	@Autowired
	private ValuationService valuationService;

	// Constructor

	public SmallholdingController() {
		super();
	}

	// List

	@GetMapping("/list")
	public ModelAndView list(@RequestParam("page") final Optional<Integer> page,
			@RequestParam("size") final Optional<Integer> size, @RequestParam(required = false, defaultValue = "") String keyword) {
		ModelAndView result;
		Collection<Smallholding> smallholdings;
		int currentPage;
		int pageSize;
		List<Smallholding> ls_smallholdings;
		List<String> geoData;
		Map<Integer,List<String>> sh_photo;

		sh_photo = new HashMap<Integer,List<String>>();

		currentPage = page.orElse(1);
		pageSize = size.orElse(4);
			
		try {
			result = new ModelAndView("smallholding/list");

			if(keyword.isEmpty())
				smallholdings = this.smallholdingService.findSmallholdingsAvailables();
			else
				smallholdings = this.smallholdingService.findSmallholdingsByKeyword(keyword);
			Page<Smallholding> shPage = this.smallholdingService
					.findPaginated(PageRequest.of(currentPage - 1, pageSize), smallholdings);
			int totalPages = shPage.getTotalPages();

			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				result.addObject("pageNumbers", pageNumbers);
			}

			// Para crear los marcadores en el mapa necesito las coordenadas
			ls_smallholdings = shPage.getContent();
	
			geoData = this.smallholdingService.getGeoData(ls_smallholdings);
			
			if (!geoData.isEmpty()) {
				result.addObject("latitudes", geoData.get(0));
				result.addObject("longitudes", geoData.get(1));
				result.addObject("titles", geoData.get(2));
			}

			for(Smallholding sh: smallholdings){
				List<Photo> photos = new ArrayList<Photo>(this.photoService.findPhotosBySmallholdingId(sh.getId()));
				Photo photo = photos.get(0);
				List<String> photoAttr = new ArrayList<String>();
				photoAttr.add(photo.getName());
				photoAttr.add(photo.getSuffix());
				photoAttr.add(this.photoService.getImageBase64(photo));
				sh_photo.put(sh.getId(), photoAttr);
			}
			
			result.addObject("smallholdingPage", shPage);
			result.addObject("sh_photo", sh_photo);
			result.addObject("requestURI", "smallholding/list");
		} catch (Exception e) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

	@GetMapping("/display")
	public ModelAndView display(@RequestParam final int smallholdingId) {
		ModelAndView result;
		Smallholding smallholding;
		Collection<Comment> comments;
		Actor principal;
		Boolean isRentedByRenter;
		Map<Photo,String> photo_imageData;
		Collection<Photo> photos;
		CreditCardForm creditCardForm;
		boolean gotCredit = false;
		CreditCard creditCard;
		Comment comment;
		Long avgMark;
		Collection<Valuation> valuations;

		photo_imageData = new HashMap<Photo,String>();
		principal = null;
		isRentedByRenter = null;
		result = new ModelAndView("smallholding/display");
		try {
			principal = this.actorService.findByPrincipal();
			
			if (principal instanceof Renter) {
				isRentedByRenter = this.smallholdingService.isSmallholdingRentedByRenter(principal.getId(),	smallholdingId);
				if(!isRentedByRenter){
					creditCardForm = new CreditCardForm();
					creditCardForm.setRenter(this.renterService.findByPrincipal());
					
					if(!((Renter) principal).getCreditCards().isEmpty()) {
						creditCard = ((Renter) principal).getCreditCards().iterator().next();
						gotCredit = true;

						result.addObject("creditCard", creditCard);

					}

					result.addObject("gotCredit", gotCredit);
					result.addObject("creditCardForm", creditCardForm);
					result.addObject("creditCardMakes", this.creditCardService.getCreditCardMakes());
				} else {
					comment = this.commentService.create(smallholdingId);

					result.addObject("comment", comment);

				}
			} 
		} catch (Exception e) {
			isRentedByRenter = false;
		}

		try {
			smallholding = this.smallholdingService.findOneToDisplay(smallholdingId);
			comments = this.commentService.findCommentsBySmallholdingId(smallholdingId);
			photos = this.photoService.findPhotosBySmallholdingId(smallholding.getId());

			for(Photo p: photos)
				photo_imageData.put(p, this.photoService.getImageBase64(p));

			result.addObject("smallholding", smallholding);
			result.addObject("comments", comments);
			result.addObject("isRentedByRenter", isRentedByRenter);
			result.addObject("photo_imageData", photo_imageData);
			if(principal != null)
				result.addObject("principalId", principal.getId());
			
			valuations = this.valuationService.findValuationsBySmallholding(smallholdingId);
			if(valuations.isEmpty())
				result.addObject("ratingVacio", "Parcela sin valoraciones");
			else {
				avgMark = this.valuationService.avgMarkSmallholding(smallholdingId);
				result.addObject("avgMark", avgMark);
			}
			
			if(principal != null && principal instanceof Owner && smallholding.getOwner().equals(principal)){
				comment = this.commentService.create(smallholdingId);

				result.addObject("comment", comment);
				result.addObject("ownerPrincipal", true);
			} else {
				result.addObject("ownerPrincipal", false);
			}
		} catch (Exception e) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

}