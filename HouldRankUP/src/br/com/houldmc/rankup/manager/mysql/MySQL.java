package br.com.houldmc.rankup.manager.mysql;

import java.sql.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.Getter;

public class MySQL {
	
	private static final Executor asyncExecutor = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("Async Thread").build());
	
	@Getter
	private Connection connection, slaveConnection;
	
	public boolean openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			connection = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "rankup", "root", "");
			slaveConnection = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "rankup", "root", "");
			
			executeUpdate("CREATE TABLE IF NOT EXISTS `rankup`.`players` (`uniqueId` VARCHAR(64) NOT NULL, `nick` VARCHAR(64) NOT NULL, `group` VARCHAR(64) NOT NULL, `group_temporary` BOOLEAN NOT NULL, `group_expire` LONG NOT NULL, `rank` VARCHAR(64) NOT NULL, `money` DOUBLE NOT NULL, `cash` INT NOT NULL, `souls` INT NOT NULL, PRIMARY KEY(`nick`, `uniqueId`)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `rankup`.`punish` (`id` INT NOT NULL AUTO_INCREMENT, `punished` VARCHAR(64) NOT NULL, `staff` VARCHAR(64) NOT NULL, `start` BIGINT(20) NOT NULL DEFAULT 1, `expire` BIGINT(20) NOT NULL DEFAULT 1, `motive` TEXT(256) NOT NULL, `active` TINYINT(1) NOT NULL DEFAULT 0, `type` TINYINT(10) NOT NULL DEFAULT 0, PRIMARY KEY (`id`, `punished`, `staff`)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `rankup`.`clans` (`owner` VARCHAR(64) NOT NULL, `name` VARCHAR(64) NOT NULL, `tag` VARCHAR(64) NOT NULL, `members` VARCHAR(64) NOT NULL, `money` INT NOT NULL DEFAULT 0, PRIMARY KEY (`owner`, `name`, `tag`)) ENGINE = InnoDB;");
			
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
	
	public boolean execute(String sql) {
		try {
			Statement statement = getConnection().createStatement();
			
			return statement.execute(sql);
		} catch (Exception e) {
		}
		return false;
	}
}
