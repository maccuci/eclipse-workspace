package com.fuzion.hg.manager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.core.FuzionAPI;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.management.PositionManager;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.game.GameStage;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.scoreboard.SimpleScoreboard;
import com.fuzion.hg.manager.timer.GameTimer;
import com.fuzion.hg.manager.timer.InvincibilityTimer;
import com.fuzion.hg.manager.timer.PreGameTimer;

public class ScoreboardManager {
	
	SimpleScoreboard scoreboard = null;
	
	public void waiting(Player player) {
			if(GameManager.getStage() == GameStage.WAITING) {
				if(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
				scoreboard = new SimpleScoreboard("  §6§lHARDCOREGAMES  ");
				scoreboard.blankLine();
				scoreboard.add("Iniciando em: §a");
				scoreboard.add("Seu Kit: §a");
				scoreboard.blankLine();
				scoreboard.add("Posição: §6");
				scoreboard.add("XP: §b0");
				scoreboard.add("Jogadores: §a");
				scoreboard.blankLine();
				scoreboard.add("§e" + FuzionAPI.ADDRESS);
				scoreboard.build();
				scoreboard.send(player);

				player.getScoreboard().registerNewTeam("starting").addEntry("Iniciando em: §a");
				player.getScoreboard().registerNewTeam("kit").addEntry("Seu Kit: §a");
				player.getScoreboard().registerNewTeam("position").addEntry("Posição: §6");
				player.getScoreboard().registerNewTeam("players").addEntry("Jogadores: §a");
			}
				player.getScoreboard().getTeam("starting").setSuffix(ItemBuilder.format(PreGameTimer.timer));
				player.getScoreboard().getTeam("kit").setSuffix(new KitManager().getKitName(player));
				player.getScoreboard().getTeam("position").setSuffix(Main.getPlugin().getPositionManager().getPosition(player) + "°");
				player.getScoreboard().getTeam("players").setSuffix("" + GameManager.getPlayerSize());
			}
}
	
	public void invincibility(Player player) {
		if(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
			scoreboard = new SimpleScoreboard("  §6§lHARDCOREGAMES  ");
			scoreboard.blankLine();
			scoreboard.add("Acabando em: §a");
			scoreboard.add("Seu Kit: §a");
			scoreboard.blankLine();
			scoreboard.add("Posição: §6");
			scoreboard.add("XP: §b0");
			scoreboard.add("Jogadores: §a");
			scoreboard.blankLine();
			scoreboard.add("§e" + FuzionAPI.ADDRESS);
			scoreboard.build();
			scoreboard.send(player);

			player.getScoreboard().registerNewTeam("inv").addEntry("Acabando em: §a");
			player.getScoreboard().registerNewTeam("kit").addEntry("Seu Kit: §a");
			player.getScoreboard().registerNewTeam("position").addEntry("Posição: §6");
			player.getScoreboard().registerNewTeam("players").addEntry("Jogadores: §a");
		}
			player.getScoreboard().getTeam("inv").setSuffix(ItemBuilder.format(InvincibilityTimer.timer));
			player.getScoreboard().getTeam("kit").setSuffix(new KitManager().getKitName(player));
			player.getScoreboard().getTeam("position").setSuffix(Main.getPlugin().getPositionManager().getPosition(player) + "°");
			player.getScoreboard().getTeam("players").setSuffix("" + GameManager.getPlayerSize());
	}
	
	public void game(Player player) {
		if(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
			scoreboard = new SimpleScoreboard("  §6§lHARDCOREGAMES  ");
			scoreboard.blankLine();
			scoreboard.add("Andamento: §a");
			scoreboard.add("Seu Kit: §a");
			scoreboard.blankLine();
			scoreboard.add("Posição: §6");
			scoreboard.add("Kills: §b0");
			scoreboard.add("Jogadores: §a");
			scoreboard.blankLine();
			scoreboard.add("§e" + FuzionAPI.ADDRESS);
			scoreboard.build();
			scoreboard.send(player);

			player.getScoreboard().registerNewTeam("game").addEntry("Andamento: §a");
			player.getScoreboard().registerNewTeam("kit").addEntry("Seu Kit: §a");
			player.getScoreboard().registerNewTeam("position").addEntry("Posição: §6");
			player.getScoreboard().registerNewTeam("players").addEntry("Jogadores: §a");
		}
		player.getScoreboard().getTeam("game").setSuffix("" + ItemBuilder.format(GameTimer.timer));
		player.getScoreboard().getTeam("kit").setSuffix(new KitManager().getKitName(player));
		player.getScoreboard().getTeam("position").setSuffix(Main.getPlugin().getPositionManager().getPosition(player) + "°");
		player.getScoreboard().getTeam("players").setSuffix("" + GameManager.getPlayerSize());
	}
	
	@SuppressWarnings("deprecation")
	public void updateFeastTimer(String string) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			online.getScoreboard().getTeam("timef").setSuffix("" + string);
		}
	}
	
	@SuppressWarnings("deprecation")
	public void addFeastTimer(Location location, int timer) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			addFeastTimer(online, location, timer);
		}
	}
	
	public void addFeastTimer(Player player, Location location, int timer) {
		player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
		if(player.getScoreboard().getObjective(DisplaySlot.SIDEBAR) == null) {
			scoreboard = new SimpleScoreboard("  §6§lHARDCOREGAMES  ");
			scoreboard.blankLine();
			scoreboard.add("Andamento: §a");
			scoreboard.add("Seu Kit: §a");
			scoreboard.blankLine();
			//scoreboard.add("Tempo do Feast §c");
			scoreboard.add("Localização: §c");	
			scoreboard.blankLine();
			scoreboard.add("Posição: §6");
			scoreboard.add("Kills: §b" + GameManager.getKills(player));
			scoreboard.add("Jogadores: §a");
			scoreboard.blankLine();
			scoreboard.add("§e" + FuzionAPI.ADDRESS);
			scoreboard.build();
			scoreboard.send(player);
			player.getScoreboard().registerNewTeam("game").addEntry("Andamento: §a");
			player.getScoreboard().registerNewTeam("kit").addEntry("Seu Kit: §a");
			//player.getScoreboard().registerNewTeam("timef").addEntry ("TempodoFeast §c");
			player.getScoreboard().registerNewTeam("feastLoc").addEntry("Localização: §c");
			player.getScoreboard().registerNewTeam("position").addEntry("Posição: §6");
			player.getScoreboard().registerNewTeam("players").addEntry("Jogadores: §a");
		}
			player.getScoreboard().getTeam("game").setSuffix("" + ItemBuilder.format(GameTimer.timer));
			player.getScoreboard().getTeam("kit").setSuffix(new KitManager().getKitName(player));
			//player.getScoreboard().getTeam("timef").setSuffix("" + timer);
			player.getScoreboard().getTeam("feastLoc").setSuffix("" + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
			player.getScoreboard().getTeam("position").setSuffix(new PositionManager().getPosition(player) + "°");
			player.getScoreboard().getTeam("players").setSuffix("" + GameManager.getPlayerSize());
	}
	
	public void updateTeam(Player player, String teamID, String text) {
		if(player.getScoreboard().getTeam(teamID) == null) {
			System.out.println("Team null");
			return;
		}
		
		player.getScoreboard().getTeam(teamID).setSuffix(text);
	}
	
}
