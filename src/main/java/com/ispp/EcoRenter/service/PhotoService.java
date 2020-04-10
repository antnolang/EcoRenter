package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.model.Customisation;
import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.repository.PhotoRepository;

@Service
@Transactional
public class PhotoService {

	private static final Log log = LogFactory.getLog(PhotoService.class);
	
	@Autowired
	private PhotoRepository photoRepository;

	@Autowired
	private CustomisationService customisationService;
	
	public PhotoService() {
		super();
	}

	public Photo findOne(int photoId) {
		Optional<Photo> optionalPhoto;
		Photo result;

		optionalPhoto = this.photoRepository.findById(photoId);

		if (optionalPhoto.isPresent()) {
			result = optionalPhoto.get();
		} else {
			result = null;
		}

		return result;
	}

	public String getImageBase64(Photo photo) {
		String result;
		byte[] data;
		
		data = photo.getData();
		result = Base64.getEncoder().encodeToString(data);
		
		return result;
	}
		
	public Photo storeImage(MultipartFile file) {
		Photo result, photo;
		
		photo = this.getPhotoByFile(file);
		
		result = (photo != null)
					? this.photoRepository.saveAndFlush(photo)
					: null;
		
		return result;
	}

	public Collection<Photo> storeImages(Collection<MultipartFile> files) {
		Assert.notNull(files, "Coleccion no nula");
		Assert.notEmpty(files, "La coleccion no debe estar vacia");
		 
		List<Photo> photos, results;
		Photo photo;

		results = new ArrayList<Photo>();
		photos = new ArrayList<Photo>();
		
		for (MultipartFile file : files) {
			photo = this.getPhotoByFile(file);
			
			if (photo != null) {
				photos.add(photo);
			}
		}
		
		if (!photos.isEmpty()) {
			results = this.photoRepository.saveAll(photos);
			this.photoRepository.flush();
		}

		return results;
	}

	public void delete(Photo photo) {
		Assert.notNull(photo, "Foto desconocida");
		Assert.isTrue(photo.getId() > 0, "Id no valido");

		this.photoRepository.delete(photo);
		log.info("Foto eliminada de la BD correctamente");

	}

	public void delete(int photoId) {
		Assert.notNull(photoId > 0, "Id no válido");

		this.photoRepository.deleteById(photoId);
	}

	public void deleteAll(Collection<Photo> photos) {
		Assert.notEmpty(photos, "No debe estar vacio");
		
		this.photoRepository.deleteAll(photos);
	}
	
	public Collection<Photo> findPhotosBySmallholdingId(int smallholdingId){
		Collection<Photo> result;

		result = this.photoRepository.findPhotosBySmallholdingId(smallholdingId);

		return result;
	}

	private Photo getPhotoByFile(MultipartFile file) {
		Assert.notNull(file, "Fichero no nulo");
		
		Photo result;
		Customisation custo;
		String fileName, contentType;
		byte[] bytes;
		
		result = null;
		
		fileName = StringUtils.cleanPath(file.getOriginalFilename());
		contentType = file.getContentType();
		
		custo = this.customisationService.find();
		
		if (this.isNotDefaultFile(file)) {		
			Assert.isTrue(file.getSize() < custo.getMaxSizePhotoBytes(),
						  "La imagen supera el tamaño máximo");
			Assert.isTrue(contentType.startsWith("image/"), "No es una imagen");
			
			try {
				bytes = file.getBytes();

				result = new Photo(fileName, contentType, bytes);
			} catch (Throwable oops) {
				result = null;
				
				log.info("PhotoService::getPhotoByFile -> No se pudo recuperar la foto");
			}
		}
		
		return result;
	}
	
	public Photo getPhotoById(int id) {
		return this.photoRepository.findPhotoById(id);
	}
	
	public boolean isNotDefaultFile(MultipartFile file) {
		boolean result;
		String filename;
		String contentType;
		
		filename = StringUtils.cleanPath(file.getOriginalFilename());
		contentType = file.getContentType();
			
		result = StringUtils.hasText(filename) && !contentType.equals("application/octet-stream");
		
		return result;
	}
	
	public boolean isNotDefaultFile(Collection<MultipartFile> files) {
		List<MultipartFile> ls_files;
		MultipartFile file;
		boolean result;
		
		ls_files = new ArrayList<MultipartFile>(files);
		file = ls_files.get(0);
		
		result = this.isNotDefaultFile(file);
		
		return result;
	}
	
	public List<String> getAttrs(Photo photo) {
		List<String> results;
		
		results = new ArrayList<String>();
		results.add(photo.getName());
		results.add(photo.getSuffix());
		results.add(this.getImageBase64(photo));
		
		return results;
	}
	
	public Map<Integer,String> getEncodedDataByPhoto(Collection<Photo> photos) {
		Map<Integer,String> results;
		String encodedData;
		
		results = new HashMap<Integer, String>();
		
		for (Photo photo: photos) {
			encodedData = this.getImageBase64(photo);
			
			results.put(photo.getId(), encodedData);
		}
		
		return results;
	}

	public Collection<Photo> findPhotosByEcoTrukiId(int id) {
		Collection<Photo> result;

		result = this.photoRepository.findPhotosByEcoTrukiId(id);

		return result;
	
	}
	
}
