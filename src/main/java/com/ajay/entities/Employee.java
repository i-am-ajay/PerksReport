package com.ajay.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.CollectionId;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OrderBy;
import org.hibernate.annotations.Type;
import org.springframework.stereotype.Component;

@Component
@Entity
@Table(name = "ProdigiousDB..Employee")
public class Employee {
	/*@GeneratedValue(generator = "emp_id_generator")
	@GenericGenerator(name="emp_id",strategy = "enhanced-sequence", 
		parameters = {
				@org.hibernate.annotations.Parameter(name = "sequence-name", value = "emp_id")
		}
	)*/
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	//private int id;
	@Id
	@Column(unique=true, nullable = false)
	private String empcode;
	@Column(name ="FirstName", nullable = false)
	private String name;
	@Column(name="status")
	private String status;
	
	@ElementCollection
	//@CollectionId(columns = { @Column(name="loan_id") }, generator = "", type = @Type(type = "int"))
	@CollectionTable(name = "ProdigiousDb..LoanHistory", 
		joinColumns = {
				@JoinColumn(name="employee")
		}
	)
	@OrderBy(clause = "fromdate,loantype")
	private List<Loan> empLoans = new ArrayList<>();

	public String getEmpcode() {
		return empcode;
	}

	public void setEmpcode(String empcode) {
		this.empcode = empcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Loan> getEmpLoans() {
		return empLoans;
	}

	public void setEmpLoans(List<Loan> empLoans) {
		this.empLoans = empLoans;
	}
}
