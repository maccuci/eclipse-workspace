package com.fuzion.core.proxy.manager.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.fuzion.core.master.account.Group;
import com.fuzion.core.proxy.ProxyMain;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.scheduler.BungeeScheduler;

public class GroupManager {
	
	public boolean exists(ProxiedPlayer player) {
		try {
			ResultSet resultSet = ProxyMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + player.getUniqueId() + "';");
			if(resultSet.next()) {
				return resultSet.getString("uuid") != null;
			}
			resultSet.close();
			return false;
		} catch(SQLException exception) {
			System.out.println(exception.getMessage());
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
			return true;
		}
	}
	
	public void create(String name, ProxiedPlayer player) {
		if(exists(player)) {
			return;
		}
		try {
			ProxyMain.getPlugin().getMysqlBackend().getStatement().execute("INSERT INTO `groups` (`uuid`, `nickname`, `playergroup`, `temporary`, `expire`) VALUES ('" + player.getUniqueId() + "', '" + name + "', '" + Group.NORMAL + "', '" + 0 + "', '" + 0L + "');");
			return;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
			return;
		}
	}
	
	public void delete(ProxiedPlayer player) {
		if(!exists(player)) {
			return;
		}
		try {
			ProxyMain.getPlugin().getMysqlBackend().getStatement().execute("DELETE FROM `groups` WHERE `uuid` = '" + player.getUniqueId() + "';");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return;
	}
	
	public String get(ProxiedPlayer player, String column) {
		if(!exists(player)) {
			return null;
		}
		try {
			ResultSet resultSet = ProxyMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + player.getUniqueId() + "';");
			if(resultSet.next()) {
				return resultSet.getString(column);
			}
			resultSet.close();
			return null;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
			return null;
		}
	}
	
	public void set(ProxiedPlayer player, String column, String value) {
		if(!exists(player)) {
			return;
		}
		try {
			ProxyMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `groups` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + player.getUniqueId() + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
		}
	}
	
	public void set(ProxiedPlayer player, String column, int value) {
		if(!exists(player)) {
			return;
		}
		try {
			ProxyMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `groups` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + player.getUniqueId() + "';");
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
		}
	}
	
	public void set(ProxiedPlayer player, String column, long value) {
		if(!exists(player)) {
			return;
		}
		try {
			ProxyMain.getPlugin().getMysqlBackend().getStatement().execute("UPDATE `groups` SET `" + column.toLowerCase() + "` = '" + value + "' WHERE `uuid` = '" + player.getUniqueId() + "';");
		} catch(SQLException exception) {
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
		}
	}
	
	public Group getGroup(ProxiedPlayer player) {
		try {
			return Group.valueOf(get(player, "playergroup"));
		} catch(Exception exception) {
			exception.printStackTrace();
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
			return Group.NORMAL;
		}
	}
	
	public Group getNextRank(ProxiedPlayer player) {
		ArrayList<Group> list = new ArrayList<>();
		for(Group groups : Group.values()) {
			if(!list.contains(groups)) {
				list.add(groups);
			}
		}
		return list.get(getGroup(player).ordinal() + 1);
	}
	
	public Group getLeastRank(ProxiedPlayer player) {
		ArrayList<Group> list = new ArrayList<>();
		for(Group groups : Group.values()) {
			if(!list.contains(groups)) {
				list.add(groups);
			}
		}
		return list.get(getGroup(player).ordinal() - 1);
	}
	
	public boolean hasGroup(ProxiedPlayer player, Group rank) {
		return getGroup(player).equals(rank);
	}
	
	public boolean hasGroupPermission(ProxiedPlayer player, Group group) {
		return getGroup(player).ordinal() <= group.ordinal();
	}
	
	public boolean isTemporary(ProxiedPlayer player) {
		try {
			ResultSet resultSet = ProxyMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + player.getUniqueId() + "';");
			if(resultSet.next()) {
				return resultSet.getBoolean("temporary");
			}
			resultSet.close();
			return false;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
			return true;
		}
	}
	
	public long getExpire(ProxiedPlayer player) {
		if(!isTemporary(player)) {
			return 0L;
		}
		try {
			ResultSet resultSet = ProxyMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + player.getUniqueId() + "';");
			if(resultSet.next()) {
				return resultSet.getLong("expire");
			}
			resultSet.close();
			return 0L;
		} catch(SQLException exception) {
			exception.printStackTrace();
			if(player.isConnected()) {
				player.disconnect(new TextComponent(""));
			}
			return 0L;
		}
	}
	
	public void checkTemporary(ProxiedPlayer player) {
		new BungeeScheduler().schedule(ProxyMain.getPlugin(), new Runnable() {
			
			public void run() {
				if(isTemporary(player)) {
					long expire = Long.valueOf(get(player, "expire"));
					int seconds = (int)((expire - System.currentTimeMillis()) / 1000L);
					if(seconds <= 0) {
						set(player, "playergroup", Group.NORMAL.name());
						set(player, "temporary", 0);
						set(player, "expire", 0L);
						if(player.isConnected()) {
							player.disconnect(new TextComponent(""));
						}
					}
				}
			}
		}, 0, 20, TimeUnit.SECONDS);
	}

}
