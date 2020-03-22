package com.ispp.EcoRenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Range;
 
@Entity
@Table(name = "customization")
public class Customisation extends DomainEntity {

	private static final long serialVersionUID = 1L;

	// Attributes ---------------------------------
	
	@NotBlank
	@Email
	@Column(unique = true)
	private String email;
	
	private String discountCodes;
	
	@Range(min = 3, max = 8)
	private int silverLevel;
	
	@Min(9)
	private int goldLevel;
	
	// Constructors -----------------------------
	
	public Customisation() {
		super();
	}

	// Getters and setters ----------------------

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDiscountCodes() {
		return discountCodes;
	}


	public void setDiscountCodes(String discountCodes) {
		this.discountCodes = discountCodes;
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
	
}
