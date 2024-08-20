/**
 * Copyright (2019) Txny, this software contains all rights reserved
 * unauthorized copying of this file, via any medium is 
 * strictly prohibited proprietary and confidential.
*/
package me.tony.school.format;

import java.text.DecimalFormat;

public class StringFormatter {
	
	private static final String[] formats = new String[] {"", "k", "m", "b", "t"};
	private static final int MAX_LENGTH = 4;
	
	public static String format(double number) {
		String decimalFormart = new DecimalFormat("##0E0").format(number);
		decimalFormart = decimalFormart.replaceAll("E[0-9]", formats[Character.getNumericValue(decimalFormart.charAt(decimalFormart.length() - 1)) / 3]);
		
		while (decimalFormart.length() > MAX_LENGTH || decimalFormart.matches("[0-9]+\\.[a-z]")) {
			decimalFormart = decimalFormart.substring(0, decimalFormart.length()-2) + decimalFormart.substring(decimalFormart.length() - 1);
		}
		return decimalFormart;
	}

}
