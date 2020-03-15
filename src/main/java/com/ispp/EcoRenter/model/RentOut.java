package com.ispp.EcoRenter.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "rentOut")
public class RentOut extends DomainEntity {
 
	private static final long serialVersionUID = 1L;

	// Attributes ----------------------------------
	
	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date startDate;
	
	@Min(0)
	private int month;
	
	@Transient
	private Date endDate;
	
	// Constructors -------------------------------
	
	public RentOut() {
		super();
	}

	
	// Getters and Setters ------------------------
	
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}
	
	public Date getEndDate() {
		Calendar cal;
		
		cal = Calendar.getInstance();
		cal.setTime(this.startDate);
		cal.add(Calendar.MONTH, this.month);
		
		this.endDate = cal.getTime();
		
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	// Associations ------------------------------
	
	



	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Renter renter;
	
	@NotNull
	@Valid
	@ManyToOne(optional = false)
	private Smallholding smallholding;
	
	@Valid
	@ManyToOne(optional = true)
	private Valuation valuation;


	public Renter getRenter() {
		return renter;
	}

	public void setRenter(Renter renter) {
		this.renter = renter;
	}

	public Smallholding getSmallholding() {
		return smallholding;
	}

	public void setSmallholding(Smallholding smallholding) {
		this.smallholding = smallholding;
	}

	public Valuation getValuation() {
		return valuation;
	}

	public void setValuation(Valuation valuation) {
		this.valuation = valuation;
	}
		
}
