package com.ispp.EcoRenter.controller.renter;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
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

	@PostMapping(value = "/rent", params = "saveRent")
	public ModelAndView checkout(@RequestParam final int smallholdingId, @PathParam("email") String email,
			@PathParam("name") String name, @PathParam("iban") String iban) {

		ModelAndView result;

		result = new ModelAndView("redirect:/smallholding/list");

		// Logic

		Smallholding sh = this.smallholdingService.findOne(smallholdingId);

		try {

			Assert.notNull(iban, "No puede ser nulo el iban");

			Assert.isTrue(iban.matches("[ES]{2}[0-9]{6}[0-9]{4}[0-9]{4}[0-9]{4}[0-9]{4}"),"Ponga bien el iban por favor.");

			this.smallholdingService.rent(sh);

			RentOut rent = this.rentoutService.create();

			rent.setSmallholding(sh);

			rent.getRenter().setIban(iban);

			this.rentoutService.save(rent);

		} catch (Exception ex) {
			System.out.println(ex.getLocalizedMessage());
			result.addObject(ex.getMessage(), "error");
		}

		result.addObject("isRented", true);

		return result;
	}

	// List

	@GetMapping("/list")
	public ModelAndView list(@RequestParam("page") final Optional<Integer> page, @RequestParam("size") final Optional<Integer> size) {
		ModelAndView result;
		// Collection<Smallholding> actSmallholdingsRented, prevSmallholdingsRented;
		Collection<Smallholding> smallholdings;
		Renter principal;
		int currentPage = page.orElse(1);
		int pageSize = size.orElse(4);

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


			result.addObject("requestURI", "renter/smallholding/list");
		} catch (Exception e) {
			result = new ModelAndView("redirect:/miscellaneous/error");
		}

		return result;
	}

}
