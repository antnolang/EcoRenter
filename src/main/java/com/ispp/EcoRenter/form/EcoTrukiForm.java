package com.ispp.EcoRenter.form;

import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

public class EcoTrukiForm {

	private int id;
	
	@NotBlank(message = "No debe ser blanco")
	private String title;
	
	@NotBlank(message = "No debe ser blanco")
	private String description;
	
	@NotEmpty
	private Set<MultipartFile> files;

	
	public EcoTrukiForm() {
		super();
	}
	
	public EcoTrukiForm(int id, String title, String description) {
		super();
		
		this.id = id;
		this.title = title;
		this.description = description;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<MultipartFile> getFiles() {
		return files;
	}

	public void setFiles(Set<MultipartFile> files) {
		this.files = files;
	}

	
	@Override
	public String toString() {
		return "EcoTrukiForm [id=" + id + ", title=" + title + "]";
	}
	
}
