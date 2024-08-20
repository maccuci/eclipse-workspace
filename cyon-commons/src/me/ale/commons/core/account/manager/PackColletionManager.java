package me.ale.commons.core.account.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.PackType;
import me.ale.commons.core.account.manager.constructor.Account;
import me.ale.commons.core.account.manager.constructor.AccountType;

public class PackColletionManager extends Account {
	
	public PackColletionManager() {
		super("Pack Collection", AccountType.PACK);
	}

	public boolean exists(UUID uuid) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `packs` WHERE `uuid` = '" + uuid + "';");
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
			PackType type = null;
			for(PackType types : PackType.values()) {
				type = types;
			}
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("INSERT INTO `packs` (`uuid`, `nickname`, `type`, `amount`) VALUES ('" + uuid + "', '" + name + "', '" + type + "', '" + 5 + "');");
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
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("DELETE FROM `packs` WHERE `uuid` = '" + uuid + "';");
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
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `packs` WHERE `uuid` = '" + uuid + "';");
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
	
	public void set(UUID uuid, String column, int value) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("UPDATE `packs` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
}
