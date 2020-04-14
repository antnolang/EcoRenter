package com.ispp.EcoRenter.controller;

import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.service.CommentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/comment")
public class CommentController {

    // Services

    @Autowired
    private CommentService commentService;

    // Constructor

    public CommentController(){
        super();
    }

    // Create



}