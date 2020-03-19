package com.ispp.EcoRenter.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.repository.RenterRepository;
import com.ispp.EcoRenter.security.Authority;
import com.ispp.EcoRenter.security.UserAccount;

@Service
public class RenterService {

    // Repository

    @Autowired
    private RenterRepository renterRepository;
    
    @Autowired
    private UserAccountService userAccountService;

    // Constructor

    public RenterService(){
        super();
    }

    // CRUD methods
    
    public Renter create() {
    	final Renter result = new Renter();
    	final UserAccount userAccount = this.userAccountService.create();
    	
    	final Authority auth = new Authority();
    	auth.setAuthority(Authority.RENTER);
    	userAccount.addAuthority(auth);
    	
    	result.setUserAccount(userAccount);
    	
    	return result;
    	
    }
    
    
    public Renter findOne(int renterId) {
    	Assert.isTrue(renterId > 0, "Invalid renterId");
    	
    	Optional<Renter> renter;
    	Renter result;
    	
    	renter = this.renterRepository.findById(renterId);
    	
    	result = renter.orElse(null);
    	
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