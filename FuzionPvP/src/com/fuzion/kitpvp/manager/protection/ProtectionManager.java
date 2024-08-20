package com.fuzion.kitpvp.manager.protection;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

public class ProtectionManager {
	
	private static final List<Player> protection = new ArrayList<>();
	
	public static void addProtectionToPlayer(Player player) {
		if(protection.contains(player)) {
			return;
		}
		protection.add(player);
		player.sendMessage("§aVocê recebeu a proteção do spawn.");
	}
	
	public static void removeProtectionToPlayer(Player player) {
		if(!protection.contains(player)) {
			return;
		}
		protection.remove(player);
		player.sendMessage("§aVocê perdeu a proteção do spawn.");
	}
	
	public static boolean hasProtected(Player player) {
		return protection.contains(player);
	}
}
