package com.ispp.EcoRenter.model;

import java.util.Collection;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "ecoTruki")
public class EcoTruki extends DomainEntity {

	private static final long serialVersionUID = 1L;
	
	// Attributes ----------------------------------
	@NotBlank
	private String title;
	
	@NotBlank
	private String description;
	
	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date moment;
	
	
	// Constructors -------------------------------
	public EcoTruki() {
		super();
	}

	// Getters and setters ------------------------
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


	public Date getMoment() {
		return moment;
	}


	public void setMoment(Date moment) {
		this.moment = moment;
	}
	
	// Associations ------------------------------------
	@NotEmpty
	@OneToMany
	private Collection<Photo> photos;


	public Collection<Photo> getPhotos() {
		return photos;
	}

	public void setPhotos(Collection<Photo> photos) {
		this.photos = photos;
	}
	
	
	
}
