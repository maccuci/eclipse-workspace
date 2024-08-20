package me.feenks.hg.utils;

import org.bukkit.scheduler.BukkitRunnable;

import me.feenks.hg.HardcoreGames;

public class Scroller {
	
	private String text = "§6§lHG-A1";
	
	public void run() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				int speed = 0;
				
				if(speed == 1) {
					text = "§6§lHG-A1";
				}
				if(speed == 2) {
					text = "§e§lH§6§lG-A1";
				}
				if(speed == 3) {
					text = "§f§lH§e§lG§6§l-A1";
				}
				if(speed == 4) {
					text = "§f§lHG§e§l-A§6§l1";
				}
				if(speed == 5) {
					text = "§f§lHG-A§e§l1";
					speed = 1;
				}
			}
		}.runTaskTimer(HardcoreGames.getPlugin(), 0, 1);
	}
	
	public String getText() {
		return text;
	}
}
