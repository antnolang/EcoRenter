package com.ispp.EcoRenter.register;

import javax.validation.constraints.Min;

public class OwnerRegister extends ActorRegister{

	//Attributes

	private String iban;
	
	@Min(0)
	private int accumulatedMonths;

	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public int getAccumulatedMonths() {
		return accumulatedMonths;
	}

	public void setAccumulatedMonths(int accumulatedMonths) {
		this.accumulatedMonths = accumulatedMonths;
	}
	
	




}
