package com.fuzion.core.bukkit.player.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.fuzion.core.FuzionAPI;
import com.fuzion.core.bukkit.player.account.Account;
import com.fuzion.core.bukkit.player.group.Group;
import com.fuzion.core.bukkit.player.tag.TagManager;

public class PlayerListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onLogin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		FuzionAPI.getAccountManager().craftAccount(player);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		Account account = FuzionAPI.getAccountManager().getAccount(player);
		
		account.setGroup(Group.DEVELOPER);
		new TagManager().findTag(player);
		
		if (player.getAddress().getAddress().toString().startsWith("0.0")){
			player.kickPlayer("Tentativa de invasão.");
		}
		
		player.sendMessage("§aSeu grupo atual: " + account.getGroup().getColor() + account.getGroup().name());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		Scoreboard board = e.getPlayer().getScoreboard();
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
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		FuzionAPI.getAccountManager().unloadAccount(event.getPlayer());
	}
}
