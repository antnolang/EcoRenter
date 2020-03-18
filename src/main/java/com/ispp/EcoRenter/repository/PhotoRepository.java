package com.ispp.EcoRenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

	
	
}
