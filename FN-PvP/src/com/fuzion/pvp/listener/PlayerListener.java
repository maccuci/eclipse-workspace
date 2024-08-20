package com.fuzion.pvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.fuzion.pvp.manager.ScoreboardManager;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(null);
		new ScoreboardManager().createScoreboard(player);
	}
	
	@EventHandler
	public void onTest(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		
		if(event.isSneaking() && player.isSneaking()) {
			new ScoreboardManager().updateScore(player, "welcome", "§7PAU NO CU");
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		
		Scoreboard board = event.getPlayer().getScoreboard();
		if (board != null) {
			for (Team t : board.getTeams()) {
				t.unregister();
				t = null;
			}
			for (Objective ob : board.getObjectives()) {
				ob.unregister();
				ob = null;
			}
			board = null;
		}
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}
