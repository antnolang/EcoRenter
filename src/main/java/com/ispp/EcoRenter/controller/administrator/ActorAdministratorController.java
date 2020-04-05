package com.ispp.EcoRenter.controller.administrator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ispp.EcoRenter.form.AdminForm;
import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.service.ActorService;
import com.ispp.EcoRenter.service.AdministratorService;
import com.ispp.EcoRenter.service.PhotoService;

@Controller
@RequestMapping("/actor/administrator")
public class ActorAdministratorController {

	private static final Log log = LogFactory.getLog(ActorAdministratorController.class);

	@Autowired
	private ActorService actorService;

	@Autowired
	private AdministratorService administratorService;

	@Autowired
	private PhotoService photoService;

	public ActorAdministratorController() {
		super();
	}

	@GetMapping(value = "/ban")
	public ModelAndView ban(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;

		try {
			actor = this.actorService.findOne(actorId);

			this.actorService.ban(actor);

			result = new ModelAndView("redirect:/actor/display?actorId=" + actorId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");

			ActorAdministratorController.log.info("Algo salio mal al banear un actor.");
		}

		return result;
	}

	@GetMapping(value = "/unban")
	public ModelAndView unBan(@RequestParam final int actorId) {
		ModelAndView result;
		Actor actor;

		try {
			actor = this.actorService.findOne(actorId);

			this.actorService.unBan(actor);

			result = new ModelAndView("redirect:/actor/display?actorId=" + actorId);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/miscellaneous/error");

			ActorAdministratorController.log.info("Algo salio mal al banear un actor.");
		}

		return result;
	}

	@GetMapping(value = "/deleteActor")
	public ModelAndView deleteActor(@RequestParam("page") final Optional<Integer> page,
			@RequestParam("size") final Optional<Integer> size) {
		ModelAndView result;
		Collection<Actor> actors;

		int currentPage = page.orElse(1);
		Map<Integer, List<String>> actor_photo;

		actor_photo = new HashMap<Integer, List<String>>();

	


		try {
			result = new ModelAndView("actor/deleteActor");
			actors = this.actorService.findAllExceptAdmin();
			int pageSize = size.orElse(actors.size());

			Page<Actor> shPage = this.actorService
					.findPaginated(PageRequest.of(currentPage - 1, pageSize), actors);


			int totalPages = shPage.getTotalPages();

			if (totalPages > 0) {
				List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
						.boxed()
						.collect(Collectors.toList());

				result.addObject("pageNumbers", pageNumbers);
			}

			for (Actor a : actors) {
				if (a.getPhoto() != null) {
					Photo photo = this.photoService.getPhotoById(a.getPhoto().getId());
					
					List<String> photoAttr = new ArrayList<String>();
					photoAttr.add(photo.getName());
					photoAttr.add(photo.getSuffix());
					photoAttr.add(this.photoService.getImageBase64(photo));
					
					actor_photo.put(a.getId(), photoAttr);
				}

			}

			result.addObject("actors", actors);

			result.addObject("actorPage", shPage);
			result.addObject("actor_photo", actor_photo);

		} catch (Throwable oops) {

			result = new ModelAndView("actor/deleteActor");
		}

		return result;
	}

	@GetMapping(value = "/delete")
	public ModelAndView delete(@RequestParam final int actorId) {
		ModelAndView result = null;

		Actor beDeleted = this.actorService.findOne(actorId);

		if (beDeleted instanceof Renter) {

			try {

				this.administratorService.deleteRenter((Renter) beDeleted);

				result = new ModelAndView("redirect:/");

			} catch (Throwable oops) {
				result = new ModelAndView("actor/administrator/deleteActor");

				result.addObject("error", oops.getMessage());
			}

		} else {
			try {
				this.administratorService.deleteOwner((Owner) beDeleted);

				result = new ModelAndView("redirect:/");
			} catch (Throwable oops) {
				result = new ModelAndView("actor/administrator/deleteActor");

				result.addObject("error", oops.getMessage());
			}

		}

		return result;
	}

	// Metodos auxiliares ---------------------------------------------------
	public ModelAndView createEditModelAndView(AdminForm adminForm) {
		ModelAndView result;

		result = new ModelAndView("actor/edit");
		result.addObject("objectForm", adminForm);
		result.addObject("buttonName", "saveAdmin");

		return result;
	}

	public ModelAndView createEditModelAndView(AdminForm adminForm,
											   String messageName,
											   String messageValue) {
		ModelAndView result;

		result = this.createEditModelAndView(adminForm);
		result.addObject(messageName, messageValue);

		return result;
	}

}
