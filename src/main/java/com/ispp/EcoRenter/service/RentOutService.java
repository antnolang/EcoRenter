package com.ispp.EcoRenter.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.repository.RentOutRepository;

@Service
public class RentOutService {

    // Repository

    @Autowired
    private RentOutRepository rentOutRepository;

    // Constructor

    public RentOutService(){
        super();
    }

    // CRUD methods

    // Other business method  
    public Collection<RentOut> findRentOutsBySmallholdingAndRenter(int smallholdingId, int renterId){
        Collection<RentOut> result;

        result = this.rentOutRepository.findRentOutBySmallholdingAndRenter(smallholdingId, renterId);
        Assert.notNull(result,"No instancia la colección vacía de alquileres");

        return result;
    }
}