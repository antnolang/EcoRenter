package com.ispp.EcoRenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;
 
@Entity
@Table(name = "customization")
public class Customisation extends DomainEntity {

	private static final long serialVersionUID = 1L;

	// Atributos ---------------------------------
	@NotBlank
	@Email
	@Column(unique = true)
	private String email;
		
	@Range(min = 3, max = 8)
	private int silverLevel;
	
	@Min(9)
	private int goldLevel;
	
	@NotBlank
	private String creditCardMakes;
	
	@Min(1)
	private long maxSizePhoto;
	
	@Transient
	private long maxSizePhotoBytes;
	
	// Constructors -----------------------------
	public Customisation() {
		super();
	}

	// Getters y setters ----------------------
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getSilverLevel() {
		return silverLevel;
	}

	public void setSilverLevel(int silverLevel) {
		this.silverLevel = silverLevel;
	}

	public int getGoldLevel() {
		return goldLevel;
	}

	public void setGoldLevel(int goldLevel) {
		this.goldLevel = goldLevel;
	}

	public String getCreditCardMakes() {
		return creditCardMakes;
	}

	public void setCreditCardMakes(String creditCardMakes) {
		this.creditCardMakes = creditCardMakes;
	}

	public long getMaxSizePhoto() {
		return maxSizePhoto;
	}

	public void setMaxSizePhoto(long maxSizePhoto) {
		this.maxSizePhoto = maxSizePhoto;
	}

	public long getMaxSizePhotoBytes() {
		this.maxSizePhotoBytes = this.maxSizePhoto * 1024 * 1024;
		
		return this.maxSizePhotoBytes;
	}

	public void setMaxSizePhotoBytes(long maxSizePhotoBytes) {
		this.maxSizePhotoBytes = maxSizePhotoBytes;
	}
	
	
}
