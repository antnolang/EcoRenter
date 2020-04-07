package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.ProviderDiscountCode;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.repository.ProviderDiscountCodeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Transactional
public class ProviderDiscountCodeService {

    // Repository

    @Autowired
    private ProviderDiscountCodeRepository providerDiscountCodeRepository;

    // Services

    @Autowired
    private ActorService actorService;

    @Autowired
    private SmallholdingService smallholdingService;

    // Constructor

    public ProviderDiscountCodeService(){
        super();
    }

    // CRUD methods

    public ProviderDiscountCode create(){
        ProviderDiscountCode result;

        result = new ProviderDiscountCode();

        return result;
    }

    public ProviderDiscountCode findOneToDisplay(int providerDiscountCodeId){
        ProviderDiscountCode result;
        Actor actor;
        Collection<Smallholding> smallholdings;

        actor = this.actorService.findByPrincipal();
        switch(actor.getUserAccount().getAuthorities().toString()){
            case "[RENTER]":
                smallholdings = this.smallholdingService.findSmallholdingsByActiveRentOut(actor.getId());
                Assert.isTrue(smallholdings.size() > 0, "No está autorizado a mostrar el descuento");
                break;
            case "[OWNER]":
                smallholdings = this.smallholdingService.findSmallholdingsByOwnerId(actor.getId());
                Assert.isTrue(smallholdings.size() > 0, "No está autorizado a mostrar el descuento");
                break;
        }

        result = this.providerDiscountCodeRepository.findById(providerDiscountCodeId).get();
        Assert.notNull(result, "El descuento no existe");

        return result;
    }

    public Collection<ProviderDiscountCode> findAll(){
        Collection<ProviderDiscountCode> result;

        result = this.providerDiscountCodeRepository.findAll();

        return result;
    }

    public ProviderDiscountCode save(ProviderDiscountCode providerDiscountCode){
        Assert.notNull(providerDiscountCode, "El descuento es nulo");

        ProviderDiscountCode result;

        result = this.providerDiscountCodeRepository.save(providerDiscountCode);
        this.providerDiscountCodeRepository.flush();

        return result;
    }

    public void delete(ProviderDiscountCode providerDiscountCode){
        Assert.notNull(providerDiscountCode, "El descuento es nulo");
        Assert.isTrue(providerDiscountCode.getId() != 0, "El descuento no existe");

        this.providerDiscountCodeRepository.delete(providerDiscountCode);
    }

    // Other business methods

    public Page<ProviderDiscountCode> findPaginated(Pageable pageable, Collection<ProviderDiscountCode> providerDiscountCodes) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ProviderDiscountCode> list;
        ArrayList<ProviderDiscountCode> dcList= new ArrayList<>(providerDiscountCodes);
 
        if (dcList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, dcList.size());
            list = dcList.subList(startItem, toIndex);
        }
 
        Page<ProviderDiscountCode> cdPage
          = new PageImpl<ProviderDiscountCode>(list, PageRequest.of(currentPage, pageSize), dcList.size());
 
        return cdPage;
    }

}