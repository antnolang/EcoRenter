package com.ispp.EcoRenter.service;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.repository.RentOutRepository;

@Service
public class RentOutService {

    // Repository

    @Autowired
    private RentOutRepository rentOutRepository;
    
    @Autowired
    RenterService renterService;

    // Constructor

    public RentOutService(){
        super();
    }

    // CRUD methods

    public RentOut create() {
    	
    	Renter principal = this.renterService.findByPrincipal();
    	Date startDate = new Date(System.currentTimeMillis()-1);
    	
    	RentOut result = new RentOut();
    	
    	result.setRenter(principal);
    	result.setStartDate(startDate);
    	
    	return result;
    	
    }
    
    
    public RentOut save(RentOut rent) {
    	RentOut result;
    	Renter principal = this.renterService.findByPrincipal();
    	
    	
    	Assert.notNull(rent, "No debe ser nulo.");
    	Assert.isTrue(rent.getRenter().equals(principal), "No la está alquilando el usuario logeado.");
    	
    	
    	result = this.rentOutRepository.save(rent);
    	
    	return result;
    }
    // Other business method

    public Collection<RentOut> findRentOutsBySmallholdingAndRenter(int smallholdingId, int renterId){
        Collection<RentOut> result;

        result = this.rentOutRepository.findRentOutBySmallholdingAndRenter(smallholdingId, renterId);
        Assert.notNull(result,"No instancia la colección vacía de alquileres");

        return result;
    }
}