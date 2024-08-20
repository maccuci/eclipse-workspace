package me.ale.christmas.util;

import org.bukkit.ChatColor;

import me.ale.christmas.Christmas;

public class MessageUtils {
	
	public String translateColor(String textToTranslate) {
		return ChatColor.translateAlternateColorCodes('&',Christmas.getPlugin().getConfig().getString("Christmas." + textToTranslate));
	}
}
