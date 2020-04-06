package com.ispp.EcoRenter.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

@Entity
@Table(name = "providerDiscountCode")
public class ProviderDiscountCode extends DomainEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Atributos -----------------------------
	private String discountCodes;
	
	@NotBlank(message = "No debe estar en blanco")
	private String name;
		
	@URL(message = "Debe ser una url")
	@NotBlank(message = "No debe estar en blanco")
	@Column(unique = true)
	private String linkPage;	
	
	
	// Constructores --------------------------
	public ProviderDiscountCode() {
		super();
	}


	// Getters y setters -----------------------
	public String getDiscountCodes() {
		return discountCodes;
	}

	public void setDiscountCodes(String discountCodes) {
		this.discountCodes = discountCodes;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLinkPage() {
		return linkPage;
	}

	public void setLinkPage(String linkPage) {
		this.linkPage = linkPage;
	}

}
