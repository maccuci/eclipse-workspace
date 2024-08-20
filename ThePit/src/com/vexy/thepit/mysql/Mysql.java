package com.vexy.thepit.mysql;

import lombok.Getter;

import java.sql.*;

public class Mysql {
	
	private String hostname;
    private String port;
    private String database;
    private String username;
    private String password;

    @Getter private Connection connection;

    public Mysql(String hostname, String port, String database, String username, String password) {
        this.hostname = hostname;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
    }
    
    public Connection start() {
    	try {
			Class.forName("com.mysql.jdbc.Driver");
			return connection = DriverManager.getConnection("jdbc:mysql://" + hostname + ":" + port + "/" + database, username, password);
        } catch (SQLException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
    	return connection;
    }
    
    public void closeConnection() {
        try {
            if (connection.isClosed())
                connection.close();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public PreparedStatement query(String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return preparedStatement;
    }

    public void createTables() {
        try {
            connection.createStatement().execute("CREATE TABLE IF NOT EXISTS `" + database + "`.thepit_stats(id INT AUTO_INCREMENT NOT NULL, uuid VARCHAR(36), level INT, xp INT, prestige INT, gold DOUBLE, PRIMARY KEY(id));");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
