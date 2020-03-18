package com.ispp.EcoRenter.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.repository.PhotoRepository;

@Service
@Transactional
public class PhotoService {

	private static final Log log = LogFactory.getLog(PhotoService.class);
	
	@Autowired
	private PhotoRepository photoRepository;
	
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
	
	public Photo create() {
		Photo result;
		
		result = new Photo();
		
		return result;
	}
	
	public Photo storeFile(MultipartFile file) {
		String fileName;
		Photo result, photo;
		
		try {
			fileName = StringUtils.cleanPath(file.getOriginalFilename());
			
			photo = new Photo(fileName, file.getContentType(), file.getBytes());
			
			result = this.photoRepository.saveAndFlush(photo);
		} catch (Throwable oops) {
			result = null;
			
			log.info("PhotoService::storeFile -> error al insertar la imagen");
		}
		
		return result;
	}
	
	public Photo save(Photo photo) {
		Assert.notNull(photo, "Foto desconocida");
		Assert.isTrue(photo.getId() >= 0, "Id no valido");
		
		Photo result;
		
		result = this.photoRepository.save(photo);
		this.photoRepository.flush();
		
		log.info("Foto persistida en la BD correctamente.");
		
		return result;
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
	
}
