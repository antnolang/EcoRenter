package com.ispp.EcoRenter.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.ispp.EcoRenter.security.UserAccount;
 
@Entity
@Table(name = "actor")
public abstract class Actor extends DomainEntity {

	private static final long serialVersionUID = 1L;

	// Attributes ----------------------------------
	
	@NotBlank
	@Pattern(regexp = "^[^0-9]+$")
	private String name;
	
	@NotBlank
	@Pattern(regexp = "^[^0-9]+$")
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
		 message = "Número de teléfono inválido"
	)
	private String telephoneNumber;
	
	// Constructors ------------------------------------
	
	public Actor() {
		super();
	}
	
	// Getters and setters -----------------------------

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
	
	
	// Associations -------------------------------------------
	@NotNull
	@Valid
	@OneToOne(optional = false, cascade = CascadeType.ALL)
	private UserAccount	userAccount;
	
	@Valid
	@OneToOne(optional = true)
	private Photo photo;

	
	public UserAccount getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(final UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public Photo getPhoto() {
		return photo;
	}

	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	
	
	
}
