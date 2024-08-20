package com.fuzion.core.master.account.management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.master.account.Rank;

public class StatsManager {
	
	public boolean exists(UUID uuid) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `stats` WHERE `uuid` = '" + uuid + "';");
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
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `stats` (`uuid`, `nickname`, `kills`, `deaths`, `streak`, `xp`, `money`, `wins`, `crates`) VALUES ('" + uuid + "', '" + name + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 1000 + "', '" + 0 + "', '" + 0 + "');");
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
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("DELETE FROM `stats` WHERE `uuid` = '" + uuid + "';");
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
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `stats` WHERE `uuid` = '" + uuid + "';");
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
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `stats` WHERE `uuid` = '" + uuid + "';");
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
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `stats` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
	
	public void set(UUID uuid, String column, Rank league) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `stats` SET `" + column.toLowerCase() + "` = '" + league.name() + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}

}
