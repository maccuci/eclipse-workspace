package me.ale.pvp.manager.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Getter;

@Getter
public class Database {
	
	private String host, database, user, password;
	private Connection connection;
	private Statement statement;
	
	public Database() {
		setCredentials();
	}
	
	private void setCredentials() {
		host = "localhost";
		database = "minecraft";
		user = "root";
		password = "";
	}
	
	public boolean isNull() {
		return host == null || database == null || user == null || password == null;
	}
	
	public synchronized void openConnection() {
		if(isNull()) {
			System.out.println("Não foi possível criar a conexão com a database.");
			return;
		}
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql//" + host + password + "/" + database + "?autoReconnect=true", user, password);
			statement = connection.createStatement();
			createTables();
			System.out.println("Conexão criada com sucesso.");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createTables() {
		if(connection == null || statement == null) {
			return;
		}
		
		try {
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.stats` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `kills` INT NOT NULL, `deaths` INT NOT NULL, `streak` INT NOT NULL, `exp` INT NOT NULL, `credits` INT NOT NULL, `faction` VARCHAR(64) NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.rank` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `playerrank` VARCHAR(64) NOT NULL, `temporary` BOOLEAN NOT NULL, expire LONG NOT NULL, exp INT NOT NULL, credits INT NOT NULL, faction VARCHAR(64) NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if(connection == null) {
			return;
		}
		try {
			connection.close();
			if(statement != null) {
				statement.close();
			}
			return;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
