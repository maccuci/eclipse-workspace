package me.tony.school.person.manager;

public class CpfManager {
	
	private static String calcDig(String num) {
		Integer primDig, segDig;
		int result = 0, amount = 10;
		
		for(int i = 0; i < num.length(); i++) {
			result += Integer.parseInt(num.substring(i, i + 1)) * amount--;
		}
		
		if(result % 11 == 0 | result % 11 == 1) 
			primDig = new Integer(0);
		else
			primDig = new Integer(11 - (result % 11));
		
		result = 0;
		amount = 11;
		
		for(int i = 0; i < num.length(); i++)
			result += Integer.parseInt(num.substring(i, i + 1)) * amount--;
		
		result += primDig.intValue() * 2;
		
		if(result % 11 == 0 | result % 11 == 1)
			segDig = new Integer(0);
		else 
			segDig = new Integer(11 - (result % 11));
		
		return primDig.toString() + segDig.toString();
	}
	
	public static String generateCpf() {
		String initials = "";
		Integer number;
		
		for(int i = 0; i < 9; i++) {
			number = new Integer((int) (Math.random() * 10));
			initials += number.toString();
		}
		return initials + calcDig(initials);
	}
}
