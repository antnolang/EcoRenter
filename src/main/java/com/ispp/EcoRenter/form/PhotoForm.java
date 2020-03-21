package com.ispp.EcoRenter.form;

public class PhotoForm {

	private String name;
	private String suffix;
	private String content;
	private String structure;
	
	public PhotoForm() {
		super();
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
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getStructure() {
		return structure;
	}
	
	public void setStructure(String structure) {
		this.structure = structure;
	}

	@Override
	public String toString() {
		return "PhotoForm [name=" + name + ", suffix=" + suffix + "]";
	}
	
}
