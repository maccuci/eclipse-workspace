package com.fuzion.core.bukkit.player.account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.bukkit.player.group.Group;
import com.fuzion.core.bukkit.player.tag.Tag;

import lombok.Getter;

@Getter
public class Account {
	
	private Player player;
	private UUID uuid;
	private String nickname;
	
	private String lastip, ip;
	
	private int xp, coins;
	
	private Group group;
	private Tag tag;
	
	public Account(Player player, String nickname, UUID uuid, int xp, int coins, Group group) {
		this.player = player;
		this.uuid = uuid;
		this.nickname = nickname;
		this.xp = xp;
		this.coins = coins;
		this.group = group;
		this.tag = Tag.MEMBRO;
		this.lastip = player.getAddress().getHostName();
		this.ip = player.getAddress().getHostName();
	}
	
	private boolean exist(String column) {
		if(column.equalsIgnoreCase("groups")) {
			try {
				ResultSet resultSet = BukkitMain.getPlugin().getMysql().getStatement().executeQuery("SELECT * FROM `groups` WHERE `uuid` = '" + uuid + "';");
				if(resultSet.next()) {
					return resultSet.getString("uuid") != null;
				}
				resultSet.close();
				return false;
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				return true;
			}
		} else if(column.equalsIgnoreCase("stats")) {
			try {
				ResultSet resultSet = BukkitMain.getPlugin().getMysql().getStatement().executeQuery("SELECT * FROM `stats` WHERE `uuid` = '" + uuid + "';");
				if(resultSet.next()) {
					return resultSet.getString("uuid") != null;
				}
				resultSet.close();
				return false;
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
				return true;
			}
		}
		return false;
	}
	
	public void create(String column) {
		if(exist(column))
			return;
		switch (column) {
		case "groups":
			try {
				BukkitMain.getPlugin().getMysql().getStatement().execute("INSERT INTO `groups` (`uuid`, `nickname`, `playergroup`, `temporary`, `expire`) VALUES ('" + uuid + "', '" + nickname + "', '" + Group.MEMBRO + "', '" + 0 + "', '" + 0L + "');");
				return;
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
			break;
		case "stats":
			try {
				BukkitMain.getPlugin().getMysql().getStatement().execute("INSERT INTO `stats` (`uuid`, `nickname`, `kills`, `deaths`, `streak`, `xp`, `coins`) VALUES ('" + uuid + "', '" + nickname + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + 0 + "');");
				return;
			} catch (Exception e) {
				System.out.println("Error: " + e.getMessage());
			}
			break;

		default:
			break;
		}
	}
	
	public void clear() {
		player = null;
		lastip = null;
		group = null;
		tag = null;
	}
	
	public boolean hasGroupPermission(Group group) {
		try {
			return getGroup().ordinal() <= group.ordinal();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage() + " Exception: " + e.toString());
			return false;
		}
	}
	
	public void setGroup(Group group) {
		this.group = group;
		try {
			BukkitMain.getPlugin().getMysql().getStatement().execute("UPDATE `groups` SET `" + "playergroup" + "` = '" + group.name() + "' WHERE `uuid` = '" + uuid + "';");
		} catch (SQLException e) {
			e.printStackTrace();
			if(player.isOnline())
				player.kickPlayer("§cAlgo deu errado na tentativa de alterar seu grupo.");
		}
	}
	
	public void setGroup(Group group, long time) {
		this.group = group;
		time = time / 1000;
		try {
			BukkitMain.getPlugin().getMysql().getStatement().execute("UPDATE `groups` SET `" + "playergroup" + "` = '" + group.name() + "' WHERE `uuid` = '" + uuid + "';");
			BukkitMain.getPlugin().getMysql().getStatement().execute("UPDATE `groups` SET `" + "temporary" + "` = '" + 1 + "' WHERE `uuid` = '" + uuid + "';");
			BukkitMain.getPlugin().getMysql().getStatement().execute("UPDATE `groups` SET `" + "expire" + "` = '" + time + "' WHERE `uuid` = '" + uuid + "';");
		} catch (SQLException e) {
			e.printStackTrace();
			if(player.isOnline())
				player.kickPlayer("§cAlgo deu errado na tentativa de alterar seu grupo.");
		}
	}
	
	public void setTag(Tag tag) {
		this.tag = tag;
	}
}
