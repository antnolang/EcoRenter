package com.ispp.EcoRenter.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query("select c from Comment c where c.smallholding.id=?1 order by c.writtenMoment desc")
    Collection<Comment> findCommentsBySmallholdingId(int smallholdingId);
    
    @Query("select c from Comment c where c.actor.id=?1")
    Collection<Comment> findCommentsByActor(int actorId);
    
}