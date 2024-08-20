package com.fuzion.party.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.bukkit.manager.scoreboard.BaseScoreboardHandler;
import com.fuzion.party.Main;
import com.fuzion.party.manager.classes.ClassManager;
import com.fuzion.party.manager.minegame.PointManager;

@SuppressWarnings("deprecation")
public class ScoreboardManager {
	
	static int ps = Bukkit.getOnlinePlayers().length;
	
	static BaseScoreboardHandler scoreboard;
	
	public static boolean start() {
		new BukkitRunnable() {
			
			@Override
			public void run() {
				if(ps == 0)
					return;
				if(ps == 12)
					return;
				for(Player player : Bukkit.getOnlinePlayers()) {
					setScoreboard(player, ScoreboardType.GAME);
				}
			}
		}.runTaskTimer(Main.getPlugin(), 2, 2);
		return true;
	}
	
	public static void setScoreboard(Player player, ScoreboardType type) {
		scoreboard = new BaseScoreboardHandler(player);
		switch (type) {
		case LOBBY:
			scoreboard.initialize("  §6§lPARTY  ");
			scoreboard.setScore("§b§c", "§2§l", "§7");
			scoreboard.setScore("§fCoins ", "§f", "§60");
			scoreboard.setScore("§fGrupo ", "§f", "§4DONO");
			scoreboard.setScore("§3§c", "§2§l", "§7");
			scoreboard.setScore("§efuzion-", "§7", "§enetwork.com");
			break;
			
		case GAME:
			scoreboard.initialize("  §6§lPARTY  ");
			scoreboard.setScore("§b§c", "§2§l", "§7");
			scoreboard.setScore("§6Pontos ", "§7", "§6atuais");
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(ps == 0)
					return;
				if(ps == 12)
					return;
				scoreboard.setScore("§f- " + online.getName(), "§7", " §e" + PointManager.getPoints(online));
			}
			scoreboard.setScore("§3§c", "§2§l", "§7");
			scoreboard.setScore("Começando em ", "§f", "§c1:50");
			scoreboard.setScore("Sua Classe ", "§f", "§b" + new ClassManager().getClassName(player));
			scoreboard.setScore("§5§c", "§2§l", "§7");
			scoreboard.setScore("§efuzion-", "§7", "§enetwork.com");
			break;

		default:
			break;
		}
	}
	
	static void updateScoreboard(Player player, ScoreboardType type) {
		scoreboard = new BaseScoreboardHandler(player);
		switch (type) {
		case GAME:
			if(scoreboard == null)
				return;
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(ps == 0)
					return;
				if(ps == 12)
					return;
				scoreboard.updateScore("§f- " + online.getName(), "§7", "§7");
			}
			break;

		default:
			break;
		}
	}
	
	public enum ScoreboardType {
		LOBBY,
		GAME;
	}
}
