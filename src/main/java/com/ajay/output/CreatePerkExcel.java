package com.ajay.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import static com.ajay.globals.LoanHeaderIndex.LOAN_HEADER_INDEX;
import static com.ajay.globals.LoanHeaderIndex.LOAN_INDEX_HEADER;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CreatePerkExcel {
	XSSFWorkbook workbook;
	XSSFSheet sheet;
	public XSSFWorkbook getPerksWorkbook() throws InvalidFormatException, IOException {
		workbook = new XSSFWorkbook();
		sheet = workbook.createSheet("Perks");
		createPerksHeader();
		return workbook;
		
	}
	
	private void createPerksHeader() {
		XSSFRow headerRow = sheet.createRow(0);
		headerRow.createCell(0).setCellValue("SNo");
		headerRow.createCell(1).setCellValue("empCode");
		int count = 2;
		for(String head : LOAN_HEADER_INDEX.keySet()) {
			headerRow.createCell(count).setCellValue("SBI_"+ head.substring(3));
			headerRow.createCell(count+1).setCellValue("SGRH_"+ head.substring(3));
			count += 2;
		}
	}
	
	public boolean insertRecordInSheet(int rowIndex, String empCode, Map<Long,String> loanType, Map<String,Double> loanMap, Map<String,Double> sgrhIntsMap) {
		boolean recordInserted = true;
		XSSFRow row = sheet.createRow(rowIndex);
		//row.createCell(0).setCellValue(empCode);
		boolean loanExists = false;
		for(String loanDr : loanMap.keySet()) {
			System.out.println(loanDr);
			try {
			row.createCell(LOAN_HEADER_INDEX.get(loanDr)).setCellValue(loanMap.get(loanDr));
			row.createCell(LOAN_HEADER_INDEX.get(loanDr)+1).setCellValue(sgrhIntsMap.get(loanDr));
			loanExists = true;
			System.out.println(empCode + "Done Successfully.");
			}
			catch(Exception ex) {
				System.out.println("EmpCode :"+empCode);
				System.out.println("Loan DR :"+loanDr);
				System.out.println("Loan Type :"+loanType.get(loanDr));
				continue;
			}
				
			}
		if(loanExists) {
			row.createCell(0).setCellValue(empCode);
		}
		else {
			System.out.println("^^^Emp code "+empCode);
			sheet.removeRow(row);
			recordInserted = false;
		}
		return recordInserted;
	}
	
	public boolean saveSheet() throws IOException {
		if(workbook != null) {
			String path = "E://test121.xlsx";
			FileOutputStream stream = new FileOutputStream(path);
			workbook.write(stream);
			return true;
		}
		else {
			return false;
		}
		
	}
}
