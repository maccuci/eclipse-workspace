package com.fuzion.hg.manager.kit;

import org.bukkit.entity.Player;

import com.fuzion.hg.Main;

public class KitMatch {
	
	public static boolean hasKitMatch(Player player, String kit) {
		return Main.getPlugin().getConfig().get(player.getName() + ".KitMatch") != null ? getKitMatch(player).contains(kit) : false;
	}
	
	public static void setKitMatch(Player player, String kit) {
		
		if(hasKitMatch(player, kit))
			return;
		Main.getPlugin().getConfig().set(player.getName() + ".KitMatch", kit);
		Main.getPlugin().saveConfig();
	}
	
	public static String getKitMatch(Player player) {
		return Main.getPlugin().getConfig().getString(player.getName() + ".KitMatch");
	}
	
	public static void removeKitMatch(Player player) {
		Main.getPlugin().getConfig().set(player.getName() + ".KitMatch", "");
	}
}
