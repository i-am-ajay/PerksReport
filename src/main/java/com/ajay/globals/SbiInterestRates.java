package com.ajay.globals;

import java.util.HashMap;
import java.util.Map;

public class SbiInterestRates {
	static private Map<String,Float> map = new HashMap<>();
	static{
		map.put("9||Car", 9.40F);
		map.put("9||CHILDBIRTH1",14.65F);
		map.put("9||ChildBirth", 14.65F);
		map.put("9||Cycle", 14.65F);
		map.put("9||Festival", 14.65F);
		map.put("9||FUNERAL1", 14.65F);
		map.put("9||Funeral", 14.65F);
		map.put("9||HIGHEREDUCATION", 10.15F);
		map.put("9||HIGHEREDUCATION1", 10.15F);
		map.put("9||HouseBuilding", 8.50F);
		map.put("9||HBSWF", 8.50F);
		map.put("9||MarriageDependent", 14.65F);
		map.put("9||MARRIAGEDEPENDENT1", 14.65F);
		map.put("9||MARRIAGEDEPENDENT2", 14.65F);
		map.put("9||MARRIAGEDEPENDENT3", 14.65F);
		map.put("9||MarriageSelf", 14.65F);
		map.put("9||MarriageSister", 14.65F);
		map.put("9||MARRIAGESISTER1", 14.65F);
		map.put("9||SPECIAL", 11.00F);
		map.put("9||Scooter", 12.65F);
		
	}
	static public void putLoanRate(String loanType, Float interest) {
		map.put(loanType, interest);
	}
	static public Float getLoanRate(String loanType) {
		if(map.containsKey(loanType)) {
			return map.get(loanType);
		}
		return (float)0.0;
	}
	
	public void populateRatesMap() {
		
	}
}
