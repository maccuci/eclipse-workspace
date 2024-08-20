/**
 * Copyright (2019) Txny, this software contains all rights reserved
 * unauthorized copying of this file, via any medium is 
 * strictly prohibited proprietary and confidential.
*/
package me.tony.school.mysql;

import me.tony.school.mysql.data.MySQL;

public class MainSQL {
	
	static MySQL mySQL = new MySQL();
	
	public static void main(String[] args) {
		if(mySQL.openConnection()) {
			System.out.print("Connection open.");
		} else {
			System.out.print("Connection not open.");
		}
	}

}
