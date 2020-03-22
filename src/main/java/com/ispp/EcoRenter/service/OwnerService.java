package com.ispp.EcoRenter.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.repository.OwnerRepository;

@Service
@Transactional
public class OwnerService {

    // Repository

    @Autowired
    private OwnerRepository ownerRepository;

    // Supporting services

    // Constructor

    public OwnerService(){
        super();
    }

    // CRUD Methods
    public Owner findOne(int ownerId) {
    	Assert.isTrue(ownerId > 0, "Invalid ownerId");
    	
    	Optional<Owner> owner;
    	Owner result;
    	
    	owner = this.ownerRepository.findById(ownerId);
    	
    	result = owner.orElse(null);
    	
    	return result;
    }

    // Other business methods

    public Owner findByPrincipal(){
        Owner result;
        UserDetails userAccount;
        Authentication authentication;

        authentication = SecurityContextHolder.getContext().getAuthentication();
        userAccount = (UserDetails) authentication.getPrincipal();

        result = this.ownerRepository.findOwnerByUsername(userAccount.getUsername());
        Assert.notNull(result,"El propietario no existe");
        
        return result;
    }
}