package com.fuzion.hg.manager.timer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.hg.Main;
import com.fuzion.hg.command.TagCommand;
import com.fuzion.hg.event.GameStageChangeEvent;
import com.fuzion.hg.manager.ScoreboardManager;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.game.GameStage;

public class PreGameTimer implements TimerScheduler {
	
	public static Integer timer = GameStage.WAITING.getDefaultTimer();
	public static int count = 0;
	
	@Override
	public void pulse() {
		count = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (timer > 0) {
					timer--;
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						if(TagCommand.scoreboard.contains(player))
							return;
						new ScoreboardManager().waiting(player);
					}

					if (timer % 30 == 0 && timer > 0) {
						Bukkit.broadcastMessage("§aPartida dando inicio em " + DateUtils.formatDifference(timer));
					}
					if (timer <= 5 && timer > 0) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.5f, 1.5f);
						}
						Bukkit.broadcastMessage("§aPartida dando inicio em " + DateUtils.formatDifference(timer));
					}
					if(timer == 0) {
						if(GameManager.getPlayerSize() < 5) {
							Bukkit.broadcastMessage("§cContagem reiniciada para 5 minutos, pois está faltando " + (5 - GameManager.getPlayerSize()) + " jogador" + (GameManager.getPlayerSize() > 1 ? "" : "s"));
							timer = 300;
						} else {
							timer = 0;
							GameStageChangeEvent event = new GameStageChangeEvent(GameStage.WAITING, GameStage.INVINCIBILITY);
							Bukkit.getPluginManager().callEvent(event);
							for (Player player : Bukkit.getOnlinePlayers()) {
								player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
								new TagManager(player).update();
							}
							stop();
							Bukkit.broadcastMessage("§aA Partida foi iniciada!");
							Bukkit.broadcastMessage("§6Seja o último de pé! Que a sorte esteja com você.");
						}
					}
				}
			}
		}, 0, 20);
	}

	@Override
	public void stop() {
		Bukkit.getScheduler().cancelTask(count);
	}
}
