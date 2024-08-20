package com.fuzion.core.master.account.management;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.FuzionAPI;
import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.data.DataType;

public class GroupManager {
	
	public boolean exists(UUID uuid) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + uuid + "';");
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
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `groups` (`uuid`, `nickname`, `playergroup`, `temporary`, `expire`) VALUES ('" + uuid + "', '" + name + "', '" + Group.NORMAL + "', '" + 0 + "', '" + 0L + "');");
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
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("DELETE FROM `groups` WHERE `uuid` = '" + uuid + "';");
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
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + uuid + "';");
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
	
	public void set(UUID uuid, String dataType, String value) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `groups` SET `" + dataType.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
	
	public void set(UUID uuid, String dataType, int value) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `groups` SET `" + dataType.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
	
	public void set(UUID uuid, String dataType, long value) {
		if(!exists(uuid)) {
			return;
		}
		try {
			BukkitMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `groups` SET `" + dataType.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + uuid + "';");
		} catch(SQLException exception) {
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
		}
	}
	
	public Group getGroup(UUID uuid) {
		try {
			return Group.valueOf(get(uuid, "playergroup"));
		} catch(Exception exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return Group.NORMAL;
		}
	}
	
	public Group getNextRank(UUID uuid) {
		ArrayList<Group> list = new ArrayList<>();
		for(Group groups : Group.values()) {
			if(!list.contains(groups)) {
				list.add(groups);
			}
		}
		return list.get(getGroup(uuid).ordinal() + 1);
	}
	
	public Group getLeastRank(UUID uuid) {
		ArrayList<Group> list = new ArrayList<>();
		for(Group groups : Group.values()) {
			if(!list.contains(groups)) {
				list.add(groups);
			}
		}
		return list.get(getGroup(uuid).ordinal() - 1);
	}
	
	public boolean hasGroup(UUID uuid, Group rank) {
		return getGroup(uuid).equals(rank);
	}
	
	public boolean hasGroupPermission(UUID uuid, Group group) {
		return getGroup(uuid).ordinal() <= group.ordinal();
	}
	
	public boolean isTemporary(UUID uuid) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + uuid + "';");
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
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + uuid + "';");
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
						set(uuid, DataType.GLOBAL_PLAYERGROUP.name().replace("GLOBAL_", ""), Group.NORMAL.name());
						set(uuid, DataType.GLOBAL_TEMPORARY.name().replace("GLOBAL_", ""), 0);
						set(uuid, DataType.GLOBAL_EXPIRE.name().replace("GLOBAL_", ""), 0L);
						Player player = Bukkit.getPlayer(uuid);
						if(player.isOnline()) {
							player.kickPlayer("§6§lFUZION §7» §aSeu grupo temporário acabou! Adquira outro em:\n    §e" + FuzionAPI.STORE);
						}
					}
				}
			}
		}.runTaskTimer(BukkitMain.getPlugin(), 0L, 20L);
	}

}
