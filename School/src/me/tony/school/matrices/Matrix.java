/**
 * Copyright (2019) Txny, this software contains all rights reserved
 * unauthorized copying of this file, via any medium is 
 * strictly prohibited proprietary and confidential.
*/
package me.tony.school.matrices;

public class Matrix {
	
	public static void main(String[] args) {
		//First value = amount of fruit | Second value = price
		double[][] fruitsPrice = new double[2][3];
		
		fruitsPrice[0][0] = 7.5;
		fruitsPrice[0][1] = 9;
		fruitsPrice[0][2] = 6.5;
		
		fruitsPrice[1][0] = 10;
		fruitsPrice[1][1] = 5;
		fruitsPrice[1][2] = 2;
		
		for(int i = 0; i < fruitsPrice.length; i++) {
			for(int j = 0; j < fruitsPrice[i].length; j++) {
				System.out.println(fruitsPrice[i][j] + " ");
			}
		}
	}
}
