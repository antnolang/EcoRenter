package com.ispp.EcoRenter.export;

public class ActorExport {
	
	private String name,surname,telephone,username,email,iban;
	
	
	
	public ActorExport(String iban,String name, String surname, String telephone, String username,String email) {
		
		
		this.name = name;
		this.surname = surname;
		this.telephone = telephone;
		this.username = username;
		this.email = email;
		this.iban = iban;
		
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public String getSurname() {
		return surname;
	}



	public void setSurname(String surname) {
		this.surname = surname;
	}



	public String getTelephone() {
		return telephone;
	}



	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getIban() {
		return iban;
	}



	public void setIban(String iban) {
		this.iban = iban;
	}

	
	

}
