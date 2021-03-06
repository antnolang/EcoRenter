package com.ispp.EcoRenter.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.Photo;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Integer> {

    @Query("select s.photos from Smallholding s where s.id = ?1")
    Collection<Photo> findPhotosBySmallholdingId(int smallholdingId);
    
    @Query("select p from Photo p where p.id = ?1")
    Photo findPhotoById(int photoId);

    @Query("select s.photos from EcoTruki s where s.id = ?1")
	Collection<Photo> findPhotosByEcoTrukiId(int id);
}
