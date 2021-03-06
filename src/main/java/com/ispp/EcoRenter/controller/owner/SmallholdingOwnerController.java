
package com.ispp.EcoRenter.controller.owner;

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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.CustomisationService;
import com.ispp.EcoRenter.service.OwnerService;
import com.ispp.EcoRenter.service.PhotoService;
import com.ispp.EcoRenter.service.SmallholdingService;

@Controller
@RequestMapping("/owner/smallholding")
public class SmallholdingOwnerController {

	// Services

	@Autowired
	private SmallholdingService	smallholdingService;

	@Autowired
	private OwnerService		ownerService;

	@Autowired
	private PhotoService photoService;

	@Autowired
    private CustomisationService customisationService;


	// Constructor

	public SmallholdingOwnerController() {
		super();
	}

	// List

	@GetMapping("/listOwnSmallholdings")
	public ModelAndView list(@RequestParam("page") final Optional<Integer> page, @RequestParam("size") final Optional<Integer> size) {
		ModelAndView result;
		Collection<Smallholding> smallholdings;
		Owner principal;
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);
		Map<Integer,List<String>> sh_photo;
		List<Smallholding> ls_smallholdings;
		List<String> geoData;
		int goldLevel;
		int silverLevel;

		sh_photo = new HashMap<Integer,List<String>>();

		try {
			result = new ModelAndView("smallholding/list");

			principal = this.ownerService.findByPrincipal();
			smallholdings = this.smallholdingService.findSmallholdingsByOwnerId(principal.getId());
			Page<Smallholding> shPage = this.smallholdingService.findPaginated(PageRequest.of(currentPage - 1, pageSize), smallholdings);
			int totalPages = shPage.getTotalPages();

			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				result.addObject("pageNumbers", pageNumbers);
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

			// Para crear los marcadores en el mapa necesito las coordenadas
			ls_smallholdings = shPage.getContent();

			geoData = this.smallholdingService.getGeoData(ls_smallholdings);
			
			if (!geoData.isEmpty()) {
				result.addObject("latitudes", geoData.get(0));
				result.addObject("longitudes", geoData.get(1));
				result.addObject("titles", geoData.get(2));
			}

			goldLevel = this.customisationService.find().getGoldLevel();
			silverLevel = this.customisationService.find().getSilverLevel();

			result.addObject("goldLevel", goldLevel);
			result.addObject("silverLevel", silverLevel);
			
			result.addObject("smallholdingPage", shPage);
			result.addObject("sh_photo", sh_photo);
			result.addObject("requestURI", "owner/smallholding/listOwnSmallholdings");
		} catch (Exception e) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

	// Create

	@GetMapping("/create")
	public ModelAndView create() {
		ModelAndView result;
		Smallholding smallholding;

		smallholding = this.smallholdingService.create();

		result = this.createEditModelAndView(smallholding);

		return result;
	}

	// Edit

	@GetMapping("/edit")
	public ModelAndView edit(@RequestParam final int smallholdingId) {
		ModelAndView result;
		Smallholding smallholding;
		Collection<Photo> photos;

		try {
			smallholding = this.smallholdingService.findOneToEdit(smallholdingId);
			photos = this.photoService.findPhotosBySmallholdingId(smallholdingId);
			smallholding.setPhotos(photos);

			result = this.createEditModelAndView(smallholding);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

	// Save

	@PostMapping(value = "/edit", params = "save")
	public ModelAndView save(List<MultipartFile> file, Smallholding smallholding, final BindingResult binding) {
		ModelAndView result;
		Smallholding smallholdingRec;
		
		smallholdingRec = this.smallholdingService.reconstruct(smallholding, binding);
		
		if (binding.hasErrors() && binding.getErrorCount() != 1) {
			result = this.createEditModelAndView(smallholding);
		} else {
			try {
				if(binding.hasErrors() && binding.getFieldErrorCount("photos") == 1){
					this.smallholdingService.save(smallholdingRec,file);
					result = new ModelAndView("redirect:/owner/smallholding/listOwnSmallholdings");
				} 
				else 
					throw new IllegalArgumentException();
				
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("El propietario debe tener un IBAN asociado")) {
					result = this.createEditModelAndView(smallholdingRec, "Debes tener un IBAN asociado a tu perfil");
				} 
				else if(oops.getMessage().equals("No es una imagen")){
					result = this.createEditModelAndView(smallholdingRec, oops.getMessage());
				} else {
					result = this.createEditModelAndView(smallholdingRec, "No se pudo realizar la operación");
				}
			}
		}

		return result;
	}

	// Deactivate

	@GetMapping("/deactivate")
	public ModelAndView deactivate(@RequestParam final int smallholdingId) {
		ModelAndView result;
		Smallholding sh;

		sh = this.smallholdingService.findOne(smallholdingId);

		try {
			this.smallholdingService.deactivate(sh);

			result = new ModelAndView("redirect:/smallholding/display?smallholdingId=" + smallholdingId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

	// Activate

	@GetMapping("/activate")
	public ModelAndView activate(@RequestParam final int smallholdingId) {
		ModelAndView result;
		Smallholding sh;

		sh = this.smallholdingService.findOne(smallholdingId);

		try {
			this.smallholdingService.activate(sh);

			result = new ModelAndView("redirect:/smallholding/display?smallholdingId=" + smallholdingId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Smallholding smallholding) {
		ModelAndView result;

		result = this.createEditModelAndView(smallholding, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Smallholding smallholding, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("smallholding/edit");
		result.addObject("smallholding", smallholding);
		result.addObject("messageCode", messageCode);

		return result;
	}


}
