package com.ispp.EcoRenter.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "valuation")
public class Valuation extends DomainEntity {
 
	private static final long serialVersionUID = 1L;
	
	@Range(min = 0, max = 5)
	private int mark;
	
	@NotNull
	@Past
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
	private Date valuationMoment;
	
	
	public Valuation() {
		super();
	}

	
	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public Date getValuationMoment() {
		return valuationMoment;
	}

	public void setValuationMoment(Date valuationMoment) {
		this.valuationMoment = valuationMoment;
	}
	
}
