package com.ispp.EcoRenter.controller.owner;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

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

import com.ispp.EcoRenter.export.ActorExport;
import com.ispp.EcoRenter.form.OwnerForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.register.OwnerRegister;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.CustomisationService;
import com.ispp.EcoRenter.service.OwnerService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;

@Controller
@RequestMapping("/actor/owner")
public class ActorOwnerController {

	@Autowired
	private ActorService actorService;

	@Autowired
	private OwnerService ownerService;
	
	@Autowired
	private CustomisationService customisationService;
	

	public ActorOwnerController() {
		super();
	}

	@GetMapping(value = "/display")
	public ModelAndView findOne(@RequestParam(required = false, defaultValue = "0") int actorId) {
		ModelAndView result;
		Actor actor;
		Owner principal;
		boolean isMyProfile;
		String iban;

		principal = this.ownerService.findByPrincipal();

		isMyProfile = actorId == principal.getId();

		actor = this.actorService.findOne(principal.getId());

		result = new ModelAndView("actor/display");

		if (isMyProfile) {
			iban = this.actorService.getEncodedIban(principal.getIban());

			result.addObject("iban", iban);
		}

		result.addObject("actor", actor);

		return result;
	}

	@GetMapping("/register")
	public ModelAndView register() {
		ModelAndView result = new ModelAndView("actor/ownerRegister");
		OwnerRegister owner = new OwnerRegister();
		Customisation custo = this.customisationService.find();
		
		result.addObject("owner", owner);
		result.addObject("maxSizePhoto", custo.getMaxSizePhoto());

		return result;
	}

	@PostMapping(value = "/register", params = "save")
	public ModelAndView registerOwner(@ModelAttribute("owner") @Valid OwnerRegister ownerRegister,
			final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("actor/ownerRegister");
		} else {
			try {
				this.ownerService.register(ownerRegister, binding);

				result = new ModelAndView("login");

			}catch(Throwable oops) {
				result = new ModelAndView("actor/ownerRegister");
				result.addObject("maxSizePhoto", 
						         this.customisationService.find().getMaxSizePhoto());
				
				String message = oops.getMessage();
				
				if(message.equals("Las contraseñas no coinciden.")) {
					result.addObject("noMatchPass", message);
					
				} else if (message.equals("El usuario elegido ya existe.")) {
					result.addObject("noValidUser", message);

				} else if (message.equals("No es una imagen")) {
					result.addObject("selImage", message);
				}   else {
					result.addObject("errorMessage",
							"No se pudo realizar el registro. Intentelo de nuevo por favor.");
				}

			}
		}

		return result;
	}
	
	
	@GetMapping("/export-ownerData")
	public void exportCSV(HttpServletResponse response) throws Exception{
		
		String filename = "myData.csv";
		
		Owner principal = this.ownerService.findByPrincipal();
		
		ActorExport toExport = new ActorExport(principal.getName(),
											   principal.getSurname(),
											   principal.getTelephoneNumber(),
											   principal.getUserAccount().getUsername(),
											   principal.getEmail());
		
		
		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename +
				"\" ");
		
		StatefulBeanToCsv<ActorExport> writer = new StatefulBeanToCsvBuilder<ActorExport>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
				.withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false)
				.build();
		writer.write(toExport);
	}

	public ModelAndView createEditModelAndView(OwnerForm ownerForm) {
		ModelAndView result;
		Customisation custo;
		
		custo = this.customisationService.find();
		
		result = new ModelAndView("actor/edit");
		result.addObject("objectForm", ownerForm);
		result.addObject("buttonName", "saveOwner");
		result.addObject("maxSizePhoto", custo.getMaxSizePhoto());
		
		return result;
	}

	public ModelAndView createEditModelAndView(OwnerForm ownerForm, String messageName,
			String messageValue) {
		ModelAndView result;
		
		result = this.createEditModelAndView(ownerForm);
		result.addObject(messageName, messageValue);
	
		return result;
	}

}
