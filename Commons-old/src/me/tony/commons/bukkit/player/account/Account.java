package me.tony.commons.bukkit.player.account;

import java.sql.*;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.tony.commons.bukkit.BukkitMain;
import me.tony.commons.bukkit.player.account.type.StatsType;

public class Account {
	
	private Player player;
	private String nickname;
	private UUID uniqueId;
	
	private int id, kills, deaths, coins, xp, doublexp;
	
	private long doubleStart;
	private long doubleEnd;
	
	private boolean doubleRunning;
	
	public Account(Player player) {
		this.player = player;
		this.uniqueId = player.getUniqueId();
	}
	
	public void load(boolean loaded) {
		this.nickname = "";
		this.doubleRunning = false;
		
		if(!loaded) {
			System.out.println("The boolean loaded, it false. Don't loading account.");
		}
		
		try {
			Connection mainConnection = BukkitMain.getPlugin(BukkitMain.class).getCoreManager().getDataManager().getMySQL().getConnection();
			
			PreparedStatement accountQuery = mainConnection.prepareStatement("SELECT * FROM `global_accounts` WHERE `uniqueId`=?");
			accountQuery.setString(1, uniqueId.toString());
			
			ResultSet accountData = accountQuery.executeQuery();
			
			if(accountData.next()) {
				this.id = accountData.getInt(1);
				this.nickname = accountData.getString(2);
				
				accountData.close();
				accountQuery.close();
			} else {
				this.coins = 0;
				this.xp = 0;
				
				PreparedStatement accountInsert = mainConnection.prepareStatement("INSERT INTO `global_accounts` (`uniqueId`, `nick`, `group`, `group_time`, `first_login`, `last_login`) VALUES (?, ?, ?, ?, ?, ?);");
				
				accountInsert.setString(1, uniqueId.toString());
				accountInsert.setString(2, nickname);
				accountInsert.setString(3, "Normal");
				accountInsert.execute();
				accountInsert.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public UUID getUniqueId() {
		return uniqueId;
	}
	
	public int getId() {
		return id;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public int getKills() {
		return kills;
	}
	
	public int getDeaths() {
		return deaths;
	}
	
	public int getCoins() {
		return coins;
	}
	
	public int getXp() {
		return xp;
	}
	
	public int getDoublexp() {
		return doublexp;
	}
	
	public long getDoubleStart() {
		return doubleStart;
	}
	
	public long getDoubleEnd() {
		return doubleEnd;
	}
	
	public boolean isDoubleRunning() {
		return doubleRunning;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
	
	public void setDeaths(int deaths) {
		this.deaths = deaths;
	}
	
	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public void setDoubleStart(long doubleStart) {
		this.doubleStart = doubleStart;
	}
	
	public void setDoubleEnd(long doubleEnd) {
		this.doubleEnd = doubleEnd;
	}
	
	public void setDoubleRunning(boolean doubleRunning) {
		this.doubleRunning = doubleRunning;
	}
	
	public void updateKills(StatsType statsType) {
		if(statsType == StatsType.PVP) {
			kills++;
		} else if(statsType == StatsType.HG) {
			kills++;
		}
	}
}
