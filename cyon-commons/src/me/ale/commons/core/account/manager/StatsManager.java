package me.ale.commons.core.account.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.League;
import me.ale.commons.core.account.manager.constructor.Account;
import me.ale.commons.core.account.manager.constructor.AccountType;

public class StatsManager extends Account {

	public StatsManager() {
		super("Stats", AccountType.STATS);
	}
	
	public boolean exists(UUID uuid) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `stats` WHERE `uuid` = '" + uuid + "';");
			if(resultSet.next()) {
				return resultSet.getString("uuid") != null;
			}
			resultSet.close();
			return false;
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return true;
		}
	}
	
	public void create(String name, UUID uuid) {
		if(exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("INSERT INTO `stats` (`uuid`, `nickname`, `kills`, `deaths`, `streak`, `exp`, `money`, `league`) VALUES ('" + uuid + "', '" + name + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 1000 + "', '" + League.APPRENTICE.name() + "');");
			return;
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return;
		}
	}
	
	public void delete(UUID uuid) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("DELETE FROM `stats` WHERE `uuid` = '" + uuid + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public int get(UUID uuid, String column) {
		if(!exists(uuid)) {
			return 0;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `stats` WHERE `uuid` = '" + uuid + "';");
			if(resultSet.next()) {
				return resultSet.getInt(column);
			}
			resultSet.close();
			return 0;
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return 0;
		}
	}
	
	public String getString(UUID uuid, String column) {
		if(!exists(uuid)) {
			return null;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `stats` WHERE `uuid` = '" + uuid + "';");
			if(resultSet.next()) {
				return resultSet.getString(column);
			}
			resultSet.close();
			return null;
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return null;
		}
	}
	
	public void set(UUID uuid, String column, int value) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("UPDATE `stats` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
	
	public void set(UUID uuid, String column, League league) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("UPDATE `stats` SET `" + column.toLowerCase() + "` = '" + league.name() + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
}
