package com.ispp.EcoRenter.controller;

import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/comment")
public class CommentController {

    // Services

    @Autowired
	private CommentService commentService;
	
	@Autowired
	private SmallholdingController smallholdingController;

    // Constructor

    public CommentController(){
        super();
    }

    // Save

	@PostMapping(value = "/edit", params = "comentar")
	public ModelAndView save(Comment comment, final BindingResult binding) {
        ModelAndView result;
        Comment commentRec=null;
		
		if (binding.hasErrors()) {
			result = this.createEditModelAndView(comment);
		} else {
			try {
				commentRec = this.commentService.reconstruct(comment,binding);
                this.commentService.save(commentRec);
                result = new ModelAndView("redirect:/smallholding/display?smallholdingId=" + commentRec.getSmallholding().getId());
			} catch (final Throwable oops) {
                result = this.createEditModelAndView(comment, "No se pudo realizar la operación");
			}
		}

		return result;
    }

    // Delete

	@GetMapping(value = "/delete")
	public ModelAndView delete(@RequestParam int commentId) {
        ModelAndView result;
        Comment comment;

		comment = this.commentService.findOneToEdit(commentId);
		try {
            this.commentService.delete(comment);
            result = new ModelAndView("redirect:/smallholding/display?smallholdingId=" + comment.getSmallholding().getId());
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(comment, "No se pudo realizar la operación");
		}

		return result;
	}

    // Ancillary methods

	protected ModelAndView createEditModelAndView(final Comment comment) {
		ModelAndView result;

		result = this.createEditModelAndView(comment, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Comment comment, final String messageCode) {
		ModelAndView result;
		result = this.smallholdingController.display(comment.getSmallholding().getId());
		result.addObject("comment", comment);
		result.addObject("messageCode", messageCode);

		return result;
	}


}