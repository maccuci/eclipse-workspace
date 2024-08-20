package me.ale.commons.core.account.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.entity.Player;

import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.manager.constructor.Faction;

public class FactionManager {
	
	private List<Faction> factions = new ArrayList<>();
	private HashMap<Player, Boolean> factionChat = new HashMap<>();
	
	public void setup() {
		try {
			ResultSet resultSet = BukkitMain.getPlugin().getDatabaseConnector().getConnection().createStatement().executeQuery("SELECT * FROM `factions`");
			while(resultSet.next()) {
				String name = resultSet.getString("name");
				String owner = resultSet.getString("owner");
				String member = resultSet.getString("members");
				String message = resultSet.getString("message");
				int kills = resultSet.getInt("kills");
				int deaths = resultSet.getInt("deaths");
				int money = resultSet.getInt("money");
				List<String> members = new ArrayList<>();
				for(String m : member.split(", ")) {
					members.add(m);
				}
				factions.add(new Faction(name, owner, message, members, kills, deaths, money));
			}
		} catch (Exception e) {
		}
	}
	
	public boolean createFaction(String name, String owner, String message, String member) {
		boolean err = false;
		try {
			PreparedStatement statement = BukkitMain.getPlugin().getDatabaseConnector().getConnection().prepareStatement("INSERT INTO `factions` (`name`, `owner`, `members`, `kills`, `deaths`, `money`, `message`) VALUES ('" + name + "', '" + owner + "', '" + member + "', '" + 0 + "', '" + 0 + "', '" + 0 + "', '" + message + "');");
			statement.execute();
			statement.close();
			List<String> members = new ArrayList<>();
			members.add(member);
			factions.add(new Faction(name, owner, message, members, 0, 0, 0));
			err = true;
		} catch(SQLException exception) {
			exception.printStackTrace();
		}
		
		return err;
	}
	
	public void put(Player player, boolean b) {
		factionChat.put(player, b);
	}
	
	public boolean hasChat(Player player) {
		boolean err = false;
		
		if(hasPlayerFaction(player)) {
			if(factionChat.containsKey(player)) {
				err = factionChat.get(player);
			}
		}
		return err;
	}
	
	public boolean hasFaction(String name) {
		boolean err = false;
		
		for(Faction factions : factions) {
			if(factions.getName().contentEquals(name)) {
				err = true;
			}
		}
		return err;
	}
	
	public boolean hasPlayerFaction(Player player) {
		boolean err = false;
		
		if(getFaction(player) != null && !getFactionName(player).isEmpty()) {
			err = true;
		}
		
		return err;
	}
	
	public void updateFaction(Player player, int kills, int deaths, int money) {
		try {
			Faction faction = getFaction(player);
			PreparedStatement statement = BukkitMain.getPlugin().getDatabaseConnector().getConnection().prepareStatement("UPDATE `factions` SET `kills`=`kills`+" + kills + ", `deaths`=`deaths`+" + deaths + ", `money`=`money`+" + money + " WHERE name ='" + faction.getName() + "'");
			statement.execute();
			statement.close();
			faction.setKills(faction.getKills() + kills);
			faction.setDeaths(faction.getDeaths() + deaths);
			faction.setMoney(faction.getMoney() + money);
		} catch (Exception e) {}
	}
	
	public void updateFactionMessage(Player player, String message) {
		try {
			Faction faction = getFaction(player);
			PreparedStatement statement = BukkitMain.getPlugin().getDatabaseConnector().getConnection().prepareStatement("UPDATE `factions` SET `message`=`message`+" + message + " WHERE name ='" + faction.getName() + "'");
			statement.execute();
			statement.close();
			faction.setMessage(message);
		} catch (Exception e) {}
	}
	
	public boolean addMember(String factionName, String name) {
		boolean err = false;
		
		try {
			Faction faction = null;
			for(Faction factions : factions) {
				if(faction.getName().equalsIgnoreCase(factionName)) {
					faction = factions;
				}
			}
			faction.getMembers().add(name);
			String names = "";
			for(String members : faction.getMembers()) {
				names += members + ", ";
			}
			PreparedStatement statement = BukkitMain.getPlugin().getDatabaseConnector().getConnection().prepareStatement("UPDATE `factions` SET `members` ='" + names + "' WHERE name ='" + factionName + "'");
			statement.execute();
			statement.close();
			
			err = true;
		} catch (Exception e) {}
		return err;
	}
	
	public boolean removeMember(String factionName, String name) {
		boolean err = false;
		
		try {
			Faction faction = null;
			for(Faction factions : factions) {
				if(faction.getName().equalsIgnoreCase(factionName)) {
					faction = factions;
				}
			}
			faction.getMembers().remove(name);
			String names = "";
			for(String members : faction.getMembers()) {
				names += members + ", ";
			}
			PreparedStatement statement = BukkitMain.getPlugin().getDatabaseConnector().getConnection().prepareStatement("UPDATE `factions` SET `members` ='" + names + "' WHERE name ='" + factionName + "'");
			statement.execute();
			statement.close();
			
			err = true;
		} catch (Exception e) {}
		return err;
	}
	
	public boolean hasOwnerPlayer(Player player) {
		boolean err = false;
		
		for(Faction factions : factions) {
			if(factions.getOwner().equalsIgnoreCase(player.getName())) {
				err = true;
			}
		}
		
		return err;
	}
	
	public String getFactionName(Player player) {
		String factionName = "";
		
		for(Faction faction : factions) {
			for(String member : faction.getMembers()) {
				if(member.equalsIgnoreCase(player.getName())) {
					factionName = faction.getName();
				} else {
					factionName = "None";
				}
			}
		}
		return factionName;
	}
	
	public Faction getFaction(Player player) {
		Faction faction = null;
		
		for(Faction factions : factions) {
			for(String member : factions.getMembers()) {
				if(member.equalsIgnoreCase(player.getName())) {
					faction = factions;
				}
			}
		}
		return faction;
	}
}
