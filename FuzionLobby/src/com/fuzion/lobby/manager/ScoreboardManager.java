package com.fuzion.lobby.manager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.core.FuzionAPI;
import com.fuzion.core.bukkit.manager.scoreboard.BaseScoreboardHandler;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.PositionManager;
import com.fuzion.core.master.account.management.StatsManager;
import com.fuzion.lobby.manager.scoreboard.SimpleScoreboard;

public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		if(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
			SimpleScoreboard scoreboard = new SimpleScoreboard("  §6§lLOBBY  ");
			scoreboard.add(" ");
			scoreboard.add("Jogadores: §a");
			scoreboard.add("Grupo: ");
			scoreboard.add(" ");
			scoreboard.add("Posição: §6" + new PositionManager().getPosition(player) + "°");
			scoreboard.add("XP: §b" + new StatsManager().get(player.getUniqueId(), "xp"));
			scoreboard.add(" ");
			scoreboard.add("§e" + FuzionAPI.STORE);
			scoreboard.build();
			scoreboard.send(player);
			
			player.getScoreboard().registerNewTeam("players").addEntry("Jogadores: §a");
			player.getScoreboard().registerNewTeam("group").addEntry("Grupo: ");
		}
		GroupManager groupManager = new GroupManager();
		player.getScoreboard().getTeam("players").setSuffix("1");
		player.getScoreboard().getTeam("group").setSuffix(groupManager.getGroup(player.getUniqueId()).getColor() + groupManager.getGroup(player.getUniqueId()).name());
	}
	
	public void test(Player player) {
		BaseScoreboardHandler scoreboardHandler = new BaseScoreboardHandler(player);
		scoreboardHandler.initialize("  §6§lLOBBY  ");
		scoreboardHandler.setScore("§b§c", "§2§l", "§7");
		scoreboardHandler.setScore("§fJogadores", "§7", " §a1");
		scoreboardHandler.setScore("§fGrupo", "§7", " " + new GroupManager().getGroup(player.getUniqueId()).getColor() + new GroupManager().getGroup(player.getUniqueId()).name());
		scoreboardHandler.setScore("§d§c", "§2§l", "§7");
		scoreboardHandler.setScore("§fPosição", "§7", " §6" + new PositionManager().getPosition(player) + "°");
		scoreboardHandler.setScore("§fXP", "§7", "§b " + new StatsManager().get(player.getUniqueId(), "xp"));
		scoreboardHandler.setScore("§3§c", "§2§l", "§7");
		scoreboardHandler.setScore("§efuzion-", "§7", "§enetwork.com");
	}
	
	public void updateScoreboard(Player player) {
		
	}
}
