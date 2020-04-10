package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.HtmlUtils;

import com.ispp.EcoRenter.form.EcoTrukiForm;
import com.ispp.EcoRenter.model.EcoTruki;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.repository.EcoTrukiRepository;

@Service
@Transactional
public class EcoTrukiService {

	// Managed repository ------------------------
	@Autowired
	private EcoTrukiRepository ecoTrukiRepository;
	
	
	// Supported services -----------------------
	@Autowired
	private PhotoService photoService;
	
	@Autowired
	private UtilityService utilityService;
	
	public EcoTrukiService() {
		super();
	}
	
	
	// CRUD methods -----------------------------
	public EcoTruki findOne(int ecoTrukiId) {
		Assert.isTrue(ecoTrukiId > 0, "Valor desconocido");
		Assert.isTrue(this.utilityService.hasAccessToExclusiveContent(), "No tiene acceso");
		
		Optional<EcoTruki> ecoTruki;
		EcoTruki result;
		
		ecoTruki = this.ecoTrukiRepository.findById(ecoTrukiId);
		
		result = ecoTruki.orElse(null);
		
		return result;
	}
	
	public Page<EcoTruki> findAll(int page) {
		Page<EcoTruki> results;
		Pageable pageable;
		Sort sort;
		
		sort = Sort.by(Direction.DESC, "moment");
		pageable = PageRequest.of(page-1, UtilityService.LIMIT, sort);
		
		results = this.ecoTrukiRepository.findAll(pageable);
		
		return results;
	}
	
	public EcoTruki create() {
		EcoTruki result;
		
		result = new EcoTruki();
		result.setMoment(this.utilityService.getCurrentMoment());
		result.setPhotos(Collections.emptySet());
		
		return result;
	}
	
	public EcoTruki save(EcoTruki ecoTruki, Set<MultipartFile> files) {
		Assert.notNull(ecoTruki, "No se pudo completar la operación");
		
		Collection<Photo> photos;
		EcoTruki result;
		
		if (!files.isEmpty() && this.photoService.isNotDefaultFile(files)) {
			photos = this.photoService.storeImages(files);
			
			ecoTruki.setPhotos(photos);
		}
		
		result = this.ecoTrukiRepository.saveAndFlush(ecoTruki);
		
		return result;
	}
	
	public EcoTruki save(EcoTrukiForm ecoTrukiForm) {
		Assert.notNull(ecoTrukiForm, "No se pudo completar la operación");
		
		EcoTruki ecoTruki, result;
		String title, description;
		int id;
		
		id = ecoTrukiForm.getId();
		title = HtmlUtils.htmlEscape(ecoTrukiForm.getTitle().trim());
		description = HtmlUtils.htmlEscape(ecoTrukiForm.getDescription().trim());
		
		ecoTruki = (id <= 0) ? this.create() : this.findOne(id);
		ecoTruki.setTitle(title);
		ecoTruki.setDescription(description);
		
		if (id <= 0) {
			ecoTruki.setMoment(this.utilityService.getCurrentMoment());
		}
		
		// Persistimos en BD
		result = this.save(ecoTruki, ecoTrukiForm.getFiles());
		
		return result;
	}
	
	public void delete(EcoTruki ecoTruki) {
		Assert.notNull(ecoTruki, "Debe ser un valor conocido");
		Assert.isTrue(ecoTruki.getId() > 0, "Debe estar persitido en BD");
		
		// Borramos EcoTruki::photos
		this.photoService.deleteAll(ecoTruki.getPhotos());
		
		this.ecoTrukiRepository.delete(ecoTruki);
	}
	
	// Other business methods -------------------
	public Map<Integer, List<String>> getPhotosByEcoTruki(Collection<EcoTruki> ecoTrukis) {
		Map<Integer, List<String>> results;
		List<Photo> photos;
		List<String> photoAttrs;
		Photo mainPhoto;
		
		results = new HashMap<Integer, List<String>>();
		
		for (EcoTruki ecoTruki: ecoTrukis) {
			photos = new ArrayList<Photo>(ecoTruki.getPhotos());
			mainPhoto = photos.get(UtilityService.MAIN_PHOTO);
			
			photoAttrs = this.photoService.getAttrs(mainPhoto);
			
			results.put(ecoTruki.getId(), photoAttrs);
		}
		
		return results;
	}
	
}
