package me.tony.pvp.manager.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;


import me.tony.commons.api.scoreboard.ScoreboardAPI;

public class ScoreboardManager {
	
	@SuppressWarnings("deprecation")
	public void createScoreboard(Player player) {
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", "§f§lOlympus§4§lPvP", DisplaySlot.SIDEBAR);
		addScore(player, "a", 9, "");
		addScore(player, "kills", 8, "§aKills: §f0");
		addScore(player, "deaths", 7, "§cDeaths: §f0");
		addScore(player, "ks", 6, "§7KillStreak: §f0");
		addScore(player, "aa", 5, "");
		addScore(player, "coins", 4, "§6Coins: §f0");
		addScore(player, "level", 3, "§eNível: §f" + player.getLevel());
		addScore(player, "aaa", 2, "");
		addScore(player, "online", 1, "§9Online: §f" + Bukkit.getOnlinePlayers().length + "/" + Bukkit.getMaxPlayers());
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
