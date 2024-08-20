package me.ale.hg.manager.timer;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.hg.Main;
import me.ale.hg.event.game.GameChangeEvent;
import me.ale.hg.manager.game.GameManager;
import me.ale.hg.manager.game.GameType;
import me.ale.hg.util.DateUtils;

public class InvincibilityTimer {
	
	private static Integer timer = GameManager.getType().getTimer();
	
	public static void startCountdown() {
		if(!GameManager.isInvincibility())
			return;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				timer--;
				
				if(timer > 0 && timer == 0 % 30) {
					Bukkit.getOnlinePlayers().forEach(players -> {
						players.sendMessage("§9§lTORNEIO §7Invencibilidade acabando em " + DateUtils.formatDifference(timer));
					});
				} else if(timer == 0) {
					GameChangeEvent event = new GameChangeEvent(GameType.WAITING, GameType.INVINCIBILITY);
					Bukkit.getPluginManager().callEvent(event);
					Bukkit.getOnlinePlayers().forEach(players -> {
						players.sendMessage("§9§lTORNEIO §7A partida teve seu início! Boa sorte para todos.");
					});
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0, 1);
	}

}
