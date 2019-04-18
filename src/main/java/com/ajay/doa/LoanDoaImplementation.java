package com.ajay.doa;


import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.persistence.Query;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.NativeQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ajay.entities.Employee;
import com.ajay.entities.Loan;
import com.ajay.globals.SbiInterestRates;
import com.ajay.output.CreatePerkExcel;
import com.ajay.queries.ProdigiousQueries;

@Repository
public class LoanDoaImplementation {
	
	SessionFactory factory;
	public LoanDoaImplementation() {
		
	}
	public LoanDoaImplementation(SessionFactory factory) {
		this.factory = factory;
	}
	
	@Transactional
	public List<String> getEmployeesWithLoan() {
		Session session = factory.openSession();
		
		List<String> empcodeList = session.createQuery(ProdigiousQueries.ACTIVE_EMPLOYEES,String.class).getResultList();
		
		session.close();
		return empcodeList;
	}
	
	@Transactional
	public boolean getEmployeeLoan(int rowNum, String empcode, LocalDate startPeriod, LocalDate endPeriod, CreatePerkExcel excel) {
		boolean hasLoan = false;
		boolean recordInserted = false;
		boolean perksEligibility = false;
		Session session = factory.openSession();
		//loanwise sbi interest map
		Map<String,Double> loanMap = new HashMap<>();
		// Map of loan dr to loan type
		Map<Long,String> loanType = new HashMap<>();
		// Sgrh Int Loan map
		Map<String,Double> sgrhIntMap = new HashMap<>();
		
		
		// get an employee with all his loan details.
		Employee emp = session.get(Employee.class, empcode);
		List<Loan> loan = emp.getEmpLoans();
		if(loan.size()>0) {
			hasLoan = true;
			Stream<Loan> loanStream = null;
			// loop through employee loans details for a financial year
			while(startPeriod.compareTo(endPeriod) < 1) {
				// a lambda requires effectively final variable so create a local copy of start period.
				LocalDate sPeriod = startPeriod;
				// find list of loans in a month
				Stream<Loan> montlyLoanStream = getLoansFromFinancialPeirod(loan.stream(), sPeriod);
				
				perksEligibility = montlyLoanStream.anyMatch(empLoan -> {
					return !(empLoan.getLoanType().equals("9||Cycle") || empLoan.getLoanType().equals("9||Festival")) ? true :false;
				});
				if(perksEligibility) {
					// get list of all loans in given month
					Stream<Loan> monthlyLoans = getLoansOfMonth(loan.stream(),sPeriod);
					monthlyLoans.forEach(monthlyLoan ->{
						System.out.println("Employee :"+monthlyLoan.getEmpcode());
						System.out.println("Loan Dr :"+monthlyLoan.getLoandDr());
						System.out.println("Loan Month"+monthlyLoan.getLoanMonth());
						System.out.println("Sgrh Int :"+monthlyLoan.getSbiInt());
						loanType.put(monthlyLoan.getLoandDr(), monthlyLoan.getLoanType());
						double balance = findLoanBalanceOfMonth(
								getLoansFromFinancialPeirod(loan.stream(), sPeriod),
								monthlyLoan.getLoandDr());
						float interestRate = SbiInterestRates.getLoanRate(monthlyLoan.getLoanType());
					 
						double sbiInt = ((balance * interestRate)/100);
						double sbiIntForMonth = sbiInt/12;
						System.out.println("Loan Dr :"+monthlyLoan.getLoandDr());
						System.out.println("SBI Int For Month :"+sbiIntForMonth);
						System.out.println("--------------------------");
						if(loanMap.containsKey(monthlyLoan.getLoanType())) {
							//System.out.println(monthlyLoan.getLoandDr()+" Found");
							loanMap.put(monthlyLoan.getLoanType(),loanMap.get(monthlyLoan.getLoanType())+sbiIntForMonth);
							sgrhIntMap.put(monthlyLoan.getLoanType(), sgrhIntMap.get(monthlyLoan.getLoanType())+monthlyLoan.getSgrhInt());
						}
						else {
							//System.out.println(monthlyLoan.getLoandDr()+" Not Found");
							loanMap.put(monthlyLoan.getLoanType(), sbiIntForMonth);
							sgrhIntMap.put(monthlyLoan.getLoanType(), monthlyLoan.getSgrhInt());
							}
						});	
				}
				startPeriod = startPeriod.plusMonths(1);
			}
		}
		session.close();
		
		if(hasLoan) {
			System.out.println("Emp Code :"+empcode +" -> "+ loanMap.size());
			System.out.println("-----------");
			recordInserted = excel.insertRecordInSheet(rowNum, empcode, loanType, loanMap, sgrhIntMap);
			for(String l : loanMap.keySet()) {
				System.out.println(l +"->"+loanMap.get(l));
			} 
		}
		return recordInserted;
	}
	
	// get loans from beginning of financial period upto last EMI in a stream
	private Stream<Loan> getLoansFromFinancialPeirod(Stream<Loan> loanStream,LocalDate date){
		return loanStream.filter(e ->{
			/*
			System.out.println("Loan "+e.getLoandDr());
			System.out.println("Loan "+e.getLoanMonth());
			System.out.println("Loan"+e.getLoanStatus());*/
			int i = e.getLoanMonth().compareTo(date);
			Character status = 'C';
			try {
				status = e.getLoanStatus();
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
			return i >= 0 && (status == 'S' || status == 's') ? true : false;
			//return i >= 0 ? true : false;
		});
	}
	
	// get all loans emis deducted in a given month. 
	private Stream<Loan> getLoansOfMonth(Stream<Loan> loanStream, LocalDate date){
		return loanStream.filter(e -> {
			int i = e.getLoanMonth().compareTo(date);
			Character status = e.getLoanStatus();
			return i == 0 && (status == 'S' || status == 's') ? true : false;
			//return i == 0 ? true : false;
		});
	}
	
	// Takes a loan stream and find the balance of principal of a particular loan by summing all emis from beginning to end month. 
	private double findLoanBalanceOfMonth(Stream<Loan> stream, long loanDr) {
		double amount = 0.0;
		amount = stream.
				filter(e -> e.getLoandDr()==loanDr).
				mapToDouble(e -> e.getLoan()).sum();
		return amount;
	}
	
	public void closeFactory() {
		if(factory != null) {
			factory.close();
		}
		else {
			System.out.println("Factory instance is not open.");
		}
	}
}
