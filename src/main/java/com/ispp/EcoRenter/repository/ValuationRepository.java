package com.ispp.EcoRenter.repository;

import java.util.Collection;

import com.ispp.EcoRenter.model.Valuation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ValuationRepository extends JpaRepository<Valuation, Integer> {

    @Query("select ro.valuation from RentOut ro where ro.smallholding.id = ?1")
    Collection<Valuation> findValuationsBySmallholding(int smallholdingId);

}