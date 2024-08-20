package com.vexy.thepit.player.scoreboard;

import org.bukkit.Bukkit;

import com.vexy.thepit.Main;

import lombok.Getter;

public class Scroller {
	
	@Getter
	private static String title = "§e§lTHE PIT";
	@Getter
	private static int count = 0, schedule;
	
	public static void run() {
		schedule = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				count++;
				
				if(count == 1) {
					title = "§f§lT§e§lHE PIT";
				}
				if(count == 2) {
					title = "§e§lT§f§lH§e§lE PIT";
				}
				if(count == 3) {
					title = "§e§lTH§f§lE §e§lPIT";
				}
				if(count == 4) {
					title = "§e§lTHE §f§lP§e§lIT";
				}
				if(count == 5) {
					title = "§e§lTHE P§f§lI§e§lT";
				}
				if(count == 6) {
					title = "§e§lTHE PI§f§lT";
				}
				if(count == 7) {
					count = 1;
				}
			}
		}, 0, 20);
	}

}
