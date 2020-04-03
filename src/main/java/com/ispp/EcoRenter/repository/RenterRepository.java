package com.ispp.EcoRenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.Renter;

@Repository
public interface RenterRepository extends JpaRepository<Renter,Integer> {

    @Query("select r from Renter r where r.userAccount.username = ?1")
    Renter findRenterByUsername(String username);
    
    @Query("select r from Renter r join r.creditCards cc where cc.id = ?1")
    Renter findRenterByCreditCard(int creditCardId);
}