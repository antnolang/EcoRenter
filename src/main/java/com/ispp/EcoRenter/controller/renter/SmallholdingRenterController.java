package com.ispp.EcoRenter.controller.renter;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
	

    @PostMapping(value = "/rent", params = "saveRent")
	public ModelAndView checkout(@RequestParam final int smallholdingId,@PathParam("email") String email,@PathParam("name") String name,@PathParam("iban") String iban) {
		
    	
    	ModelAndView result;
    	
    	result = new ModelAndView("redirect:/smallholding/display?smallholdingId=" + smallholdingId);	
		//Stripe
    	result.addObject("amount", 50 * 100); // in cents
    	result.addObject("stripePublicKey", stripePublicKey);
    	result.addObject("currency", ChargeRequest.Currency.EUR);
		
		
		//Logic
		
		Smallholding sh = this.smallholdingService.findOne(smallholdingId);
		
		sh.setStatus("ALQUILADA");
		sh.setAvailable(false);
		
		this.smallholdingService.rent(sh);
		
		RentOut rent = this.rentoutService.create();
		//Comprueba que no esté alquilado etc, seteale el IBAN
		rent.setSmallholding(sh);
		
		this.rentoutService.save(rent);
		
		result.addObject("messagePay", "El pago se ha realizado con éxito.");
		
		return result;
	}


}
