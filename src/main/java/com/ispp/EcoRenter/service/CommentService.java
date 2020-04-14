package com.ispp.EcoRenter.service;

import java.util.Collection;

import javax.transaction.Transactional;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Transactional
public class CommentService {

    // Repository

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RentOutService rentOutService;

    @Autowired
    private ActorService actorService;

    // Constructor

    public CommentService(){
        super();
    }

    // CRUD methods

    public Comment create(int rentOutId){
        Assert.isTrue(rentOutId !=0, "El alquiler debe existir");
        Assert.notNull(rentOutId, "El alquiler no puede ser nulo");

        Comment result;
        RentOut rentOut;

        rentOut = this.rentOutService.findOne(rentOutId);
        this.checkEdit(rentOut);

        result = new Comment();
        result.setRentOut(this.rentOutService.findOne(rentOutId));

        return result;

    }

    public Comment save(Comment comment){
        Assert.isTrue(comment.getId() != 0, "El comentario debe existir");
        Assert.notNull(comment, "El comentario no puede ser nulo");

        Comment result;
        
        this.checkEdit(comment.getRentOut());

        result = this.commentRepository.save(comment);

        return result;

    }

    public Comment findOne(int commentId){
        Comment result;

        result = this.commentRepository.findById(commentId).get();
        Assert.notNull(result, "El comentario debe existir");

        return result;
    }
    
    
    public void delete(Comment comment) {
        Assert.isTrue(comment.getId() != 0, "El comentario debe existir");
        Assert.notNull(comment, "El comentario no puede ser nulo");

        this.checkEdit(comment.getRentOut());

    	this.commentRepository.delete(comment);
    }

    // Other business methods

    public Collection<Comment> findCommentsBySmallholdingId(int smallholdingId){
        Collection<Comment> result;

        result = this.commentRepository.findCommentsBySmallholdingId(smallholdingId);

        return result;
    }
    
    public Collection<Comment> findCommentsByRentOut(int rentOutId){
       
        return this.commentRepository.findCommentsByRentOut(rentOutId);
    }

    private void checkEdit(RentOut rentOut){
        Actor actor;
        actor = this.actorService.findByPrincipal();

        if(actor instanceof Renter)
            Assert.isTrue(rentOut.getRenter().equals(actor), "No se pueden hacer comentarios sobre parcelas no alquiladas");
        else if(actor instanceof Owner)
            Assert.isTrue(rentOut.getSmallholding().getOwner().equals(actor), "No se pueden hacer comentarios sobre otras parcelas");
    }
}