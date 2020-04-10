package com.ispp.EcoRenter.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.form.EcoTrukiForm;
import com.ispp.EcoRenter.model.EcoTruki;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.service.EcoTrukiService;
import com.ispp.EcoRenter.service.PhotoService;

@SpringBootTest
public class EcoTrukiServiceTest {

	// Service under testing -------------------
	@Autowired
	private EcoTrukiService ecoTrukiService;
	
	@Autowired
	private PhotoService photoService;
	
	/*
	 * Un administrador visualiza un eco truki
	 */
	@WithMockUser("admin1")
	@Test
	@Transactional
	public void positiveTest_findOne_uno() {
		int ecoTrukiId;
		EcoTruki ecoTruki;
		
		ecoTrukiId = 800;
		ecoTruki = this.ecoTrukiService.findOne(ecoTrukiId);
		
		assertNotNull(ecoTruki);
	}
	
	
	/*
	 * Un renter con al menos un alquiler activo visualiza un eco truki
	 */
	@WithMockUser("renter1")
	@Test
	@Transactional
	public void positiveTest_findOne_dos() {
		int ecoTrukiId;
		EcoTruki ecoTruki;
		
		ecoTrukiId = 800;
		ecoTruki = this.ecoTrukiService.findOne(ecoTrukiId);
		
		assertNotNull(ecoTruki);
	}
	
	
	/*
	 * Un owner con al menos un alquiler activo visualiza un eco truki
	 */
	@WithMockUser("owner1")
	@Test
	@Transactional
	public void positiveTest_findOne_tres() {
		int ecoTrukiId;
		EcoTruki ecoTruki;
		
		ecoTrukiId = 800;
		ecoTruki = this.ecoTrukiService.findOne(ecoTrukiId);
		
		assertNotNull(ecoTruki);
	}
	
	
	/*
	 * Un renter sin alquileres activos trata de acceder a un eco truki
	 */
	@WithMockUser("renter2")
	@Test
	@Transactional
	public void negativeTest_findOne_uno() {
		int ecoTrukiId;
		EcoTruki ecoTruki;
		
		ecoTrukiId = 800;
		
		try {
			ecoTruki = this.ecoTrukiService.findOne(ecoTrukiId);
		} catch (IllegalArgumentException e) {
			ecoTruki = null;
		}
		
		assertTrue(ecoTruki == null);
	}
	
	/*
	 * Un administrador accede al listado de eco trukis
	 */
	@WithMockUser("admin1")
	@Test
	@Transactional
	public void test_findAll() {
		Page<EcoTruki> all;
		
		all = this.ecoTrukiService.findAll(1);
		
		assertNotNull(all);
		assertTrue(all.hasContent());
	}
	
	@WithMockUser("admin1")
	@Transactional
	@Test
	public void test_create() {
		EcoTruki ecoTruki;
		
		ecoTruki = this.ecoTrukiService.create();
		
		assertNotNull(ecoTruki);
		assertNotNull(ecoTruki.getMoment());
		assertNotNull(ecoTruki.getPhotos());
	}
	
	/*
	 * Un administrador crear un eco truki
	 */
	@WithMockUser("admin1")
	@Transactional
	@Test
	public void positiveTest_save_uno() {
		EcoTrukiForm ecoTrukiForm;
		EcoTruki ecoTruki;
		String title;
		String description;
		Set<MultipartFile> files;
		
		title = "Title test";
		description = "Description test";
		files = this.getFiles("photo-test.png", "image/png");		
		
		ecoTrukiForm = new EcoTrukiForm(0, title, description);
		ecoTrukiForm.setFiles(files);
		
		ecoTruki = this.ecoTrukiService.save(ecoTrukiForm);
		
		assertNotNull(ecoTruki);
		assertTrue(ecoTruki.getId() > 0);
	}
	
	
	/*
	 * Un administrador edita un eco truki
	 */
	@WithMockUser("admin1")
	@Transactional
	@Test
	public void positiveTest_save_dos() {
		EcoTrukiForm ecoTrukiForm;
		EcoTruki ecoTruki;
		String title;
		String description;
		int ecoTrukiId;
		
		ecoTrukiId = 800;
		title = "Title test";
		description = "Description test";		
		
		ecoTrukiForm = new EcoTrukiForm(ecoTrukiId, title, description);
		ecoTrukiForm.setFiles(Collections.emptySet());
		
		ecoTruki = this.ecoTrukiService.save(ecoTrukiForm);
		
		assertNotNull(ecoTruki);
		assertTrue(ecoTruki.getId() == ecoTrukiId);
	}
	
	/*
	 * Un administrador elimina un eco truki
	 */
	@WithMockUser("admin1")
	@Transactional
	@Test
	public void positiveTest_delete() {
		EcoTruki ecoTruki;
		EcoTruki deleted;
		int ecoTrukiId;
		
		ecoTrukiId = 800;
		ecoTruki = this.ecoTrukiService.findOne(ecoTrukiId);
		
		this.ecoTrukiService.delete(ecoTruki);
		
		deleted = this.ecoTrukiService.findOne(ecoTrukiId);
		
		assertTrue(deleted == null);
	}
	
	/*
	 * Un administrador trata de eliminar un eco truki que ni
	 * siquiera esta persistido en BD
	 */
	@WithMockUser("admin1")
	@Transactional
	@Test
	public void negativeTest_delete_uno() {
		EcoTruki ecoTruki;
		EcoTruki deleted;

		ecoTruki = this.ecoTrukiService.create();
		
		try {
			this.ecoTrukiService.delete(ecoTruki);
			
			deleted = this.ecoTrukiService.findOne(ecoTruki.getId());
		} catch (IllegalArgumentException e) {
			deleted = null;
		}
		
		assertTrue(deleted == null);
	}
	
	@WithMockUser("admin1")
	@Transactional
	@Test
	public void positiveTest_getPhotosByEcoTruki() {
		Map<Integer, List<String>> results;
		Collection<EcoTruki> ecoTrukis;
		int page;
		
		page = 1;
		ecoTrukis = this.ecoTrukiService.findAll(page).getContent();
		
		results = this.ecoTrukiService.getPhotosByEcoTruki(ecoTrukis);
		
		assertNotNull(results);
		assertTrue(results.size() > 0);
	}
	
	private Set<MultipartFile> getFiles(String name, String contentType) {
		Set<MultipartFile> files;
		MultipartFile file;
		
		file = this.getFile(name, contentType);

		files = new HashSet<MultipartFile>();
		files.add(file);
		
		return files;
	}
	
	private MultipartFile getFile(String name, String contentType) {
		Photo photo;
		int photoId;
		MultipartFile file;
		
		photoId = 4;
		photo = this.photoService.findOne(photoId);
		
		file = new MockMultipartFile(name,
				 				     name,
				 				     contentType,
				 				     photo.getData());
		
		return file;
	}
	
}
