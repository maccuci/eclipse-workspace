package com.fuzion.core.master.account.management;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.bukkit.BukkitMain;

public class PositionManager {
	
	public static final List<PositionInformation> POSITION = new ArrayList<>();
	
	public void run() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				setup();
			}
		}.runTaskTimer(BukkitMain.getPlugin(), 0, 20);
	}
	
	public void setup() {
		try {
			int pos = 0;
			ResultSet resultSet = BukkitMain.getPlugin().getMysqlBackend().getConnection().createStatement().executeQuery("SELECT * FROM `stats` ORDER BY `xp` DESC");
			while (resultSet.next()) {
				pos++;
				POSITION.add(new PositionInformation(resultSet.getString("nickname"), resultSet.getInt("xp"), pos));
			}
//			for(Player player : Bukkit.getOnlinePlayers()) {
//				Hologram hologram = new Hologram("§6§lPOSIÇOES DO SERVIDOR", Bukkit.getWorld("world").getSpawnLocation().add(0, 2, 0), true);
//				for(PositionInformation information : PositionManager.POSITION) {
//					hologram.addLine("§" + (information.getPosition() == 1 ? "a" : information.getPosition() == 2 ? "2" : information.getPosition() == 3 ? "6" : information.getPosition() == 4 ? "e" : information.getPosition() == 5 ? "c" : "4") + information.getPosition() + "º" + " §f" + information.getName() + " §7- §fXP: §b" + new StatsManager().get(Bukkit.getPlayer(information.getName()).getUniqueId(), "xp"));
//				}
//					hologram.show(player);
//			}
			resultSet.close();
		} catch (Exception e) {}
	}
	
	public Integer getPosition(Player player) {
		int pos = 0;
		
		for(PositionInformation manager : POSITION) {
			if(manager.getName().equalsIgnoreCase(player.getName())) {
				pos = manager.getPosition();
			}
		}
		return pos;
	}
	
	public Integer getPosition(OfflinePlayer player) {
		int pos = 0;
		
		for(PositionInformation manager : POSITION) {
			if(manager.getName().equalsIgnoreCase(player.getName())) {
				pos = manager.getPosition();
			}
		}
		return pos;
	}
	
}
