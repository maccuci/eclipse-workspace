package com.fuzion.core.master.data;

import java.sql.SQLException;

import com.fuzion.core.master.data.mysql.Mysql;

import lombok.Getter;

public class DataMaster {
	
	@Getter
	private Mysql mysql;
	
	public DataMaster() {
		mysql = new Mysql();
	}
	
	public boolean openConnection() {
		try {
			mysql.open();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return true;
	}
}
