package com.ispp.EcoRenter.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "renter")
public class Renter extends Actor {
 
	private static final long serialVersionUID = 1L;
	
	// Atributos ----------------------
	private String iban;


	// Constructores ------------------
	public Renter() {
		super();
		
		
	}
	

	
	// Getters y setters --------------
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}
	
	
	// Asociaciones -------------------

	@NotNull
	@Valid
	@OneToMany
	private Collection<CreditCard> creditCards;


	public Collection<CreditCard> getCreditCards() {
		return creditCards;
	}

	public void setCreditCards(Collection<CreditCard> creditCards) {
		this.creditCards = creditCards;
	}
	
	

		
}
