package me.ale.hg.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import me.ale.hg.api.item.ItemBuilder;
import me.ale.hg.api.scoreboard.ScoreboardAPI;
import me.ale.hg.manager.kit.KitManager;

public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", "  §e§lHG-A1  ", DisplaySlot.SIDEBAR);
		addScore(player, "a", 7, "");
		addScore(player, "timer", 6, "Iniciando em: §a" + ItemBuilder.format(300));
		addScore(player, "kit", 5, "Kit: §a" + new KitManager().getKitName(player));
		addScore(player, "aa", 4, "");
		addScore(player, "players", 3, "Jogadores: §a" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers());
		addScore(player, "aaa", 2, "");
		addScore(player, "site", 1, "§7www.hardcoregames.com");
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
