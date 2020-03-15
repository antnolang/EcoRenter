package com.ispp.EcoRenter.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.Smallholding;

@Repository
public interface SmallholdingRepository extends JpaRepository<Smallholding,Integer> {

    @Query("select sh from Smallholding sh where sh.owner.id = ?1")
    Collection<Smallholding> findSmallholdingsByOwnerId(int ownerId);

    @Query("select sh from Smallholding sh where sh.isAvailable=true and sh.status = 'NO ALQUILADA'")
    Collection<Smallholding> findSmallholdingsAvailables();
    
    @Query("select distinct ro.smallholding from RentOut ro where ro.renter.id = ?1 and ro.isActive = true")
    Collection<Smallholding> findSmallholdingsByActiveRentOut(int renterId);
}