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

    @Query("select sh from Smallholding sh where sh.isAvailable=true and sh.isArgumented=false and sh.status = 'NO ALQUILADA'")
    Collection<Smallholding> findSmallholdingsAvailables();
    
    @Query("select distinct ro.smallholding from RentOut ro where ro.renter.id = ?1 and ro.isActive = true")
    Collection<Smallholding> findSmallholdingsByActiveRentOut(int renterId);

    @Query("select distinct ro.smallholding from RentOut ro where ro.smallholding.owner.id = ?1 and ro.isActive = true")
    Collection<Smallholding> findSmallholdingsRentedByOwnerId(int ownerId);

    @Query("select distinct s from Smallholding s where (s.isAvailable = true and s.isArgumented = false and s.status = 'NO ALQUILADA') and ((s.province like concat('%', concat(?1, '%'))) or (s.farmingType like concat('%', concat(?1, '%'))) or (s.locality like concat('%', concat(?1, '%'))) or (CAST(s.size as text) like concat('%', concat(?1, '%'))) or (CAST(s.price as text) like concat('%', concat(?1, '%'))))")
    Collection<Smallholding> findSmallholdingsByKeyword(String keyword);

}