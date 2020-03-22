package com.ispp.EcoRenter.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.model.Photo;
import com.ispp.EcoRenter.service.PhotoService;

@SpringBootTest
public class PhotoServiceTest {

	@Autowired
	private PhotoService photoService;
	
	/*
	 * Se persite una foto en BD.
	 */
	@Transactional
	@Test
	public void positiveTest_storeImage_uno() {
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
		
		assertNotNull(photo);
		assertTrue(photo.getId() > 0);
	}
	
	/*
	 * Se persiste una foto en BD.
	 * La foto tiene un valor desconocido.
	 */
	@Transactional
	@Test
	public void negativeTest_storeImage_uno() {
		Photo photo;
		MultipartFile file;
	
		file = null;	
		
		try {
			photo = this.photoService.storeImage(file);
		} catch (IllegalArgumentException e) {
			photo = null;
		}
		
		assertTrue(photo == null);
	}
	
	/*
	 * Se persiste una foto en BD.
	 * La foto realmente es un video.
	 */
	@Transactional
	@Test
	public void negativeTest_storeImage_dos() {
		String name, originalFilename, contentType;
		Photo photo;
		MultipartFile file;
		Path path;
		
		try {
			path = Paths.get("C:\\Users\\alvar\\Pictures\\motor.jpg");
			name = "motor.jpg";
			originalFilename = "motor.jpg";
			contentType = "video/mp3";
			
			file = this.getMultipartFile(name, originalFilename, contentType, path);
			
			photo = this.photoService.storeImage(file);
		} catch (IllegalArgumentException e) {
			photo = null;
		}
		
		assertTrue(photo == null);
	}
	
	@Transactional
	@Test
	public void positveTest_storeImages() {
		String name, originalFilename, contentType;
		List<MultipartFile> files;
		Collection<Photo> photos;
		MultipartFile file;
		Path path;
		
		files = new ArrayList<MultipartFile>();
		
		path = Paths.get("C:\\Users\\alvar\\Pictures\\motor.jpg");
		name = "motor.jpg";
		originalFilename = "motor.jpg";
		contentType = "image/jpg";
		
		file = this.getMultipartFile(name, originalFilename, contentType, path);
		files.add(file);
		
		path = Paths.get("C:\\Users\\alvar\\Pictures\\marker.png");
		name = "marker.png";
		originalFilename = "marker.png";
		contentType = "image/png";
		
		file = this.getMultipartFile(name, originalFilename, contentType, path);
		files.add(file);
		
		photos = this.photoService.storeImages(files);
		
		assertNotNull(photos);
		assertTrue(!photos.isEmpty());
	}
	
	private MultipartFile getMultipartFile(String name, String originalFilename, String contentType, Path path) {
		MultipartFile result;
		byte[] data;
		
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			data = null;
		}
		
		result = new MockMultipartFile(name, originalFilename, contentType, data);
		
		return result;
	}
	
}
