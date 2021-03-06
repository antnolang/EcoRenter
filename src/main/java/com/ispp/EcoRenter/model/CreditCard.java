package com.ispp.EcoRenter.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "creditCard")
public class CreditCard extends DomainEntity {

	private static final long serialVersionUID = 1L;

	// Atributos --------------------------------------
	
	@NotBlank
	@Pattern(
		regexp = "^[^0-9]+$",
		message = "Nombre no válido"
	)
	private String holderName;
	
	@NotBlank
	@Pattern(
		regexp = "^[^0-9]+$",
		message = "Marca no válida"
	)
	private String brandName;
	
	@NotBlank
	@CreditCardNumber
	@Pattern(
		regexp = "^\\d+$",
		message = "Número inválido"
	)
	private String number;
	
	@NotBlank
	@Pattern(
		regexp = "^\\d{1,2}$",		
		message = "Mes inválido"
	)
	private String expirationMonth;
	
	@NotBlank
	@Pattern(
			regexp = "^\\d{2}$",
			message = "Año inválido"
	)
	private String expirationYear;
	
	@Range(min = 100, max = 999)
	private int cvvCode;
	
	// Constructores --------------------------------
	public CreditCard() {
		super();
	}

	
	// Getters y setters ---------------------------
	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getExpirationMonth() {
		return expirationMonth;
	}

	public void setExpirationMonth(String expirationMonth) {
		this.expirationMonth = expirationMonth;
	}

	public String getExpirationYear() {
		return expirationYear;
	}

	public void setExpirationYear(String expirationYear) {
		this.expirationYear = expirationYear;
	}

	public int getCvvCode() {
		return cvvCode;
	}

	public void setCvvCode(int cvvCode) {
		this.cvvCode = cvvCode;
	}
	
	// Asociaciones ---------------------------------------
	
	
}
