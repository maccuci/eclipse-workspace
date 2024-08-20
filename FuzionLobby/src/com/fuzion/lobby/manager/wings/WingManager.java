package com.fuzion.lobby.manager.wings;

import java.util.HashMap;

import org.bukkit.entity.Player;


public class WingManager {
	
	public static HashMap<Player, String> wingMap = new HashMap<>();
	
	public void setWing(Player player, WingType type) {
		wingMap.put(player, type.name());
	}
	
	public void removeWing(Player player) {
		wingMap.remove(player);
	}
	
	
	public enum WingType {
		ANGEL,
		ETERNAL,
		DEATH,
		BLOOD;
	}
}
