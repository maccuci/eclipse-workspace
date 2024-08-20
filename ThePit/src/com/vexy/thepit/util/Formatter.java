package com.vexy.thepit.util;

import com.vexy.thepit.player.PitPlayer;

public class Formatter {
	
	public static String formatLevel(int value) {
		if(value >= 1 || value < 10) {
			return "§7";
		}
		if(value >= 10 || value < 20) {
			return "§9";
		}
		if(value >= 20 || value < 30) {
			return "§3";
		}
		if(value >= 30 || value < 40) {
			return "§2";
		}
		if(value >= 40 || value < 50) {
			return "§a";
		}
		if(value >= 50 || value < 60) {
			return "§e";
		}
		if(value >= 60 || value < 70) {
			return "§6§l";
		}
		if(value >= 70 || value < 80) {
			return "§c§l";
		}
		if(value >= 80 || value < 90) {
			return "§4§l";
		}
		if(value >= 90 || value < 100) {
			return "§5§l";
		}
		if(value >= 100 || value < 110) {
			return "§f§l";
		}
		if(value >= 110 || value < 120) {
			return "§b§l";
		}
		return "§b§l";
	}
	
	public static String formatPresitige(int value) {
		if(value == 1) {
			return "§7";
		}
		if(value == 2) {
			return "§9";
		}
		if(value == 3) {
			return "§3";
		}
		if(value == 4) {
			return "§2";
		}
		if(value == 5) {
			return "§a";
		}
		if(value == 6) {
			return "§e";
		}
		if(value == 7) {
			return "§6";
		}
		if(value == 8) {
			return "§c";
		}
		if(value == 9) {
			return "§4";
		}
		if(value == 10) {
			return "§5";
		}
		if(value == 11) {
			return "§f";
		}
		if(value == 12) {
			return "§b";
		}
		return "";
	}
	
	public static String formatChat(PitPlayer player) {
		return formatPresitige(player.getPrestige()) + "[" + formatLevel(player.getLevel()) + player.getLevel() + formatPresitige(player.getPrestige()) + "]";
	}
}
