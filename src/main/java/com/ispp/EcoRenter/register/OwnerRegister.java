package com.ispp.EcoRenter.register;

import javax.validation.constraints.Min;

public class OwnerRegister extends ActorRegister{

	// Attributes ----------------------------------
	private String iban;


	
	// Constructors --------------------------------
	public OwnerRegister() {
		super();
	}
	
	public OwnerRegister(String name, String surname, String email, 
			             String telephoneNumber, String username,
			             int accumulatedMonths) {
		
		super(name, surname, email, telephoneNumber, username);
	
		this.iban = "";
		
	}
	

	// Getter and setters ------------------------------
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	

}
