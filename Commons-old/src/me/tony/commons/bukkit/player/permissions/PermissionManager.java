package me.tony.commons.bukkit.player.permissions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import me.tony.commons.api.item.ItemBuilder;
import me.tony.commons.bukkit.BukkitMain;
import me.tony.commons.bukkit.manager.CoreManager;
import me.tony.commons.bukkit.manager.management.Management;
import me.tony.commons.bukkit.player.permissions.list.Rank;
import me.tony.commons.bukkit.player.permissions.list.RankType;
import me.tony.commons.bukkit.player.tag.list.TagTypes;

public class PermissionManager extends Management {
	
	public PermissionManager(CoreManager coreManager) {
		super(coreManager, "PermissionManager");
	}
	
	private static final HashMap<String, Rank> ranks = new HashMap<>();
	
	public void loadRanks() {
		System.out.println("Trying to load all the ranks in the table.");
		
		try {
			Connection mainConnection = BukkitMain.getPlugin(BukkitMain.class).getCoreManager().getDataManager().getMySQL().getConnection();
			PreparedStatement rankQuery = mainConnection.prepareStatement("SELECT * FROM `global_ranks`;");
			ResultSet rankData = rankQuery.executeQuery();
			
			if(rankData.next()) {
				int id = rankData.getInt(1);
				String name = rankData.getString(2);
				TagTypes tag = TagTypes.getTag(rankData.getString(3));
				boolean defaultGroup = ItemBuilder.getBooleanByInteger(rankData.getInt(4));
				
				if(!ranks.containsKey(name)) {
					Rank rank = new Rank(name, id, tag, 0, defaultGroup);
					if(RankType.VIP.ordinal() < RankType.BETA.ordinal()) {
						rank.setVip(true);
					}
					ranks.put(name, rank);
					System.out.println("The ranks " + name + " was added to the list of ranks.");
				}
			}
			rankData.close();
			rankQuery.close();
			
			for(RankType rankType : RankType.values()) {
				if(!ranks.containsKey(rankType.getName())) {
					createRank(rankType.getName(), rankType.getType().getTagName());
				}
			}
			
			System.out.println("All ranks was loaded by the tables in mysql.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createRank(String groupName, String tag) {
		try {
			Connection mainConnection = BukkitMain.getPlugin(BukkitMain.class).getCoreManager().getDataManager().getMySQL().getConnection();
			PreparedStatement insertStatment = mainConnection.prepareStatement("INSERT INTO `global_ranks` (`name`, `tag`) VALUES (?, ?)");
			
			insertStatment.setString(1, groupName);
			insertStatment.setString(2, tag);
			insertStatment.execute();
			insertStatment.close();
		} catch (Exception e) {
		}
	}

	public RankType getRankType(String name) {
		for (RankType rankType : RankType.values())
			if (rankType.getName().equalsIgnoreCase(name))
				return rankType;
		return null;
	}

	public Rank getRank(int id) {
		for (Rank rank : ranks.values())
			if (rank.getId() == id)
				return rank;
		return null;
	}

	public Rank getDefaultRank() {
		for (Rank rank : ranks.values())
			if (rank.isDefaultRank())
				return rank;
		return null;
	}

	public Rank getRank(String name) {
		for (Rank rank : ranks.values())
			if (rank.getName().equalsIgnoreCase(name))
				return rank;
		return null;
	}

	public boolean initialize() {
		return true;
	}
}
