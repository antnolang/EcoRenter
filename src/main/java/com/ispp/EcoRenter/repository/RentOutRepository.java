package com.ispp.EcoRenter.repository;

import java.util.Collection;

import com.ispp.EcoRenter.model.RentOut;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RentOutRepository extends JpaRepository<RentOut, Integer> {

    @Query("select ro from RentOut ro where ro.smallholding.id = ?1 and ro.renter.id = ?2")
    Collection<RentOut> findRentOutBySmallholdingAndRenter(int smallholdingId, int renterId);
}