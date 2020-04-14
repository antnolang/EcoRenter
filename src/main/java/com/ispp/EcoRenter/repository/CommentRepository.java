package com.ispp.EcoRenter.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query("select c from Comment c where c.smallholding.id=?1")
    Collection<Comment> findCommentsBySmallholdingId(int smallholdingId);
    
}