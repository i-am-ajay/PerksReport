package com.ajay.config;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.ajay.doa.DoaFactory;
import com.ajay.doa.LoanDoaImplementation;
import com.ajay.output.CreatePerkExcel;

public class MainApp {
	public static void main(String [] args) {
		LocalDate startPeriod = LocalDate.of(2018, 4, 1);
		LocalDate endPeriod = LocalDate.of(2019, 3, 31);
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		CreatePerkExcel excel = new CreatePerkExcel();
		try {
			excel.getPerksWorkbook();
		} catch (InvalidFormatException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		LoanDoaImplementation imp = (LoanDoaImplementation) context.getBean("doa");
		//imp.getEmployeeLoan(1, "GAA7508",startPeriod , endPeriod, excel);
		
		List<String> empcodeList = imp.getEmployeesWithLoan();
		System.out.println("emp list"+empcodeList.size());
		int rowNum = 1;
		for(String empCode : empcodeList) {
			if(imp.getEmployeeLoan(rowNum, empCode, startPeriod, endPeriod, excel)) {
				rowNum += 1;
			}
		}
		try {
			excel.saveSheet();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
