package com.fuzion.core.master.data.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

import com.fuzion.core.bukkit.BukkitMain;

import lombok.Getter;

@Getter
public class Mysql {
	
	private String host, username, password, database;
	private Connection connection;
	private Statement statement;
	
	public Mysql() {
		host = "localhost";
		username = "root";
		database = "fuzioncommons";
		password = "";
	}
	
	public void open() throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver").newInstance();
		BukkitMain.getPlugin().getLogger().info("Conectando ao mysql.");
		connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + 3306 + "/" + database, username,
				password);
		statement = connection.createStatement();
		createTables();
	}
	
	public void close() throws SQLException {
		if(isConnected()) {
			connection.close();
		}
	}
	
	public void createTables() {
		try {
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`stats` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(512) NOT NULL, `kills` INT NOT NULL, `deaths` INT NOT NULL, `streak` INT NOT NULL, `xp` INT NOT NULL, `money` INT NOT NULL, `wins` INT NOT NULL, `crates` INT NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`groups` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `playergroup` VARCHAR(64) NOT NULL, `temporary` BOOLEAN NOT NULL, `expire` LONG NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`kitsfavs` (`uuid` VARCHAR(64) NOT NULL, `kits` VARCHAR(64) NOT NULL) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`clans` (`name` VARCHAR(64) NOT NULL, `owner` VARCHAR(64) NOT NULL, `members` VARCHAR(64) NOT NULL, `kills` INT NOT NULL, `deaths` INT NOT NULL, `money` INT NOT NULL, `rank` VARCHAR(64) NOT NULL, `xp` INT NOT NULL, PRIMARY KEY(`name`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`punish` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `type` VARCHAR(64) NOT NULL, `duration` VARCHAR(64) NOT NULL, `reason` VARCHAR(64) NOT NULL, `staffer` VARCHAR(64) NOT NULL, `date` VARCHAR(64) NOT NULL, `expire` LONG, PRIMARY KEY(`id`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8 AUTO_INCREMENT = 1;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`kits` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `kit` VARCHAR(64) NOT NULL, `duration` VARCHAR(64), `temporary` BOOLEAN NOT NULL, `expire` LONG NOT NULL) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`languages` (`id` INT NOT NULL, `language` VARCHAR(64) NOT NULL, `key` VARCHAR(64) NOT NULL, `value` TEXT NOT NULL) ENGINE = InnoDB;");
			connection.createStatement().executeUpdate("CREATE SCHEMA IF NOT EXISTS `" + database + "` DEFAULT CHARACTER SET utf8 ;");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void execute(String command) {
		if(this.connection == null || this.statement == null) {
			Bukkit.getLogger().severe("Conexao e/ou statement nulo(s).");
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
		if(this.connection == null || this.statement == null) {
			Bukkit.getLogger().severe( "Conexao e/ou statement nulo(s).");
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
	
	public void recallConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
		if (!isConnected()) {
			BukkitMain.getPlugin().getLogger().info("Reconectando ao mysql.");
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + 3306 + "/" + database, username,
					password);
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
