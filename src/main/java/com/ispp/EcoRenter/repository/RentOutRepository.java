package com.ispp.EcoRenter.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.RentOut;

@Repository
public interface RentOutRepository extends JpaRepository<RentOut, Integer> {

    @Query("select ro from RentOut ro where ro.smallholding.id = ?1 and ro.renter.id = ?2 ")
    Collection<RentOut> findRentOutBySmallholdingAndRenter(int smallholdingId, int renterId);
    

    @Query("select ro from RentOut ro where ro.smallholding.owner.id = ?1 and ro.smallholding.id = ?2 and ro.isActive = true")
    RentOut findRentOutByOwnerAndSmallholding(int ownerId, int smallholdingId);

    
    @Query("select r from RentOut r where r.smallholding.id = ?1")
    Collection<RentOut> findBySmallholding(int smallholdingId);

    
    @Query("select ro from RentOut ro where ro.renter.id = ?1")
    Collection<RentOut> findRentOutByRenter(int renterId);

    @Query("select count(ro) from RentOut ro where ro.creditCard.id = ?1")
    Integer findRentOutByCreditCard(int creditCardId);
}