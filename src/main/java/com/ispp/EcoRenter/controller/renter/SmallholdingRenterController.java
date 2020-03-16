package com.ispp.EcoRenter.controller.renter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ispp.EcoRenter.helper.ChargeRequest;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.RentOutService;
import com.ispp.EcoRenter.service.SmallholdingService;

@Controller
@RequestMapping("/renter/smallholding")
public class SmallholdingRenterController {
	
	@Autowired
	private RentOutService rentoutService;
	
	
	@Autowired
	private SmallholdingService smallholdingService;
	
	@Value("pk_test_76RlPdxYR4nMYOpmKbYA8xE9000VDDeCpk")
	private String stripePublicKey;
	
	@PostMapping("/rent")
	public String checkout(Model model,@RequestParam final int smallholdingId) {
		
		//Stripe
		model.addAttribute("amount", 50 * 100); // in cents
		model.addAttribute("stripePublicKey", stripePublicKey);
		model.addAttribute("currency", ChargeRequest.Currency.EUR);
		
		
		//Logic
		
		Smallholding sh = this.smallholdingService.findOne(smallholdingId);
		
		sh.setStatus("ALQUILADA");
		sh.setAvailable(false);
		
		this.smallholdingService.rent(sh);
		
		RentOut rent = this.rentoutService.create();
		
		rent.setSmallholding(sh);
		
		this.rentoutService.save(rent);
		
		model.addAttribute("payment succesfull", "El pago se ha realizado con éxito.");
		
		return "/smallholding/display?smallholdingId=" + smallholdingId;
	}


}
