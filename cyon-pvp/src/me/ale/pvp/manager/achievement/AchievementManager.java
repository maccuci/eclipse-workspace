package me.ale.pvp.manager.achievement;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.manager.StatsManager;
import me.ale.pvp.manager.achievement.type.AchievementType;

public class AchievementManager {
	
	private Plugin plugin = null;
	
	public AchievementManager(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public boolean hasAchievement(Player player, AchievementType type) {
		return plugin.getConfig().get(player.getName() + ".Achievements." + type.getName()) != null ? true : false;
	}
	
	public void giveAchievement(Player player, AchievementType type) {
		StatsManager manager = BukkitMain.getPlugin().getStatsManager();
		
		if(hasAchievement(player, type))
			return;
		plugin.getConfig().set(player.getName() + ".Achievements." + type.getName(),true);
        plugin.saveConfig();
        player.sendMessage(type.getMessage().replace("%achievement-name%", type.getItemName()).replace("%reward%", String.valueOf(type.getReward())));
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1, 1);
        manager.set(player.getUniqueId(), "money", manager.get(player.getUniqueId(), "money") + type.getReward());
	}
}
