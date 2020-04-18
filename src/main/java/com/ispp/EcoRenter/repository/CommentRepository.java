package com.ispp.EcoRenter.repository;

import java.util.Collection;

import com.ispp.EcoRenter.model.Comment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Integer> {

    @Query("select c from Comment c where c.rentOut.smallholding.id=?1")
    Collection<Comment> findCommentsBySmallholdingId(int smallholdingId);
    
    @Query("select c from Comment c where c.rentOut.id=?1")
    Collection<Comment> findCommentsByRentOut(int rentOutId);
}