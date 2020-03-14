package com.ispp.EcoRenter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.repository.RenterRepository;

@Service
public class RenterService {

    // Repository

    @Autowired
    private RenterRepository renterRepository;

    // Constructor

    public RenterService(){
        super();
    }

    // CRUD methods
    public Renter findOne(int renterId) {
    	Assert.isTrue(renterId > 0, "Invalid renterId");
    	
    	Renter result;
    	
    	result = this.renterRepository.findById(renterId).get();
    	
    	return result;
    }
    
    // Other business methods

    public Renter findByPrincipal(){
        Renter result;
        UserDetails userAccount;
        Authentication authentication;

        authentication = SecurityContextHolder.getContext().getAuthentication();
        userAccount = (UserDetails) authentication.getPrincipal();
        
        result = this.renterRepository.findRenterByUsername(userAccount.getUsername());
        Assert.notNull(result,"El arrendatario no existe");

        return result;
    }


}