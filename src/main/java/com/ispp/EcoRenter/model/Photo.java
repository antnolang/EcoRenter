package com.ispp.EcoRenter.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "photo")
public class Photo extends DomainEntity {
 
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String name;
	
	@NotBlank
	private String suffix;
	
	@Lob
	private byte[] data;
	
	public Photo() {
		super();
	}

	public Photo(String name, String suffix, byte[] data) {
		super();
		
		this.name = name;
		this.suffix = suffix;
		this.data = data;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
