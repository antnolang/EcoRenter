package com.ispp.EcoRenter.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.model.Owner;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.model.Smallholding;
import com.ispp.EcoRenter.service.OwnerService;
import com.ispp.EcoRenter.service.PhotoService;
import com.ispp.EcoRenter.service.SmallholdingService;

@SpringBootTest
public class SmallholdingServiceTest {

    @Autowired
    private SmallholdingService smallholdingService;

    @Autowired
    private OwnerService ownerService;

    @Autowired
    private PhotoService photoService;

    /*
        El owner1 realiza un listado de sus parcelas
    */
    @Transactional
    @WithMockUser("propietario1")
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
    @Transactional
    @WithMockUser("propietario1")
    @Test
    public void create_positive_test(){
        Smallholding smallholding, saved;
        List<Photo> photos;

        smallholding = this.smallholdingService.create();
        photos = new ArrayList<Photo>();
        Photo photo;
		MultipartFile file;
		Path path;
		String name, originalFilename, contentType;
		byte[] data;
		
		path = Paths.get("C:\\Users\\alvar\\Pictures\\motor.jpg");
		name = "motor.jpg";
		originalFilename = "motor.jpg";
		contentType = "image/jpg";
		
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			data = null;
		}
		
		file = new MockMultipartFile(name, originalFilename, contentType, data);
		photo = this.photoService.storeImage(file);

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
        photos.add(photo);
        smallholding.setPhotos(photos);

        saved = this.smallholdingService.save(smallholding);

        Assert.isTrue(saved.getId() != 0, "La parcela no se ha creado correctamente");

    }

     /*
        La parcela no se crea correctamente. El atributo título está en blanco
    */
    @WithMockUser("propietario1")
    @Test
    public void create_negative_test() {
        Smallholding smallholding;
        Boolean catched;
        List<Photo> photos;

        smallholding = this.smallholdingService.create();
        photos = new ArrayList<Photo>();

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
        photos.add(this.photoService.findOne(1));
        smallholding.setPhotos(photos);

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
    @Transactional
    @WithMockUser("propietario1")
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
    @Transactional
    @WithMockUser("propietario2")
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
    @Transactional
    @WithMockUser("propietario1")
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
    @WithMockUser("arrendatario2")
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