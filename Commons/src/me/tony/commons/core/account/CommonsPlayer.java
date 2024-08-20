package me.tony.commons.core.account;

import java.sql.*;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;
import me.tony.commons.core.Commons;

@Getter
public class CommonsPlayer {
	
	private Player player;
	private String name;
	private UUID uuid;
	
	@Setter private Group group;
	private int group_time;
	
	@Setter private int coins, xp;
	
	private transient String ipAddress = "";
	private String lastIpAddress = "";
	
	private boolean online;
	
	public CommonsPlayer(UUID uuid, String name) {
		this.uuid = uuid;
		this.name = name;
	}
	
	public boolean hasGroupPermission(Group group) {
		return getGroup().ordinal() <= group.ordinal();
	}
	
	public Group getGroup() {
		return group;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setJoinData(String username, String ipAdrress) {
		setName(username);
		this.ipAddress = ipAdrress;
		this.online = true;
	}
	
	public void setGroupTime(int group_time) {
		this.group_time = group_time;
	}
	
	public boolean load() {
		try {
			PreparedStatement accountQuery = Commons.getMySQL().getConnection().prepareStatement("SELECT * FROM `global_accounts` WHERE `uniqueId`=?");
			
			accountQuery.setString(1, uuid.toString());
			
			ResultSet accountData = accountQuery.executeQuery();

			if (accountData.next()) {
				setName(accountData.getString(2));
				setGroup(Commons.getGroupCommon().getGroup(accountData.getInt(3)));
				setGroupTime(accountData.getInt(4));
				
				accountData.close();
			} else {
				this.group = Group.MEMBER;
				this.group_time = -1;
				this.coins = 0;
				this.xp = 0;
				
				PreparedStatement accountInsert = Commons.getMySQL().getConnection().prepareStatement("INSERT INTO `global_accounts` (`uniqueId`, `nick`, `group`, `group_time`) VALUES (?, ?, ?, ?);");
				accountInsert.setString(1, uuid.toString());
				accountInsert.setString(2, name);
				accountInsert.setString(3, Group.MEMBER.name());
				accountInsert.setInt(4, group_time);
				accountInsert.execute();
				accountInsert.close();
			}
			accountQuery.close();
			return true;
		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.SEVERE,"Error when the plugin tried to load the data of player with the uuid: " + uuid + ".", e);
		}
		return false;
	}
	
	public boolean update() {
		return false;
	}
}
