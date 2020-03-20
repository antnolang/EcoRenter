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

	public Photo create() {
		Photo result;

		result = new Photo();

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
		Assert.notNull(file, "Fichero no nulo");

		String fileName, contentType;
		byte[] bytes;
		Photo result, photo;

		fileName = StringUtils.cleanPath(file.getOriginalFilename());
		contentType = file.getContentType();
		
		Assert.isTrue(contentType.startsWith("image/"), "No es una imagen");
		
		try {
			bytes = file.getBytes();

			photo = new Photo(fileName, contentType, bytes);

			result = this.photoRepository.saveAndFlush(photo);
		} catch (Throwable oops) {
			result = null;

			log.info("PhotoService::storeFile -> error al insertar la imagen");
		}

		return result;
	}

	public Collection<Photo> storeImages(Collection<MultipartFile> files) {
		Assert.notNull(files, "Coleccion no nula");
		Assert.notEmpty(files, "La coleccion no debe estar vacia");

		List<Photo> results;
		Photo photo;

		results = new ArrayList<Photo>();
		for (MultipartFile file : files) {
			photo = this.storeImage(file);

			if (photo != null) {
				results.add(photo);
			}
			
		}

		return results;
	}

	public Photo save(Photo photo) {
		Assert.notNull(photo, "Foto desconocida");
		Assert.isTrue(photo.getId() >= 0, "Id no valido");

		Photo result;

		result = this.photoRepository.saveAndFlush(photo);

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
