package me.tony.commons.core.data.mysql;

import java.sql.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class MySQL {
	
	private static final Executor asyncExecutor = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("Async Thread").build());
	
	private Connection connection, slave;
	
	public boolean openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			connection = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "pvp", "root", "");
			slave = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "pvp", "root", "");
			
			executeUpdate("CREATE TABLE IF NOT EXISTS `pvp`.`global_accounts` (`id` INT NOT NULL AUTO_INCREMENT, `uniqueId` VARCHAR(64) NOT NULL, `nick` VARCHAR(64) NOT NULL, `group` VARCHAR(64) NOT NULL, group_time BIGINT(20) NOT NULL DEFAULT 1, `first_login` BIGINT(20) NOT NULL DEFAULT 1,`last_login` BIGINT(20) NOT NULL DEFAULT 1, `ip` VARCHAR(15) NOT NULL DEFAULT ' ', PRIMARY KEY (`id`, `uniqueId`)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `pvp`.`global_stats` (`uniqueId` VARCHAR(64) NOT NULL, `kills` INT NOT NULL, `deaths` INT NOT NULL, `coins` INT NOT NULL, PRIMARY KEY (`uniqueId`)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `pvp`.`global_ranks` (`id` INT NOT NULL AUTO_INCREMENT,`name` VARCHAR(64) NOT NULL, `tag` VARCHAR(64) NOT NULL, `dafault` TINYINT(1) NOT NULL DEFAULT 0, PRIMARY KEY (`id`, `tag`))");
			
			return true;
		} catch (Exception e) {
			System.out.println("Impossible to create the connection of mysql.");
		}
		return false;
	}
	
	public ResultSet executeQuery(String query) {
		try {
			return getConnection().createStatement().executeQuery(query);
		} catch (Exception exception) {
			System.out.println("Impossible to execute the mysql query (" + query + ").");
		}
		return null;
	}
	
	public boolean executeAsyncUpdate(String update) {
		AtomicBoolean atomicBoolean = new AtomicBoolean(false);
		asyncExecutor.execute(() -> {
			try {
				PreparedStatement statement = getSlaveConnection().prepareStatement(update);
				statement.execute();
				statement.close();
				atomicBoolean.set(true);
			} catch (Exception exception) {
				System.out.println("Impossible to execute a async mysql update (" + update + ").");
			}
		});
		return atomicBoolean.get();
	}

	public boolean executeUpdate(String update) {
		try {
			PreparedStatement statement = getSlaveConnection().prepareStatement(update);
			statement.execute();
			statement.close();
			return true;
		} catch (Exception exception) {
			System.out.println("Impossible to execute a sync mysql update (" + update + ").");
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
