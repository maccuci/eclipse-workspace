package me.ale.pvp.manager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import me.ale.pvp.api.ScoreboardAPI;
import me.ale.pvp.manager.kit.KitManager;

public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", " §d❃ §f§lKITPVP §d❃ ", DisplaySlot.SIDEBAR);
		addScore(player, "a", 10, "");
		addScore(player, "kit", 9, "Kit: §a" + new KitManager().getKitName(player));
		addScore(player, "faction", 8, "Faction: §aNenhum");
		addScore(player, "aa", 7, "");
		addScore(player, "kills", 6, "Kills: §e0");
		addScore(player, "deaths", 5, "Deaths: §e0");
		addScore(player, "streak", 4, "Streak: §e0");
		addScore(player, "exp", 3, "Exp: §b0");
		addScore(player, "aaa", 2, "");
		addScore(player, "site", 1, "§7www.unknown-kitpvp.net");
	}
	
	public void updateKit(Player player) {
		updateScore(player, "kit", "Kit: §a" + new KitManager().getKitName(player));
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

	private void updateScore(Player player, String scoreId, String text) {
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
