package com.ispp.EcoRenter.form;

public class RenterForm extends ActorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Atributos -----------------
	
	// Constructores --------------
	public RenterForm(String name, String surname, String email, String
	         telephoneNumber, String username) {
		super(name, surname, email, telephoneNumber, username);
	}


	// Getters y setters -----------
	

	@Override
	public String toString() {
		return "RenterForm [toString()=" + super.toString() + "]";
	}

}
