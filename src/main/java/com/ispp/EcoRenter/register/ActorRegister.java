package com.ispp.EcoRenter.register;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.URL;

import com.ispp.EcoRenter.model.DomainEntity;

public class ActorRegister extends DomainEntity{

	//Attributes


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	@Pattern(regexp = "^[^0-9]+$")
	private String name;

	@NotBlank
	@Pattern(regexp = "^[^0-9]+$")
	private String surname;

	@Transient
	private String fullname;

	@NotBlank
	@Column(unique = true)
	@Email
	private String email;

	@NotBlank
	private String telephoneNumber;

	@URL
	private String image;

	@NotBlank
	@Size(min = 5, max = 35)
	@Column(unique = true)
	private String username;


	@NotBlank
	@Size(min = 5, max = 35)
	private String password;


	@NotBlank
	@Size(min = 5, max = 35)
	private String passwordMatch;

	
	//Getters and setters

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


	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getTelephoneNumber() {
		return telephoneNumber;
	}


	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}


	public String getImage() {
		return image;
	}


	public void setImage(String image) {
		this.image = image;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getPasswordMatch() {
		return passwordMatch;
	}


	public void setPasswordMatch(String passwordMatch) {
		this.passwordMatch = passwordMatch;
	}




}
