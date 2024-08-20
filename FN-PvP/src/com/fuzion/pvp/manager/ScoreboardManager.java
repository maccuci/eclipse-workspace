package com.fuzion.pvp.manager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.pvp.api.ScoreboardAPI;

public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", "  §b§lUNKNOWN  ", DisplaySlot.SIDEBAR);
		addScore(player, "a", 9, " ");
		addScore(player, "welcome", 8, "§7Bem vindo ao modo Splah!");
		addScore(player, "welcome2", 7, "§7Saia do spawn para jogar.");
		addScore(player, "aa", 6, " ");
		addScore(player, "molhou", 5, "§fMolhou: §a0 Vezes");
		addScore(player, "molhado", 4, "§fMolhado: §a0 Vezes");
		addScore(player, "coins", 3, "§fCoins: §60");
		addScore(player, "aaa", 2, " ");
		addScore(player, "site", 1, "§awww.splash.com.br");
	}
	
	public void clearScoreboard(Player player) {
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "clear", " ", DisplaySlot.SIDEBAR);
	}
	
	private void addScore(Player player, String scoreId, int score, String text) {
		String part1 = text;
		String part2 = "";
		if (text.length() > 12) {
			int a = 12;
			while (text.substring(0, a).endsWith("§"))
				--a;
			part1 = text.substring(0, a);
			part2 = text.substring(a, text.length());
			if (!part2.startsWith("§"))
				for (int i = part1.length(); i > 0; i--) {
					if (part1.substring(i - 1, i).equals("§") && part1.substring(i, i + 1) != null) {
						part2 = part1.substring(i - 1, i + 1) + part2;
						break;
					}
				}
			if (!part2.startsWith("§"))
				part2 = "§f" + part2;
		}
		String id = "";
		for (int i = 0; i < (score + "").length(); i++)
			id = id + "§" + (score + "").charAt(i);
		ScoreboardAPI.addScoreOnObjectiveToPlayer(player, "main", scoreId, score, id, part1, part2);
	}

	public void updateScore(Player player, String scoreId, String text) {
		String part1 = text;
		String part2 = "";
		if (text.length() > 12) {
			int a = 12;
			while (text.substring(0, a).endsWith("§"))
				--a;
			part1 = text.substring(0, a);
			part2 = text.substring(a, text.length());
			if (!part2.startsWith("§"))
				for (int i = part1.length(); i > 0; i--) {
					if (part1.substring(i - 1, i).equals("§") && part1.substring(i, i + 1) != null) {
						part2 = part1.substring(i - 1, i + 1) + part2;
						break;
					}
				}
			if (!part2.startsWith("§"))
				part2 = "§f" + part2;
		}
		ScoreboardAPI.updateScoreOnObjectiveToPlayer(player, "main", scoreId, part1, part2);
	}

}