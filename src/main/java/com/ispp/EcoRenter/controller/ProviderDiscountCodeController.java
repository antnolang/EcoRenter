package com.ispp.EcoRenter.controller;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.ProviderDiscountCode;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.ProviderDiscountCodeService;
import com.ispp.EcoRenter.service.SmallholdingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/providerDiscountCode")
public class ProviderDiscountCodeController {

    // Service

    @Autowired
	private ProviderDiscountCodeService providerDiscountCodeService;
	
	@Autowired
	private ActorService actorService;

	@Autowired
	private SmallholdingService smallholdingService;

    // Constructor

    public ProviderDiscountCodeController(){
        super();
    }

    // Display

    @GetMapping("/display")
    public ModelAndView display(@RequestParam int providerDiscountCodeId){
        ModelAndView result;
        ProviderDiscountCode providerDiscountCode;

        try {
            providerDiscountCode = this.providerDiscountCodeService.findOneToDisplay(providerDiscountCodeId);
            result = new ModelAndView("providerDiscountCode/display");
            result.addObject("providerDiscountCode", providerDiscountCode);
        } catch(Throwable oops){
            result = new ModelAndView("redirect:/miscellaneous/error");
        }
        
        return result;
    }

    // List

	@GetMapping("/list")
	public ModelAndView list(@RequestParam("page") final Optional<Integer> page,@RequestParam("size") final Optional<Integer> size) {
		ModelAndView result;
		Collection<ProviderDiscountCode> discountCodes;
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);
		Boolean isAvailable = false;
		Collection<Smallholding> smallholdings;
		Actor actor;

		try {
			result = new ModelAndView("providerDiscountCode/list");

			actor = this.actorService.findByPrincipal();
			switch(actor.getUserAccount().getAuthorities().toString()){
				case "[RENTER]":
					smallholdings = this.smallholdingService.findSmallholdingsByActiveRentOut(actor.getId());
					if(smallholdings.size() > 0)
						isAvailable = true;
					else
						isAvailable = false;
					break;
				case "[OWNER]":
					smallholdings = this.smallholdingService.findSmallholdingsByOwnerId(actor.getId());
					if(smallholdings.size() > 0)
						isAvailable = true;
					else
						isAvailable = false;
					break;
				default:
					isAvailable = true;
			}

			discountCodes = this.providerDiscountCodeService.findAll();
			Page<ProviderDiscountCode> dcPage = this.providerDiscountCodeService
					.findPaginated(PageRequest.of(currentPage - 1, pageSize), discountCodes);
			int totalPages = dcPage.getTotalPages();

			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				result.addObject("pageNumbers", pageNumbers);
			}

			result.addObject("dcPage", dcPage);
			result.addObject("isAvailable", isAvailable);
			result.addObject("requestURI", "providerDiscountCode/list");
		} catch (Exception e) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

}