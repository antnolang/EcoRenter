package com.ispp.EcoRenter.services;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.Assert;

import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.OwnerService;
import com.ispp.EcoRenter.service.SmallholdingService;

@SpringBootTest
public class SmallholdingServiceTest {

    @Autowired
    private SmallholdingService smallholdingService;

    @Autowired
    private OwnerService ownerService;

    /*
        El owner1 realiza un listado de sus parcelas
    */
    @WithMockUser("owner1")
    @Test
    public void list_positive_test(){
        Collection<Smallholding> smallholdings;
        Owner owner;

        owner = this.ownerService.findByPrincipal();
        smallholdings = this.smallholdingService.findSmallholdingsByOwnerId(owner.getId());

        Assert.notNull(smallholdings, "Las parcelas son nulas"); 

    }

    /*
        La parcela se crea correctamente
    */
    @WithMockUser("owner1")
    @Test
    public void create_positive_test(){
        Smallholding smallholding, saved;

        smallholding = this.smallholdingService.create();
        smallholding.setTitle("Huerto 1");
        smallholding.setDescription("Huerto grande y cultivable");
        smallholding.setSize(40);
        smallholding.setFarmingType("Cultivo de hortalizas");
        smallholding.setPrice(40.0);
        smallholding.setProvince("Sevilla");
        smallholding.setLocality("Sevilla");
        smallholding.setAddress("Avenida de Prueba");
        smallholding.setPostalCode("41560");
        smallholding.setLatitude("80");
        smallholding.setLongitude("100");
        smallholding.setMaxDuration(3);
        smallholding.setImages("https://media.game.es/COVERV2/3D_L/147/147191.png");

        saved = this.smallholdingService.save(smallholding);

        Assert.isTrue(saved.getId() != 0, "La parcela no se ha creado correctamente");

    }

     /*
        La parcela no se crea correctamente. El atributo título está en blanco
    */

    @WithMockUser("owner1")
    @Test
    public void create_negative_test() {
        Smallholding smallholding;
        Boolean catched;

        smallholding = this.smallholdingService.create();
        smallholding.setTitle("");
        smallholding.setDescription("Huerto grande y cultivable");
        smallholding.setSize(40);
        smallholding.setFarmingType("Cultivo de hortalizas");
        smallholding.setPrice(40.0);
        smallholding.setProvince("Sevilla");
        smallholding.setLocality("Sevilla");
        smallholding.setAddress("Avenida de Prueba");
        smallholding.setPostalCode("41560");
        smallholding.setLatitude("80");
        smallholding.setLongitude("100");
        smallholding.setMaxDuration(3);
        smallholding.setImages("https://media.game.es/COVERV2/3D_L/147/147191.png");

        catched = false;
        try {
            this.smallholdingService.save(smallholding);
        } catch(TransactionSystemException t){
            catched = true;
        }

        Assert.isTrue(catched, "La parcela se ha guardado con errores");
        
    }

    /*
        El owner1 edita una parcela la cual no está alquilada
    */

    @WithMockUser("owner1")
    @Test
    public void edit_positive_test(){
        Smallholding smallholding;
        String oldTitle;

        smallholding = this.smallholdingService.findOneToEdit(401);
        oldTitle = smallholding.getTitle();

        smallholding.setTitle("Nuevo titulo");

        Assert.isTrue(!oldTitle.equals(smallholding.getTitle()), "No se ha modificado correctamente la parcela");
    }

     /*
        El owner2 intenta editar una parcela la cual no es suya
    */

    @WithMockUser("owner2")
    @Test
    public void edit_negative_test(){
        Boolean catched;

        catched = null;
        try {
            this.smallholdingService.findOneToEdit(401);
        } catch(IllegalArgumentException exception){
            catched = true;
        }
        
        Assert.isTrue(catched, "La parcela se ha modificado");
    }

    /*
        El owner1 intenta editar una parcela alquilada
    */

    @WithMockUser("owner1")
    @Test
    public void edit_negative_test2(){
        Boolean catched;

        catched = null;
        try {
            this.smallholdingService.findOneToEdit(400);
        } catch(IllegalArgumentException exception){
            catched = true;
        }

        Assert.isTrue(catched, "La parcela se ha modificado");
    }

    /*
        Renter2 visualiza una parcela de otro arrendatario alquilada
    */
    @WithMockUser("renter2")
    @Test
    public void display_negative_test(){
        Boolean catched;

        catched = null;
        try {
            this.smallholdingService.findOneToDisplay(400);
        } catch(IllegalArgumentException exception) {
            catched = true;
        }
        
        Assert.isTrue(catched, "La parcela no se puede mostrar");
    }

    
}