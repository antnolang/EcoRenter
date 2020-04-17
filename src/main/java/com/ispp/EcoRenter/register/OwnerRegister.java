package com.ispp.EcoRenter.register;

import javax.validation.constraints.Min;

public class OwnerRegister extends ActorRegister{

	

	
	// Constructors --------------------------------
	public OwnerRegister() {
		super();
	}
	
	public OwnerRegister(String name, String surname, String email, 
			             String telephoneNumber, String username,
			             int accumulatedMonths) {
		
		super(name, surname, email, telephoneNumber, username);
	
		
		
	}
	

	
	

}
