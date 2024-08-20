package me.ale.hg.manager.timer;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.hg.Main;
import me.ale.hg.event.game.GameChangeEvent;
import me.ale.hg.manager.game.GameManager;
import me.ale.hg.manager.game.GameType;
import me.ale.hg.util.DateUtils;

public class PreGameTimer {
	
	private static Integer timer = GameManager.getType().getTimer();

	public static void startCountdown() {
		if(!GameManager.isPreGame())
			return;
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				timer--;
				
				if(timer > 0 && timer == 0 % 30) {
					Bukkit.getOnlinePlayers().forEach(players -> {
						players.sendMessage("§9§lTORNEIO §7Torneio dando inicio em " + DateUtils.formatDifference(timer));
					});
				} else if(timer == 0) {
					GameChangeEvent event = new GameChangeEvent(GameType.INVINCIBILITY, GameType.GAMETIME);
					Bukkit.getPluginManager().callEvent(event);
					Bukkit.getOnlinePlayers().forEach(players -> {
						players.sendMessage("§9§lTORNEIO §7Torneio iniciou com §a" + Bukkit.getOnlinePlayers().size() + " jogadores. Boa sorte a todos!");
					});
				}
			}
		}.runTaskTimer(Main.getPlugin(), 0, 1);
	}
}
