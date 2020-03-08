package com.ispp.EcoRenter.service;

import java.util.Collection;

import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    // Repository

    @Autowired
    private CommentRepository commentRepository;

    // Constructor

    public CommentService(){
        super();
    }

    // CRUD methods

    // Other business methods

    public Collection<Comment> findCommentsBySmallholdingId(int smallholdingId){
        Collection<Comment> result;

        result = this.commentRepository.findCommentsBySmallholdingId(smallholdingId);

        return result;
    }
}