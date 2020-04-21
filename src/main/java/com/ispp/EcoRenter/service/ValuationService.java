package com.ispp.EcoRenter.service;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.transaction.Transactional;

import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Valuation;
import com.ispp.EcoRenter.repository.ValuationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

@Service
@Transactional
public class ValuationService {

    // Repositorio

    @Autowired
    private ValuationRepository valuationRepository;

    // Servicios

    @Autowired
    private RenterService renterService;

    @Autowired
    private RentOutService rentOutService;

    @Autowired
    private UtilityService utilityService;

    @Autowired
    private Validator validator;

    // CRUD Methods

    public Valuation create(){
        Valuation result;

        result = new Valuation();
        result.setValuationMoment(this.utilityService.getCurrentMoment());

        return result;
    }

    public Valuation save(Valuation valuation, int rentOutId){
        Assert.notNull(valuation, "La valoración no puede ser nula");

        this.checkNew(rentOutId);

        Valuation result;
        RentOut rentOut;
        // Calendar startDateCalendarPlus1;
        // Date startDatePlus1;

        rentOut = this.rentOutService.findOne(rentOutId);
        Assert.isTrue(rentOut.getRenter().equals(this.renterService.findByPrincipal()), "El arrendatario debe valorar sobre parcelas que ha alquilado");
        /*
        startDateCalendarPlus1= Calendar.getInstance();
        startDateCalendarPlus1.setTime(rentOut.getStartDate());
        startDateCalendarPlus1.add(Calendar.MONTH, 1);
        startDatePlus1 = startDateCalendarPlus1.getTime();

        Assert.isTrue(this.utilityService.getCurrentMoment().after(startDatePlus1), "Para realizar una valoración debe haber transcurrido un mes del alquiler");
        */
        result = this.valuationRepository.save(valuation);

        this.addValuation(rentOut, valuation);

        return result;
    }


    // Reglas de negocio

    private void addValuation(RentOut rentOut, Valuation valuation){
        rentOut.setValuation(valuation);
    }

    public Valuation reconstruct(Valuation valuation, BindingResult binding){
        Valuation result;

        result = this.create();
        result.setMark(valuation.getMark());

        this.validator.validate(result, binding);

        return result;
    }

    public void checkNew(int rentOutId){
        RentOut rentOut;

        rentOut = this.rentOutService.findOneToDisplay(rentOutId);
        Assert.isNull(rentOut.getValuation(), "Ya se ha hecho una valoración");
    }

    public Collection<Valuation> findValuationsBySmallholding(int smallholdingId){
        Collection<Valuation> result;

        result = this.valuationRepository.findValuationsBySmallholding(smallholdingId);

        return result;
    }

    public Long avgMarkSmallholding(int smallholdingId){
        Collection<Valuation> valuations;
        int marksCounter;
        Long avg;

        valuations = this.findValuationsBySmallholding(smallholdingId);
        marksCounter = 0;

        for(Valuation v: valuations)
            marksCounter = marksCounter + v.getMark();
        
        avg = (long) (marksCounter / valuations.size());

        return avg;
    }

    

}