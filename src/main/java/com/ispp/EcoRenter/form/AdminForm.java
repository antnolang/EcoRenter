package com.ispp.EcoRenter.form;

public class AdminForm extends ActorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public AdminForm(String name, String surname, String email, String
	         telephoneNumber, String username) {
		super(name, surname, email, telephoneNumber, username);
	}


	@Override
	public String toString() {
		return "AdminForm [toString()=" + super.toString() + "]";
	}
	
}
