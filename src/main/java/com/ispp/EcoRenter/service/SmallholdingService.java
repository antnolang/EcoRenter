package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import com.ispp.EcoRenter.model.Actor;
import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.RentOut;
import com.ispp.EcoRenter.model.Renter;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.repository.SmallholdingRepository;

@Service
public class SmallholdingService {

    // Repository

    @Autowired
    private SmallholdingRepository smallholdingRepository;

    // Supporting services

    @Autowired
    private ActorService actorService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private Validator validator;
    
    @Autowired
    private RentOutService rentOutService;

    // Constructor

    public SmallholdingService(){
        super();
    }

    // CRUD Methods

    public Smallholding create(){
        Smallholding result;
        Owner principal;

        principal = this.ownerService.findByPrincipal();
        Assert.notNull(principal, "El propietario debe existir");

        result = new Smallholding();
        result.setStatus("NO ALQUILADA");
        result.setAvailable(true);
        result.setOwner(principal);
        result.setPhotos(Collections.emptySet());

        return result;
    }

    public Smallholding save(Smallholding smallholding){
        Assert.notNull(smallholding, "La parcela debe existir");
        Assert.isTrue(smallholding.getOwner().equals(
            this.ownerService.findByPrincipal()), "El propietario de la parcela no corresponde con el usuario autenticado");
        Assert.isTrue(!smallholding.getOwner().getIban().isEmpty(), "El propietario debe tener un IBAN asociado");
        Assert.isTrue(smallholding.getStatus().equals("NO ALQUILADA"), "No se puede editar una parcela ya alquilada");

        Smallholding result;

        result = this.smallholdingRepository.save(smallholding);

        return result;

    }

    public void deactivate(Smallholding smallholding){
        Assert.notNull(smallholding, "La parcela no debe ser nula");
        Assert.isTrue(smallholding.getOwner().equals(
            this.ownerService.findByPrincipal()), "El propietario de la parcela no corresponde con el usuario autenticado");
        Assert.isTrue(smallholding.getStatus().equals("NO ALQUILADA"), "No se puede editar una parcela ya alquilada");
        Assert.isTrue(smallholding.getId() != 0, "La parcela no existe");
        Assert.isTrue(smallholding.isAvailable(), "La parcela está ya deshabilitada");

        smallholding.setAvailable(false);
        this.smallholdingRepository.flush();
        
    }

    public void activate(Smallholding smallholding){
        Assert.notNull(smallholding, "La parcela no debe ser nula");
        Assert.isTrue(smallholding.getOwner().equals(
            this.ownerService.findByPrincipal()), "El propietario de la parcela no corresponde con el usuario autenticado");
        Assert.isTrue(smallholding.getStatus().equals("NO ALQUILADA"), "No se puede editar una parcela ya alquilada");
        Assert.isTrue(smallholding.getId() != 0, "La parcela no existe");
        Assert.isTrue(!smallholding.isAvailable(), "La parcela está ya habilitada");

        smallholding.setAvailable(true);
        this.smallholdingRepository.flush();
        
    }

    public Collection<Smallholding> findAll() {
        Collection<Smallholding> result;

        result = this.smallholdingRepository.findAll();

        return result;
    }

    public Smallholding findOne(int smallholdingId) {
        Assert.isTrue(smallholdingId > 0, "La parcela no existe");

        Smallholding result;

        result = this.smallholdingRepository.findById(smallholdingId).get();
        Assert.notNull(result, "La parcela no existe");

        return result;
    }

    public Smallholding findOneToDisplay(int smallholdingId){
        Smallholding result;
        Actor principal;
        Boolean isRentedByRenter;

        result = this.findOne(smallholdingId);

        try {
            principal = this.actorService.findByPrincipal();
        } catch(Exception e){
            principal = null;
        }

        /*
            If principal is a renter or unauthenticated, he/she displays availables and not rented smallholdings only.

            For the other side, if principal is an owner, he/she displays availables smallholdings unless it's his/her own so
            displays unavailables too.
        */
        if(principal == null) // Un usuario no autenticado solo puede ver las parcelas disponibles y con estado no alquilada
            Assert.isTrue(result.isAvailable() && result.getStatus().equals("NO ALQUILADA"),"La parcela no se puede mostrar");
        else if((principal != null && principal instanceof Renter)){
            /* 
                El arrendatario solo puede ver la parcela en caso de que no esté alquilada y su estado sea disponible o bien, en caso
                de que esté alquilada por el actualmente
            */
            isRentedByRenter = this.isSmallholdingRentedByRenter(principal.getId(), smallholdingId);
            Assert.isTrue((result.isAvailable() && result.getStatus().equals("NO ALQUILADA") || isRentedByRenter),"La parcela no se puede mostrar");
        }
        else if(principal != null && principal instanceof Owner){
             /* 
                El propietario solo puede ver la parcela en caso de que no esté alquilada y su estado sea disponible o bien, en caso
                de que sea su parcela
            */
            Assert.isTrue((result.isAvailable() && result.getStatus().equals("NO ALQUILADA")) || result.getOwner().equals(principal),"La parcela no se puede mostrar");
        }

        return result;
        
        
    }

    public Smallholding findOneToEdit(int smallholdingId){
        Smallholding result;

        result = this.findOne(smallholdingId);
        Assert.isTrue(result.getOwner().equals(
            this.ownerService.findByPrincipal()), "El propietario de la parcela no corresponde con el usuario autenticado");
        Assert.isTrue(result.getStatus().equals("NO ALQUILADA"), "La parcela no se puede editar debido a que se encuentra en estado distinto a NO ALQUILADA");

        return result;
    }

    // Other business methods

    public Smallholding reconstruct(final Smallholding smallholding, final BindingResult binding) {
		Smallholding result, stored;

		if (smallholding.getId() == 0)
			result = this.create();
		else {
			stored = this.findOne(smallholding.getId());

			result = new Smallholding();
			result.setId(stored.getId());
            result.setVersion(stored.getVersion());
            result.setOwner(stored.getOwner());
            result.setStatus(stored.getStatus().trim());
            result.setAvailable(stored.isAvailable());
        }
       
        result.setTitle(smallholding.getTitle().trim());
        result.setSize(smallholding.getSize());
        result.setDescription(smallholding.getDescription().trim());
        result.setFarmingType(smallholding.getFarmingType().trim());
        result.setPrice(smallholding.getPrice());
        result.setProvince(smallholding.getProvince().trim());
        result.setLocality(smallholding.getLocality().trim());
        result.setAddress(smallholding.getAddress().trim());
        result.setPostalCode(smallholding.getPostalCode().trim());
        result.setLatitude(smallholding.getLatitude().trim());
        result.setLongitude(smallholding.getLongitude().trim());
        result.setMaxDuration(smallholding.getMaxDuration());
        result.setImages(smallholding.getImages().trim());
        result.setPhotos(smallholding.getPhotos());

		this.validator.validate(result, binding);

		return result;
	}

    public Page<Smallholding> findPaginated(Pageable pageable, Collection<Smallholding> smallholdings) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Smallholding> list;
        ArrayList<Smallholding> smaList= new ArrayList<>(smallholdings);
 
        if (smaList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, smaList.size());
            list = smaList.subList(startItem, toIndex);
        }
 
        Page<Smallholding> smPage
          = new PageImpl<Smallholding>(list, PageRequest.of(currentPage, pageSize), smaList.size());
 
        return smPage;
    }

    public Collection<Smallholding> findSmallholdingsByOwnerId(int ownerId){
        Collection<Smallholding> result;

        result = this.smallholdingRepository.findSmallholdingsByOwnerId(ownerId);

        return result;
    }

    public Collection<Smallholding> findSmallholdingsAvailables(){
        Collection<Smallholding> result;

        result = this.smallholdingRepository.findSmallholdingsAvailables();

        return result;
    }

    public Boolean isSmallholdingRentedByRenter(int renterId, int smallholdingId){
        Boolean result;
        Collection<RentOut> rentOuts;

        result = null;
        rentOuts = this.rentOutService.findRentOutsBySmallholdingAndRenter(smallholdingId, renterId);
        if(rentOuts.size() == 0)
            result = false;
        else {
            for(RentOut ro: rentOuts){
                result = this.smallholdingRepository.isSmallholdingRentedByRenter(renterId, smallholdingId, ro.getEndDate());
                if(result)
                    break;
            }
        }
            

        return result;
    }

    public Collection<Smallholding> findSmallholdingsByActiveRentOut(int renterId) {
    	Collection<Smallholding> results;
    	 	
    	results = this.smallholdingRepository.findSmallholdingsByActiveRentOut(renterId);
    	
    	return results;
    }
    
}