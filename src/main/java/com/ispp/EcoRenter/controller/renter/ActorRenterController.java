package com.ispp.EcoRenter.controller.renter;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.form.RenterForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.register.RenterRegister;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.RenterService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;


@Controller
@RequestMapping("/actor/renter")
public class ActorRenterController {

	private static final Log log = LogFactory.getLog(ActorRenterController.class);

	@Autowired
	private ActorService actorService;

	@Autowired
	private RenterService renterService;


	public ActorRenterController() {
		super();
	}

	@GetMapping(value = "/display")
	public ModelAndView findOne(@RequestParam(required = false, defaultValue = "0") int actorId) {
		ModelAndView result;
		Actor actor;
		Renter principal;
		boolean isMyProfile;
		String iban;

		principal = this.renterService.findByPrincipal();

		isMyProfile = actorId == principal.getId();

		actor = this.actorService.findOne(principal.getId());

		result = new ModelAndView("actor/display");

		if (isMyProfile) {
			iban = this.actorService.getEncodedIban(principal.getIban());

			result.addObject("iban", iban);

			log.info("Es un arrendatario.");
		}

		result.addObject("actor", actor);

		return result;
	}


	@GetMapping("/register")
	public ModelAndView register() {
		ModelAndView result = new ModelAndView("actor/renterRegister");
		RenterRegister renter = new RenterRegister();

		result.addObject("renter", renter);


		return result;
	}



	@PostMapping(value = "/register", params = "save")
	public ModelAndView registerRenter(@ModelAttribute("renter") @Valid RenterRegister renterRegister, final BindingResult binding) {
		ModelAndView result;


		if (binding.hasErrors()) {
			result = new ModelAndView("actor/renterRegister");
		}else {
			try {

				this.renterService.register(renterRegister, binding);

				result = new ModelAndView("login");

			}catch(Throwable oops) {
				result = new ModelAndView("actor/renterRegister");
				String message = oops.getMessage();

				if(message.equals("Las contraseñas no coinciden.")) {
					result.addObject("noMatchPass", message);
				}else if(message.equals("El usuario elegido ya existe.")) {
					result.addObject("noValidUser", message);

				}else if(message.equals("Iban incorrecto.")) {
					result.addObject("noValidIban", message);
				}else {
					result.addObject("errorMessage", message);
				}

			}
		}

		return result;

	}
	
	@GetMapping("/export-renters")
	public void exportCSV(HttpServletResponse response) throws Exception{
		
		String filename = "export.csv";
		List<Renter> result = new ArrayList<Renter>();
		Renter principal = this.renterService.findByPrincipal();
		
		result.add(principal);
		
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\" ");
		
		StatefulBeanToCsv<Renter> writer = new StatefulBeanToCsvBuilder<Renter>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
				.withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false)
				.build();
		writer.write(principal);
	}

	// Metodos auxiliares ---------------------------------------------------
	public ModelAndView createEditModelAndView(RenterForm renterForm) {
		ModelAndView result;
		
		result = new ModelAndView("actor/edit");
		result.addObject("objectForm", renterForm);
		result.addObject("buttonName", "saveRenter");
		
		return result;
	}
	
	public ModelAndView createEditModelAndView(Object objectForm, String messageName, String messageValue) {
		ModelAndView result;
		
		result = new ModelAndView("actor/edit");
		result.addObject("objectForm", objectForm);
		result.addObject(messageName, messageValue);
		result.addObject("buttonName", "saveRenter");
		
		return result;
	}
	

}
