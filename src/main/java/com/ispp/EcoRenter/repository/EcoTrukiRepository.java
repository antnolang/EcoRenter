package com.ispp.EcoRenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.EcoTruki;

@Repository
public interface EcoTrukiRepository extends JpaRepository<EcoTruki, Integer> {

}
