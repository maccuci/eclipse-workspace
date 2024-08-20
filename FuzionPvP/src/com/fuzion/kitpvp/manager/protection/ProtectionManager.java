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
		player.sendMessage("�aVoc� recebeu a prote��o do spawn.");
	}
	
	public static void removeProtectionToPlayer(Player player) {
		if(!protection.contains(player)) {
			return;
		}
		protection.remove(player);
		player.sendMessage("�aVoc� perdeu a prote��o do spawn.");
	}
	
	public static boolean hasProtected(Player player) {
		return protection.contains(player);
	}
}
