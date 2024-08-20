package me.tony.commons.core.data.backend.mysql;

import java.sql.*;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import lombok.Getter;
import me.tony.commons.core.Commons;
import me.tony.commons.core.data.backend.Database;

public class MySQL implements Database {
	
	private static final Executor asyncExecutor = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("Async Thread").build());
	
	@Getter
	private Connection connection, slave;
	
	@Override
	public boolean openConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			connection = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "commons", "root", "");
			slave = DriverManager.getConnection("jdbc:mysql://" + "localhost" + ":" + 3306 + "/" + "commons", "root", "");
			
			executeUpdate("CREATE TABLE IF NOT EXISTS `commons`.`global_groups` (`id` INT NOT NULL, `name` VARCHAR(64) NOT NULL, PRIMARY KEY(`id`, `name`)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `commons`.`global_tags` (`id` INT NOT NULL, `name` VARCHAR(64) NOT NULL, `group` VARCHAR(64) NOT NULL, PRIMARY KEY(`id`, `name`)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `commons`.`global_permissions` (`id` INT NOT NULL AUTO_INCREMENT, `permission` VARCHAR(32) NOT NULL, `owner` VARCHAR(64) NOT NULL, `group` INT NOT NULL DEFAULT 1, `active` INT NOT NULL DEFAULT 0, `time` BIGINT(20) NOT NULL DEFAULT 1,  PRIMARY KEY (`id`), UNIQUE INDEX `id_UNIQUE` (`id` ASC)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `commons`.`global_accounts` (`id` INT NOT NULL AUTO_INCREMENT, `uniqueId` VARCHAR(64) NOT NULL, `nick` VARCHAR(64) NOT NULL, `group` VARCHAR(64) NOT NULL, `group_time` BIGINT(20) NOT NULL DEFAULT 1, PRIMARY KEY (`id`, `uniqueId`), UNIQUE INDEX `id_UNIQUE` (`id` ASC)) ENGINE = InnoDB;");
			executeUpdate("CREATE TABLE IF NOT EXISTS `commons`.`servers` (`id` INT NOT NULL, `name` VARCHAR(64) NOT NULL, `motd` TEXT NOT NULL, `players` INT NOT NULL, `online` BOOLEAN NOT NULL, PRIMARY KEY(`id`, `name`)) ENGINE = InnoDB;");
			
			return true;
		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.INFO, e, "Impossible to create the connection of mysql.");
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
				PreparedStatement statement = getSlave().prepareStatement(update);
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
			PreparedStatement statement = getSlave().prepareStatement(update);
			statement.execute();
			statement.close();
			return true;
		} catch (Exception exception) {
			System.out.println("Impossible to execute a sync mysql update (" + update + ").");
		}
		return false;
	}
	
	@Override
	public void recallConnection() {
		if(connection == null)
			return;
	}
}
