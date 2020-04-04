package com.ispp.EcoRenter.form;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.Range;

import com.ispp.EcoRenter.model.DomainEntity;
import com.ispp.EcoRenter.model.Renter;

public class CreditCardForm extends DomainEntity {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	// Atributos ----------------------------------
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
	
	@NotNull
	@Valid
	private Renter renter;

	// Constructores -----------------------------
	public CreditCardForm() {
		super();
	}

	public CreditCardForm(int id, int version, String holderName,
						  String brandName, String number,
			              String expirationMonth, String expirationYear,
			              int cvvCode, Renter renter) {
		super();
		
		this.setId(id);
		this.setVersion(version);
		
		this.holderName = holderName;
		this.brandName = brandName;
		this.number = number;
		this.expirationMonth = expirationMonth;
		this.expirationYear = expirationYear;
		this.cvvCode = cvvCode;
		this.renter = renter;
	}
	
	// Getters y setters --------------------------
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

	public Renter getRenter() {
		return renter;
	}

	public void setRenter(Renter renter) {
		this.renter = renter;
	}


	@Override
	public String toString() {
		return "CreditCardForm [holderName=" + holderName + ", brandName=" + brandName + "]";
	}
	
}
