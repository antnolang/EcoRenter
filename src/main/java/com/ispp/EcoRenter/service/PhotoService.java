package com.ispp.EcoRenter.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
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

	public Collection<Photo> findPhotosBySmallholdingId(int smallholdingId){
		Collection<Photo> result;

		result = this.photoRepository.findPhotosBySmallholdingId(smallholdingId);

		return result;
	}

	private Photo getPhotoByFile(MultipartFile file) {
		Assert.notNull(file, "Fichero no nulo");
		
		Photo result;
		
		String fileName, contentType;
		byte[] bytes;
		
		result = null;
		
		fileName = StringUtils.cleanPath(file.getOriginalFilename());
		contentType = file.getContentType();
		
		if (StringUtils.hasText(fileName) && !contentType.equals("application/octet-stream")) {
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
	
}
