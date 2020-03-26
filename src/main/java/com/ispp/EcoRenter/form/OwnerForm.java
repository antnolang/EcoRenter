package com.ispp.EcoRenter.form;

public class OwnerForm extends ActorForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Atributos -------------------------
	private String iban;

	
	// Constructores ----------------------
	public OwnerForm(String name, String surname, String email, String
			         telephoneNumber, String username) {
		super(name, surname, email, telephoneNumber, username);
		
		this.iban = "";
	}

	// Getters y setters -----------------
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	
	@Override
	public String toString() {
		return "OwnerForm [iban=" + iban + "]";
	}
	
}
