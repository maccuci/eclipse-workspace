/**
 * Copyright (2019) Txny, this software contains all rights reserved
 * unauthorized copying of this file, via any medium is 
 * strictly prohibited proprietary and confidential.
*/
package me.tony.school.mysql.data;

import java.sql.*;

public class MySQL {
	
	private String host, user, database, password;
	private Connection connection, slave;
	
	public MySQL() {
		host = "localhost";
		user = "root";
		database = "tutorial";
		password = "";
	}

	public boolean openConnection() {
		String url = "jdbc:mysql://" + host + ":" + 3306 + "/" + database + "?autoReconnect=true";
		
		try {
			
			connection = DriverManager.getConnection(url, user, password);
			slave = DriverManager.getConnection(url, user, password);
			
			connection.createStatement().executeUpdate("CREATE SCHEMA IF NOT EXISTS `" + database + "` DEFAULT CHARACTER SET utf8 ;");
			
			connection.createStatement().executeQuery("USE `" + database + "`");
			slave.createStatement().executeQuery("USE `" + database + "`");
			return insertTables();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error MySQL.");
		}
		return false;
	}
	
	public boolean insertTables() {
		try {
			executeUpdate("CREATE TABLE IF NOT EXIST `" + database + "`.`test` (`name` VARCHAR(64) NOT NULL, `money` INT NOT NULL DEFAULT 0, PRIMARY KEY(`name`)) ENGINE = InnoDB;");
			
			System.out.println("All tables was created.");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean executeUpdate(String update) {
		try {
			PreparedStatement statement = getSlaveConnection().prepareStatement(update);
			statement.execute();
			statement.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public Connection getSlaveConnection() {
		return slave;
	}
}
