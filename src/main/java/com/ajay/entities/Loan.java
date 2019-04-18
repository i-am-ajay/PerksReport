package com.ajay.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Formula;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

@Component
@Embeddable
public class Loan {
	@Column(name ="PRINCIPALAMOUNT" ,nullable = false)
	private double loan;
	@Column(name ="INTERESTPAY",nullable = false)
	private Double sgrhInt;
	@Transient
	private Double sbiInt;
	
	@Column(name = "FROMDATE",nullable = false)
	private LocalDate loanMonth;
	
	@Column(name = "EMPLOYEE", nullable = false)
	private String empcode;
	
	@Column(name="loanDR")
	private long loandDr;
	
	@Column(name="LOANTYPE")
	private String loanType;
	
	
	@Formula(value = "(SELECT ISNULL(LOAN.SANCTION,'C') FROM ProdigiousDb..LOAN WHERE LOAN.ID = loanDR)")
	private Character loanStatus;

	public String getEmpcode() {
		return empcode;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public double getLoan() {
		return loan;
	}

	public void setLoan(double loan) {
		this.loan = loan;
	}

	public Double getSgrhInt() {
		return sgrhInt;
	}

	public void setSgrhInt(Double sgrhInt) {
		this.sgrhInt = sgrhInt;
	}

	public Double getSbiInt() {
		return sbiInt;
	}

	public void setSbiInt(Double sbiInt) {
		this.sbiInt = sbiInt;
	}

	public LocalDate getLoanMonth() {
		return loanMonth;
	}

	public void setLoanMonth(LocalDate loanMonth) {
		this.loanMonth = loanMonth;
	}

	public long getLoandDr() {
		return loandDr;
	}

	public void setLoandDr(long loandDr) {
		this.loandDr = loandDr;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}
	
	public Character getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(Character loanStatus) {
		this.loanStatus = loanStatus;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean flag = false;
		Loan loan1 = (Loan)obj;
		if (this == loan1) {
			flag=true;
		}
		if(obj != null) {
			flag = false;
		}
		if(this.getLoandDr() == loan1.getLoandDr()) {
			flag = true;
		}
		return flag;
	}
	
	@Override
	public int hashCode() {
		return (int)this.getLoandDr();
	}
	
	public String toString() {
		return this.getLoanMonth()+" : "+this.getLoanType();
	}
	
	
}
