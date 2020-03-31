package com.ispp.EcoRenter.controller.renter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.controller.SmallholdingController;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.PhotoService;
import com.ispp.EcoRenter.service.RentOutService;
import com.ispp.EcoRenter.service.RenterService;
import com.ispp.EcoRenter.service.SmallholdingService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

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

	@PostMapping(value = "/rent", params = "saveRent")
	public ModelAndView checkout(@RequestParam final int smallholdingId, @PathParam("card") String card,
	@PathParam("fecha") String fecha, @PathParam("cvv") String cvv) throws StripeException {

		ModelAndView result;

		result = new ModelAndView("redirect:/renter/smallholding/list");

		Smallholding sh = this.smallholdingService.findOne(smallholdingId);

		Stripe.apiKey = "sk_test_DxeIjPSmKslD2tFg1b1CG2TU00Q4RigZkT";
		
		// Logic

		try {
			this.rentoutService.checkChard(card, fecha, cvv);

			Long amount = (new Double(sh.getPrice())).longValue();

			PaymentIntentCreateParams paymentParams = PaymentIntentCreateParams.builder()
				.setCurrency("eur")
				.setAmount(amount*100)
				.setReceiptEmail(this.renterService.findByPrincipal().getEmail())
				.putMetadata("integration_check", "accept_a_payment")
				.build();

			PaymentIntent intent = PaymentIntent.create(paymentParams);

			PaymentIntent paymentIntent = PaymentIntent.retrieve(intent.getId());

			Map<String, Object> params = new HashMap<>();
			params.put("payment_method", "pm_card_visa");

			paymentIntent.confirm(params);
			
			this.smallholdingService.rent(sh);

			RentOut rent = this.rentoutService.create();

			rent.setSmallholding(sh);
			
			rent.setIsActive(true);
			
			this.rentoutService.save(rent);

		} catch (Exception ex) {
			//result = new ModelAndView("redirect:/smallholding/display?smallholdingId=" + smallholdingId);
			result = this.smallholdingController.display(smallholdingId);
			result.addObject("errorPay", "No se ha podido realizar el pago correctamente");
		}

		
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
		Map<Integer,List<String>> sh_photo;

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

}
