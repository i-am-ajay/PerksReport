package com.ajay.doa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class DoaFactory {
	@Autowired
	static LoanDoaImplementation loan;
	
	public static LoanDoaImplementation getDoa() {
		return loan;
	}
}
