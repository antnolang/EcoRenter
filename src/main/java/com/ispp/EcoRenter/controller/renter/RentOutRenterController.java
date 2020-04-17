package com.ispp.EcoRenter.controller.renter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.service.RentOutService;
import com.ispp.EcoRenter.service.RenterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/renter/rentOut")
public class RentOutRenterController {

    // Servicio

    @Autowired
    private RentOutService rentOutService;

    @Autowired
    private RenterService renterService;

    // Constructor

    public RentOutRenterController(){
        super();
    }

    // Display

    @GetMapping("/display")
    public ModelAndView display(@RequestParam int rentOutId){
        ModelAndView result;
        RentOut rentOut;

        try {
            rentOut = this.rentOutService.findOneToDisplay(rentOutId);
            result = new ModelAndView("rentOut/display");
            result.addObject("rentOut", rentOut);
        } catch(Throwable oops){
            result = new ModelAndView("redirect:/miscellaneous/error");
        }
        
        return result;
    }

    // Lista

    @GetMapping("/list")
    public ModelAndView list(@RequestParam("page") final Optional<Integer> page,@RequestParam("size") final Optional<Integer> size){
        ModelAndView result;
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(4);
        Collection<RentOut> rentOuts;

        try {
            rentOuts = this.rentOutService.findRentOutsByRenter(this.renterService.findByPrincipal().getId());

            result = new ModelAndView("rentOut/list");
            
            Page<RentOut> roPage = this.rentOutService
					.findPaginated(PageRequest.of(currentPage - 1, pageSize), rentOuts);
            int totalPages = roPage.getTotalPages();
            
            if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
				result.addObject("pageNumbers", pageNumbers);
            }
            
            result.addObject("roPage", roPage);
        } catch (Exception e) {
			result = new ModelAndView("redirect:/miscellaneous/error");
        }
        
        return result;

    }

}