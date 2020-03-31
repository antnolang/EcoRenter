package com.ispp.EcoRenter.service;

import java.util.Calendar;
import java.util.Collection;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.RentOut;

import com.ispp.EcoRenter.model.Renter;

import com.ispp.EcoRenter.repository.RentOutRepository;

@Service
@Transactional
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
    
    public void delete(RentOut rentout){
    	this.rentOutRepository.delete(rentout);
    }
    
    // Other business method

    public void checkChard(String card, String fecha, String cvv){
        Assert.notNull(card, "La tarjeta no puede ser nula");
        Assert.notNull(fecha, "La fecha no puede ser nula");
        Assert.notNull(cvv, "El cvv no puede ser nula");
        Assert.isTrue(card.length() >= 12 && card.length() <= 19, "El número de tarjeta debe contener entre 12 y 19 dígitos");
        Assert.isTrue(cvv.length() == 3, "El CVV debe contener 3 dígitos");
        Assert.isTrue(fecha.matches("(?:0[1-9]|1[0-2])/[0-9]{2}"), "La fecha es inválida");
        int actualYear,actualMonth, twoDigitsYear, cardYear, cardMonth;
        String digitsYear;

        actualYear = Calendar.getInstance().get(Calendar.YEAR);
        actualMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
        digitsYear = String.valueOf(actualYear).substring(2);
        twoDigitsYear = Integer.parseInt(digitsYear);
        cardYear = Integer.parseInt(fecha.split("/")[1]);
        cardMonth = Integer.parseInt(fecha.split("/")[0]);

        Assert.isTrue(cardYear >= twoDigitsYear, "La tarjeta de crédito ha expirado");
        if(twoDigitsYear == cardYear)
            Assert.isTrue(cardMonth >= actualMonth,"La tarjeta de crédito ha vencido");
        

    }

    public Collection<RentOut> findRentOutsBySmallholdingAndRenter(int smallholdingId, int renterId){
        Collection<RentOut> result;

        result = this.rentOutRepository.findRentOutBySmallholdingAndRenter(smallholdingId, renterId);
        Assert.notNull(result,"No instancia la colección vacía de alquileres");

        return result;
    }
    
    public Collection<RentOut> findRentOutsByRenter(int renterId){
        Collection<RentOut> result;

        result = this.rentOutRepository.findRentOutByRenter(renterId);
        Assert.notNull(result,"No instancia la colección vacía de alquileres");

        return result;
    }
}