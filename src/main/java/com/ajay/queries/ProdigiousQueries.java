package com.ajay.queries;

public class ProdigiousQueries {
	public static final String DISTINCT_EMPLOYEES = "SELECT DISTINCT EMPLOYEE FROM Payslip(NOLOCK) WHERE 1=1 AND PAYPEIROD :payperiod";
	public static final String ACTIVE_EMPLOYEES = "SELECT empcode FROM Employee";
}
