package me.ale.hg.manager.timer;

import org.bukkit.scheduler.BukkitRunnable;

import me.ale.hg.Main;
import me.ale.hg.manager.feast.FeastManager;
import me.ale.hg.manager.game.GameManager;

public class GameTimer {
	
	private static Integer timer = GameManager.getType().getTimer();
	
	public static void startCountdown() {
		if(!GameManager.isGame())
			return;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				timer++;
				
				if(timer == 600) {
					FeastManager.announceFeast();
				}
				
				if(timer == 900) {
					FeastManager.spawn();
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0, 1);
	}
}
