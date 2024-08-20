package me.tony.bedwars.data.mysql;

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
			
			connection = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "bedwars", "root", "");
			slave = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "bedwars", "root", "");
			
			executeUpdate("CREATE TABLE IF NOT EXISTS `bedwars`.`global_accounts` (`id` INT NOT NULL AUTO_INCREMENT, `uniqueId` VARCHAR(64) NOT NULL, `nick` VARCHAR(64) NOT NULL, `group` VARCHAR(64) NOT NULL, group_time BIGINT(20) NOT NULL DEFAULT 1, `ip` VARCHAR(15) NOT NULL DEFAULT ' ', PRIMARY KEY (`id`, `uniqueId`)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `bedwars`.`global_translations` (`id` INT NOT NULL AUTO_INCREMENT, `language` INT NOT NULL, `key` VARCHAR(64) NOT NULL, `value` TEXT NOT NULL, PRIMARY KEY (`id`), UNIQUE INDEX `id_UNIQUE` (`id` ASC)) ENGINE = InnoDB;");
			
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
