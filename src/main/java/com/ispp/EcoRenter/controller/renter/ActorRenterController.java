package com.ispp.EcoRenter.controller.renter;

import java.util.Collection;

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

import com.ispp.EcoRenter.export.ActorExport;
import com.ispp.EcoRenter.form.RenterForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.register.RenterRegister;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.CommentService;
import com.ispp.EcoRenter.service.CustomisationService;
import com.ispp.EcoRenter.service.RentOutService;
import com.ispp.EcoRenter.service.RenterService;
import com.ispp.EcoRenter.service.SmallholdingService;
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

	@Autowired
	private CustomisationService customisationService;

	@Autowired
	private SmallholdingService smallholdingService;

	@Autowired
	private RentOutService rentoutService;

	@Autowired
	private CommentService commentService;

	public ActorRenterController() {
		super();
	}

	@GetMapping(value = "/display")
	public ModelAndView findOne(@RequestParam(required = false, defaultValue = "0") int actorId) {
		ModelAndView result;
		Actor actor;
		Renter principal;
		boolean isMyProfile;

		principal = this.renterService.findByPrincipal();

		isMyProfile = actorId == principal.getId();

		actor = this.actorService.findOne(principal.getId());

		result = new ModelAndView("actor/display");
		result.addObject("actor", actor);
		result.addObject("isMyProfile", isMyProfile);

		return result;
	}

	@GetMapping("/register")
	public ModelAndView register() {
		ModelAndView result = new ModelAndView("actor/renterRegister");
		RenterRegister renter = new RenterRegister();
		Customisation custo = this.customisationService.find();

		result.addObject("renter", renter);
		result.addObject("maxSizePhoto", custo.getMaxSizePhoto());

		return result;
	}

	@PostMapping(value = "/register", params = "save")
	public ModelAndView registerRenter(
			@ModelAttribute("renter") @Valid RenterRegister renterRegister,
			final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors()) {
			result = new ModelAndView("actor/renterRegister");
		} else {
			try {
				this.renterService.register(renterRegister, binding);

				result = new ModelAndView("login");

			}catch(Throwable oops) {

				result = new ModelAndView("actor/renterRegister");
				result.addObject("maxSizePhoto",
						this.customisationService.find().getMaxSizePhoto());

				String message = oops.getMessage();

				if (message.equals("Las contraseñas no coinciden.")) {
					result.addObject("noMatchPass", message);
				} else if (message.equals("El usuario elegido ya existe.")) {
					result.addObject("noValidUser", message);

				} else if (message.equals("Iban incorrecto.")) {
					result.addObject("noValidIban", message);
				} else if (message.equals("No es una imagen")) {
					result.addObject("selImage", message);
				} else {
					result.addObject("errorMessage", message);
				}

			}
		}

		return result;

	}

	@GetMapping("/export-renterData")
	public void exportCSV(HttpServletResponse response) throws Exception{

		String filename = "myData.csv";

		Renter principal = this.renterService.findByPrincipal();

		Collection<Smallholding> smalls = this.smallholdingService.findSmallholdingsByRenterId(principal.getId());
		Collection<RentOut> rents = this.rentoutService.findRentOutsByRenter(principal.getId());
		Collection<Comment> comments = this.commentService.findCommentsByActor(principal.getId());

		String smallString = "";
		int countSmall = 0;
		int smallSize = smalls.size();

		String rentString = "";
		int countRents = 0;
		int rentSize = rents.size();

		String commentString = "";
		int countComments= 0;
		int commentSize = comments.size();


		for(Smallholding s: smalls) {
			countSmall++;


			if(countSmall == smallSize) {
				smallString = smallString + s.getTitle();
			}else {
				smallString = smallString + s.getTitle()+",";
			}
		}

		for(RentOut r : rents) {
			countRents++;


			if(countRents == rentSize) {
				rentString = rentString + r.getStartDate().toString();
			}else {
				rentString = rentString + r.getStartDate().toString()+",";
			}
		}

		for(Comment c : comments) {
			countComments++;

			if(countComments == commentSize) {
				commentString = commentString + c.getText();
			}else {
				commentString = commentString + c.getText()+",";
			}

		}


		ActorExport toExport = new ActorExport(principal.getName(),principal.getSurname(), principal.getTelephoneNumber(), principal.getUserAccount().getUsername(), principal.getEmail(),smallString,rentString,commentString);


		response.setContentType("text/csv");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\" ");

		StatefulBeanToCsv<ActorExport> writer = new StatefulBeanToCsvBuilder<ActorExport>(response.getWriter())
				.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
				.withSeparator(CSVWriter.DEFAULT_SEPARATOR)
				.withOrderedResults(false)
				.build();
		writer.write(toExport);
	}

	// Metodos auxiliares ---------------------------------------------------
	public ModelAndView createEditModelAndView(RenterForm renterForm) {
		ModelAndView result;
		Customisation custo;

		custo = this.customisationService.find();

		result = new ModelAndView("actor/edit");
		result.addObject("objectForm", renterForm);
		result.addObject("buttonName", "saveRenter");
		result.addObject("maxSizePhoto", custo.getMaxSizePhoto());

		return result;	
	}

	public ModelAndView createEditModelAndView(RenterForm renterForm,
			String messageName,
			String messageValue) {
		ModelAndView result;

		result = this.createEditModelAndView(renterForm);
		result.addObject(messageName, messageValue);

		return result;
	}

}
