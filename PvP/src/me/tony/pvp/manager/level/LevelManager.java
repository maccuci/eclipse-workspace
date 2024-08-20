package me.tony.pvp.manager.level;

import me.tony.commons.api.item.ItemBuilder;

public class LevelManager {
	
	public static String getFormattedFromColors(int level) {
		String format = "";
		
		if(level <= 10) {
			format = "§8(§a§l" + level + "§8) ";
		} else if(level <= 20) {
			format = "§8(§e§l" + level + "§8) ";
		} else if(level <= 30) {
			format = "§8(§6§l" + level + "§8) ";
		} else if(level <= 40) {
			format = "§8(§c§l" + level + "§8) ";
		} else if(level <= 50) {
			format = "§8(§4§l" + level + "§8) ";
		} else if(level >= 60 && level <= 1000) {
			format = "§8(§5§l" + level + "§8) ";
		} else if(level >= 1000) {
			format = "§8(§b§l" + ItemBuilder.formatNumber(level) + "§8) ";
		}
		
		return format;
	}
}
