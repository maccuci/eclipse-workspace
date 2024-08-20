package me.feenks.wdr.util;

import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PlayerUtils {
	
	public static int getPing(Player player) {
		CraftPlayer craftPlayer = (CraftPlayer)player;
		
		return craftPlayer.getHandle().ping;
	}

}
