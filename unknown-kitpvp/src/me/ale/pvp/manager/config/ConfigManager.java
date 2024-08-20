package me.ale.pvp.manager.config;

import org.bukkit.entity.Player;

import me.ale.pvp.Main;

public class ConfigManager {
	
	public String getPrefixMessage(String name) {
		return Main.getPlugin().getConfig().getString(name + ".");
	}
	
	public void message(Player player, String message) {
		player.sendMessage(getPrefixMessage(message));
	}
}
