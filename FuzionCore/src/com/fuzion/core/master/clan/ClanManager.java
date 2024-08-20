package com.fuzion.core.master.clan;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.bukkit.BukkitMain;

public class ClanManager {
	
	private List<Clan> clans = new ArrayList<>();
	
	public ClanManager() {
		setup();
		loadTop10Clan();
		loadTop10Play();
	}
	
	public void setup() {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getConnection().createStatement().executeQuery("SELECT * FROM `clans`");
			while(resultSet.next()) {
				String name = resultSet.getString("name");
				String owner = resultSet.getString("owner");
				String tag = resultSet.getString("tag");
				String member = resultSet.getString("members");
				String rank = resultSet.getString("rank");
				int kills = resultSet.getInt("kills");
				int deaths = resultSet.getInt("deaths");
				int money = resultSet.getInt("money");
				int xp = resultSet.getInt("xp");
				List<String> members = new ArrayList<>();
				for(String m : member.split(", ")) {
					members.add(m);
				}
				clans.add(new Clan(name, tag, owner, rank, members, kills, deaths, xp, money));
			}
		} catch (Exception e) {}
	}
	
	public boolean createClan(String name, String tag, String owner, String member) {
		boolean err = false;
		try {
			PreparedStatement statement = BukkitMain.getPlugin().getMysqlBackend().getConnection().prepareStatement("INSERT INTO `clans` (`name`, `owner`, `members`, `kills`, `deaths`, `money`, `rank`, `xp`) VALUES ('" + name + "', '" + owner + "', '" + member + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + "INITIAL" + "', '" + 0 + "');");
			statement.execute();
			statement.close();
			List<String> members = new ArrayList<>();
			members.add(member);
			clans.add(new Clan(name, tag, owner, "initial", members, 0, 0, 0, 0));
			err = true;
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		
		return err;
	}
	
	public boolean hasClan(String name) {
		boolean exist = false;
		try {
			PreparedStatement stmt = BukkitMain.getPlugin().getMysqlBackend().getConnection().prepareStatement("SELECT name FROM `clans` WHERE `name`='" + name + "';");
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
			exist = true;
			}
			result.close();
			stmt.close();
		} catch (SQLException e) {}
		return exist;
	}
	
	public boolean hasPlayerClan(Player player) {
		boolean exist = false;
		try {
			PreparedStatement stmt = BukkitMain.getPlugin().getMysqlBackend().getConnection().prepareStatement("SELECT name FROM `clans` WHERE `members`='" + player.getName() + "';");
			ResultSet result = stmt.executeQuery();
			if(result.next()) {
			exist = true;
			}
			result.close();
			stmt.close();
		} catch (SQLException e) {}
		return exist;
	}
	
	public void updateClan(Player player, int kills, int deaths, int xp, int money) {
		try {
			Clan faction = getClan(player);
			PreparedStatement statement = BukkitMain.getPlugin().getMysqlBackend().getConnection().prepareStatement("UPDATE `factions` SET `kills`=`kills`+" + kills + ", `deaths`=`deaths`+" + deaths + ", " + "`xp`=`xp`+" + xp +", `money`=`money`+" + money + " WHERE name ='" + faction.getName() + "'");
			statement.execute();
			statement.close();
			faction.setKills(faction.getKills() + kills);
			faction.setDeaths(faction.getDeaths() + deaths);
			faction.setXp(faction.getXp() + xp);
			faction.setMoney(faction.getMoney() + money);
		} catch (Exception e) {}
	}
	
	public boolean addMember(String factionName, String name) {
		boolean err = false;
		
		try {
			Clan faction = null;
			for(Clan factions : clans) {
				if(faction.getName().equalsIgnoreCase(factionName)) {
					faction = factions;
				}
			}
			faction.getMembers().add(name);
			String names = "";
			for(String members : faction.getMembers()) {
				names += members + ", ";
			}
			PreparedStatement statement = BukkitMain.getPlugin().getMysqlBackend().getConnection().prepareStatement("UPDATE `factions` SET `members` ='" + names + "' WHERE name ='" + factionName + "'");
			statement.execute();
			statement.close();
			
			err = true;
		} catch (Exception e) {}
		return err;
	}
	
	public boolean removeMember(String factionName, String name) {
		boolean err = false;
		
		try {
			Clan faction = null;
			for(Clan factions : clans) {
				if(faction.getName().equalsIgnoreCase(factionName)) {
					faction = factions;
				}
			}
			faction.getMembers().remove(name);
			String names = "";
			for(String members : faction.getMembers()) {
				names += members + ", ";
			}
			PreparedStatement statement = BukkitMain.getPlugin().getMysqlBackend().getConnection().prepareStatement("UPDATE `factions` SET `members` ='" + names + "' WHERE name ='" + factionName + "'");
			statement.execute();
			statement.close();
			
			err = true;
		} catch (Exception e) {}
		return err;
	}
	
	public boolean hasOwnerPlayer(Player player) {
		boolean err = false;
		
		for(Clan factions : clans) {
			if(factions.getOwner().equalsIgnoreCase(player.getName())) {
				err = true;
			}
		}
		
		return err;
	}
	
	public String getClanName(Player player) {
		String factionName = "";
		
		for(Clan faction : clans) {
			for(String member : faction.getMembers()) {
				if(member.equalsIgnoreCase(player.getName())) {
					factionName = faction.getName();
				} else {
					factionName = "Nenhum";
				}
			}
		}
		return factionName;
	}
	
	public String getClanRankName(Player player) {
		return getClanRank(player).getColor() + getClanRank(player).name();
	}
	
	public ClanRank getClanRank(Player player) {
		ClanRank clan = null;
		
		for(Clan clans : clans) {
			for(String member : clans.getMembers()) {
				if(member.equalsIgnoreCase(player.getName())) {
					clan = clans.getRank();
				}
			}
		}
		return clan;
	}
	
	public ClanRank getRank(String name) {
		try {
			return ClanRank.valueOf(getString(name, "rank"));
		} catch (Exception e) {
			e.printStackTrace();
			return ClanRank.INITIAL;
		}
	}
	
	public Clan getClan(Player player) {
		Clan clan = null;
		
		for(Clan clans : this.clans) {
			for(String member : clans.getMembers()) {
				if(member.equalsIgnoreCase(player.getName())) {
					clan = clans;
				}
			}
		}
		return clan;
	}
	
	public String getString(String name, String column) {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getStatement().executeQuery("SELECT * FROM `clans` WHERE `name` = '" + name + "';");
			if(resultSet.next()) {
				return resultSet.getString(column);
			}
			resultSet.close();
			return null;
		} catch(SQLException exception) {
			exception.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<String> names;
	public ArrayList<Integer> xps;
	public ArrayList<String> ranks;
	
	public void loadTop10Clan()  {
		names = new ArrayList<>();
		xps = new ArrayList<>();
		ranks = new ArrayList<>();
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				PreparedStatement stmt;
				try {
					stmt = BukkitMain.getPlugin().getMysqlBackend().prepareStatment("SELECT xp, name FROM clans ORDER BY xp DESC LIMIT 10");
					ResultSet result = stmt.executeQuery();
					while (result.next()) {
						names.add(result.getString("name"));
						xps.add(result.getInt("xp"));
						ranks.add(result.getString("rank"));
					}
					result.close();
					stmt.close();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
					}
			}
		}.runTaskTimerAsynchronously(BukkitMain.getPlugin(), 0L, 300 * 20);
	}
	
	
	public boolean isOnTop10(String name) {
		if (names.contains(name)) {
			return true;
		}
		return false;
	}
	
	public ArrayList<String> namesd;
	public ArrayList<Integer> xpsd;
	
	public void loadTop10Play()  {
		namesd = new ArrayList<>();
		xpsd = new ArrayList<>();
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				PreparedStatement stmt;
				try {
					stmt = BukkitMain.getPlugin().getMysqlBackend().prepareStatment("SELECT xp, nickname FROM stats ORDER BY xp DESC LIMIT 10");
					ResultSet result = stmt.executeQuery();
					while (result.next()) {
						namesd.add(result.getString("nickname"));
						xpsd.add(result.getInt("xp"));
					}
					result.close();
					stmt.close();
				} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
					}
			}
		}.runTaskTimerAsynchronously(BukkitMain.getPlugin(), 0L, 300 * 20);
	}
}
