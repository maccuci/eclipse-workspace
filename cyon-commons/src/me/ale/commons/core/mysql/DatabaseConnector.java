package me.ale.commons.core.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import lombok.Getter;
import me.ale.commons.bukkit.BukkitMain;

@Getter
public class DatabaseConnector {
	
	private String host, username, database, password;
	private Connection connection;
	private Statement statement;
	
	public DatabaseConnector() {
		host = "localhost";
		database = "minecraft";
		username = "root";
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
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`stats` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `kills` INT NOT NULL, `deaths` INT NOT NULL, `streak` INT NOT NULL, `exp` INT NOT NULL, `money` INT NOT NULL, `league` VARCHAR(64) NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`ranks` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `playerrank` VARCHAR(64) NOT NULL, `temporary` BOOLEAN NOT NULL, `expire` LONG NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`factions` (`name` VARCHAR(64) NOT NULL, `owner` VARCHAR(64) NOT NULL, `members` VARCHAR(64) NOT NULL, `kills` INT NOT NULL, `deaths` INT NOT NULL, `money` INT NOT NULL, `message` VARCHAR(64) NOT NULL, PRIMARY KEY(`name`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`punish` (`id` INT UNSIGNED NOT NULL AUTO_INCREMENT, `uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `type` VARCHAR(64) NOT NULL, `duration` VARCHAR(64) NOT NULL, `reason` VARCHAR(64) NOT NULL, `staffer` VARCHAR(64) NOT NULL, `date` VARCHAR(64) NOT NULL, `expire` LONG, PRIMARY KEY(`id`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8 AUTO_INCREMENT = 1;");
			statement.execute("CREATE TABLE IF NOT EXISTS `" + database + "`.`packs` (`uuid` VARCHAR(64) NOT NULL, `nickname` VARCHAR(64) NOT NULL, `type` VARCHAR(64) NOT NULL, `amount` INT NOT NULL, PRIMARY KEY(`uuid`)) ENGINE = InnoDB DEFAULT CHARSET = UTF8;");
		} catch (SQLException e) {
			e.printStackTrace();
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
