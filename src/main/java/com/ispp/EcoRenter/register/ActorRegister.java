package com.ispp.EcoRenter.register;

import javax.persistence.Column;
import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.ispp.EcoRenter.model.DomainEntity;

public class ActorRegister extends DomainEntity{

	//Attributes


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	@Pattern(regexp = "^[^0-9]+$",  message = "Nombre no válido")
	private String name;

	@NotBlank
	@Pattern(regexp = "^[^0-9]+$",  message = "Apellidos no válidos")

	private String surname;

	@Transient
	private String fullname;

	@NotBlank
	@Email
	@Column(unique = true)
	@Pattern(
			regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$",
	    	message = "Email inválido")
	private String email;

	@NotBlank
	@Pattern(

			regexp = "^((\\+[0-9]{1,3}\\ )?([0-9]{1,3}\\ )?([0-9]{4,})?)$",
			message = "Número de teléfono inválido")

	private String telephoneNumber;

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

	private MultipartFile file;


	public ActorRegister() {
		super();
	}

	public ActorRegister(String name, String surname, String email,
			String telephoneNumber, String username) {
		super();

		this.name = name;
		this.surname = surname;
		this.email = email;
		this.telephoneNumber = telephoneNumber;
		this.username = username;
		this.password = "";
		this.passwordMatch = "";
		this.file = null;
	}


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
		this.fullname = this.name + " " + this.surname;

		return this.fullname;
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

	public MultipartFile getFile() {
		return file;
	}


	public void setFile(MultipartFile file) {
		this.file = file;
	}




}
