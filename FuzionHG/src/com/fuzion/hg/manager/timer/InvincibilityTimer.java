package com.fuzion.hg.manager.timer;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.hg.Main;
import com.fuzion.hg.event.GameStageChangeEvent;
import com.fuzion.hg.manager.ScoreboardManager;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.game.GameStage;

public class InvincibilityTimer implements TimerScheduler {
	
	public static int timer = Main.INVINCIBILITY_TIMER;
	public static int count = 0;
	
	@Override
	public void pulse() {
		count = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {

			@SuppressWarnings("deprecation")
			public void run() {
				GameManager.stage = GameStage.INVINCIBILITY;
				if (timer > 0) {
					timer--;
					
					for (Player player : Bukkit.getOnlinePlayers()) {
						new ScoreboardManager().invincibility(player);
					}

					if (timer % 30 == 0 && timer > 0) {
						Bukkit.broadcastMessage("§aInvencibilidade terminando em " + DateUtils.formatDifference(timer));
					}
					if (timer <= 5 && timer > 0) {
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.playSound(player.getLocation(), Sound.NOTE_STICKS, 1.5f, 1.5f);
						}
						Bukkit.broadcastMessage("§aInvencibilidade terminando em " + DateUtils.formatDifference(timer));
					}
					if (timer == 0) {
						Bukkit.broadcastMessage("§aInvencibilidade acabou!");
						GameStageChangeEvent event = new GameStageChangeEvent(GameStage.INVINCIBILITY, GameStage.GAME);
						Bukkit.getPluginManager().callEvent(event);
						new GameTimer().pulse();						
						for (Player player : Bukkit.getOnlinePlayers()) {
							player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
							TagManager tagManager = new TagManager(player);
							tagManager.setTag(tagManager.getTag());
							tagManager.update();
						}
						stop();
					}
				}
			}
			
		}, 0, 20L);
	}
	
	@Override
	public void stop() {
		Bukkit.getScheduler().cancelTask(count);
	}
}
