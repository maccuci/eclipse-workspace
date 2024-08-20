package me.ale.commons.core.account.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.commons.CyonAPI;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.Rank;
import me.ale.commons.core.account.manager.constructor.Account;
import me.ale.commons.core.account.manager.constructor.AccountType;

public class RankManager extends Account {

	public RankManager() {
		super("Rank", AccountType.RANK);
	}
	
	public boolean exists(UUID uuid) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid + "';");
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
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("INSERT INTO `ranks` (`uuid`, `nickname`, `playerrank`, `temporary`, `expire`) VALUES ('" + uuid + "', '" + name + "', '" + Rank.NORMAL + "', '" + 0 + "', '" + 0L + "');");
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
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("DELETE FROM `ranks` WHERE `uuid` = '" + uuid + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public String get(UUID uuid, String column) {
		if(!exists(uuid)) {
			return null;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid + "';");
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
	
	public void set(UUID uuid, String column, String value) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("UPDATE `ranks` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
	
	public void set(UUID uuid, String column, int value) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("UPDATE `ranks` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
	
	public void set(UUID uuid, String column, long value) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getDatabaseConnector().getStatement().execute("UPDATE `ranks` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
	
	public Rank getRank(UUID uuid) {
		try {
			return Rank.valueOf(get(uuid, "playerrank"));
		} catch(Exception exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return Rank.NORMAL;
		}
	}
	
	public Rank getNextRank(UUID uuid) {
		ArrayList<Rank> list = new ArrayList<>();
		for(Rank ranks : Rank.values()) {
			if(!list.contains(ranks)) {
				list.add(ranks);
			}
		}
		return list.get(getRank(uuid).ordinal() + 1);
	}
	
	public Rank getLeastRank(UUID uuid) {
		ArrayList<Rank> list = new ArrayList<>();
		for(Rank ranks : Rank.values()) {
			if(!list.contains(ranks)) {
				list.add(ranks);
			}
		}
		return list.get(getRank(uuid).ordinal() - 1);
	}
	
	public boolean hasGroup(UUID uuid, Rank rank) {
		return getRank(uuid).equals(rank);
	}
	
	public boolean hasGroupPermission(UUID uuid, Rank rank) {
		return getRank(uuid).ordinal() <= rank.ordinal();
	}
	
	public boolean isTemporary(UUID uuid) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid + "';");
			if(resultSet.next()) {
				return resultSet.getBoolean("temporary");
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
	
	public long getExpire(UUID uuid) {
		if(!isTemporary(uuid)) {
			return 0L;
		}
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getStatement().executeQuery("SELECT * FROM `ranks` WHERE `uuid` = '" + uuid + "';");
			if(resultSet.next()) {
				return resultSet.getLong("expire");
			}
			resultSet.close();
			return 0L;
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return 0L;
		}
	}
	
	public void checkTemporary(UUID uuid) {
		new BukkitRunnable() {
			public void run() {
				if(isTemporary(uuid)) {
					long expire = Long.valueOf(get(uuid, "expire"));
					int seconds = (int)((expire - System.currentTimeMillis()) / 1000L);
					if(seconds <= 0) {
						set(uuid, "playergroup", Rank.NORMAL.name());
						set(uuid, "temporary", 0);
						set(uuid, "expire", 0L);
						Player player = Bukkit.getPlayer(uuid);
						if(player.isOnline()) {
							player.kickPlayer(CyonAPI.SERVER_PREFIX + "Seu rank foi alterado para Normal novamente.");
						}
					}
				}
			}
		}.runTaskTimer(BukkitMain.getPlugin(), 0L, 20L);
	}
}
