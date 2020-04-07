package com.ispp.EcoRenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.model.ProviderDiscountCode;

@Repository
public interface ProviderDiscountCodeRepository
	extends JpaRepository<ProviderDiscountCode, Integer> {

}
