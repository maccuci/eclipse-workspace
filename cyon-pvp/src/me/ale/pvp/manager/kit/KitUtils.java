package me.ale.pvp.manager.kit;

import me.ale.pvp.Main;

public class KitUtils {
	
	public static void addKitFree(String kitName) {
		Kit kit = KitManager.getKit(kitName);
		String k = Main.getPlugin().getConfig().getString("FreeKit") != null ? Main.getPlugin().getConfig().getString("FreeKit") : "";
		
		Main.getPlugin().getConfig().set("FreeKit", k + "," + kit.getName());
		Main.getPlugin().saveConfig();
	}
	
	public static void removeKitFree(String kitName) {
		Kit kit = KitManager.getKit(kitName);
		String k = Main.getPlugin().getConfig().getString("FreeKit") != null ? Main.getPlugin().getConfig().getString("FreeKit") : "";
		
		if(k.contains(kit.getName())) {
			//k.replace("," + kit.getName(), "");
			k.split("," + kit.getName(), 0);
		}
		
		Main.getPlugin().getConfig().set("FreeKit", k);
		Main.getPlugin().saveConfig();
	}
	
	public static void clearFreeKit() {
		Main.getPlugin().getConfig().set("FreeKit", "");
		Main.getPlugin().saveConfig();
	}
	
	public static boolean isKitFree(String kitName) {
		Kit kit = KitManager.getKit(kitName);
		String k = Main.getPlugin().getConfig().getString("FreeKit") != null ? Main.getPlugin().getConfig().getString("FreeKit") : "";
		
		return k.contains(kit.getName());
	}
}
