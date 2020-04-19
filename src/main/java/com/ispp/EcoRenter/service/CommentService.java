package com.ispp.EcoRenter.service;

import java.util.Collection;

import javax.transaction.Transactional;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Comment;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.repository.CommentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
@Transactional
public class CommentService {

    // Repository

    @Autowired
    private CommentRepository commentRepository;

    // Services

    @Autowired
    private SmallholdingService smallholdingService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private UtilityService utilityService;

    @Autowired
    private Validator validator;

    // Constructor

    public CommentService(){
        super();
    }

    // CRUD methods

    public Comment create(int smallholdingId){
        Assert.isTrue(smallholdingId !=0, "La parcela debe existir");

        Comment result;
        Smallholding smallholding;

        smallholding = this.smallholdingService.findOne(smallholdingId);
        this.checkEdit(smallholding);

        result = new Comment();
        result.setSmallholding(smallholding);
        result.setWrittenMoment(this.utilityService.getCurrentMoment());
        result.setActor(this.actorService.findByPrincipal());

        return result;

    }

    public Comment save(Comment comment){
        Assert.notNull(comment, "El comentario no puede ser nulo");

        Comment result;
        
        this.checkEdit(comment.getSmallholding());

        result = this.commentRepository.save(comment);

        return result;

    }

    public Comment findOneToEdit(int commentId){
        Comment result;

        result = this.commentRepository.findById(commentId).get();
        Assert.notNull(result, "El comentario debe existir");
        this.checkEdit(result.getSmallholding());

        return result;
    }
    
    
    public void delete(Comment comment) {
        Assert.isTrue(comment.getId() != 0, "El comentario debe existir");
        Assert.notNull(comment, "El comentario no puede ser nulo");
        Assert.isTrue(comment.getActor().equals(this.actorService.findByPrincipal()), "El actor que elimina debe ser el mismo que el creador del mismo");

        this.checkEdit(comment.getSmallholding());

    	this.commentRepository.delete(comment);
    }
    
    public void actorDelete(Comment comment) {
      

    	this.commentRepository.delete(comment);
    }

    // Other business methods

    public Collection<Comment> findCommentsBySmallholdingId(int smallholdingId){
        Collection<Comment> result;

        result = this.commentRepository.findCommentsBySmallholdingId(smallholdingId);

        return result;
    }
    
    public Collection<Comment> findCommentsByActor(int actorId){
    	
    	return this.commentRepository.findCommentsByActor(actorId);
    }
    
    private void checkEdit(Smallholding smallholding){
        Actor actor;
        actor = this.actorService.findByPrincipal();
        Collection<Smallholding> smallholdings;

        if(actor instanceof Renter){
            smallholdings = this.smallholdingService.findSmallholdingsByRenterId(actor.getId());
            Assert.isTrue(smallholdings.contains(smallholding), "No se pueden hacer comentarios sobre parcelas no alquiladas");
        } else if(actor instanceof Owner)
            Assert.isTrue(smallholding.getOwner().equals(actor), "No se pueden hacer comentarios sobre otras parcelas");
    }

    public Comment reconstruct(Comment comment, BindingResult binding){
        Comment result,saved;

        if(comment.getId() == 0)
            result = this.create(comment.getSmallholding().getId());
        else {
            saved = this.findOneToEdit(comment.getId());

            result = new Comment();
            result.setId(saved.getId());
            result.setVersion(saved.getVersion());
            result.setSmallholding(saved.getSmallholding());
            result.setWrittenMoment(saved.getWrittenMoment());
            result.setActor(this.actorService.findByPrincipal());
        }

        result.setText(comment.getText().trim());
            
        this.validator.validate(result, binding);

        return result;
    }
}