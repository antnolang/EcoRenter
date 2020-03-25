package com.ispp.EcoRenter.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.repository.UserAccountRepository;
import com.ispp.EcoRenter.security.UserAccount;

@Service
@Transactional
public class UserAccountService {
	
	//Attributes

	@Autowired
	private UserAccountRepository userAccountRepository;
	
	
	//CRUD Methods
	
	public UserAccount create() {
		
		return new UserAccount();
	}
	

	public UserAccount save(UserAccount userAccount) {
		
		
		return this.userAccountRepository.save(userAccount);
		
	}
	
	
}
