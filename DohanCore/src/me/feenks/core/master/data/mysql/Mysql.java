package me.feenks.core.master.data.mysql;

import java.sql.*;
import java.util.logging.Level;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.feenks.core.DohanAPI;
import me.feenks.core.bukkit.BukkitMain;

@Getter
public class Mysql {
	
	private Connection connection;
	private Statement statement;
	private String host, username, password, database;
	
	public Mysql() {
		host = "localhost";
		username = "root";
		database = "core";
		password = "";
	}
	
	public boolean open() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + 3306 + "/" + database, username, password);
			statement = connection.createStatement();
			
			statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS `" + database + "` DEFAULT CHARACTER SET utf8 ;");
			
			statement.executeQuery("USE `" + database + "`;");
		} catch (IllegalAccessException | ClassNotFoundException | InstantiationException | SQLException e) {
		}
		return createTable();
	}
	
	public boolean createTable() {
		boolean created = false;
		long start = System.currentTimeMillis();
		try {
			execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`global_accounts` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `group` VARCHAR(64) NOT NULL, `group_time` LONG NOT NULL, `lastip` VARCHAR(64) NOT NULL, `ip` VARCHAR(64) NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`global_stats` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `rank` VARCHAR(64) NOT NULL, `xp` INT NOT NULL, `money` INT NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`global_permissions` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(512) NOT NULL, `permission` INT NOT NULL, `active` BOOLEAN NOT NULL, `time` LONG NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`global_groups` (`id` INT NOT NULL, `group` VARCHAR(512) NOT NULL, `permissions` VARCHAR(512) NOT NULL, `members` VARCHAR(512) NOT NULL, `default` BOOLEAN NOT NULL, `permission_active` BOOLEAN NOT NULL, PRIMARY KEY(`id`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			created = true;
			DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] The all tables was created.");
			return created;
		} catch (Exception e) {
			DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] Something occurred during the creation of the tables.", e);
		}
		return false;
	}
	
	public synchronized void execute(String command) {
		long start = System.currentTimeMillis();
		if(this.connection == null || this.statement == null) {
			DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] Something relevant to mysql, was not found.");
			Bukkit.getPluginManager().disablePlugin(BukkitMain.getPlugin());
			return;
		}
		try {
			this.statement.execute(command);
		} catch(SQLException exception) {
			exception.printStackTrace();
			return;
		}
	}
	public synchronized ResultSet resultSet(String query) {
		long start = System.currentTimeMillis();
		if(this.connection == null || this.statement == null) {
			DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] Something relevant to mysql, was not found.");
			Bukkit.getPluginManager().disablePlugin(BukkitMain.getPlugin());
			return null;
		}
		try {
			return this.statement.executeQuery(query);
		} catch(SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public void update(String sqlString)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (!isConnected()) {
			recallConnection();
		}
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(sqlString);
		stmt.close();
		stmt = null;
	}

	public boolean recallConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		long start = System.currentTimeMillis();
		if (!isConnected()) {
			DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] The mysql connection crashed, recreating it automatically.");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + 3306 + "/" + database, username,
					password);
			return true;
		}
		return false;
	}
	
	public PreparedStatement prepareStatment(String sql)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		if (!isConnected()) {
			recallConnection();
		}
		return connection.prepareStatement(sql);
	}
	
	public boolean isConnected() throws SQLException {
		if (connection == null)
			return false;
		if (connection.isClosed())
			return false;
		return true;
	}
}
