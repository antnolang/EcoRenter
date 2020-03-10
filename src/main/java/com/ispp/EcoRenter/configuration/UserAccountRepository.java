package com.ispp.EcoRenter.configuration;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ispp.EcoRenter.security.Authority;
import com.ispp.EcoRenter.security.UserAccount;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
	
	
	Optional<UserAccount> findByUsername(String username);
	
	@Query(value = "select authority a from eco_renter.user_account_authorities where user_account_id = ?1", nativeQuery = true)
	String findByUserId(int id);
	
	
}
