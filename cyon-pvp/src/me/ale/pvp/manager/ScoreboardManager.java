package me.ale.pvp.manager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import me.ale.commons.api.scoreboard.ScoreboardAPI;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.manager.StatsManager;

public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		StatsManager manager = BukkitMain.getPlugin().getStatsManager();
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", "�3�lCyonPvP", DisplaySlot.SIDEBAR);
		addScore(player, "a", 9, "");
		addScore(player, "kills", 8, "�7Kills �8� �a" + manager.get(player.getUniqueId(), "kills"));
		addScore(player, "deaths", 7, "�7Deaths �8� �c" + manager.get(player.getUniqueId(), "deaths"));
		addScore(player, "money", 6, "�7Money �8� �2" + manager.get(player.getUniqueId(), "money"));
		addScore(player, "aa", 5, "");
		addScore(player, "faction", 4, "�7Faction �8� �bNone");
		addScore(player, "exp", 3, "�7Exp �8� �7" + manager.get(player.getUniqueId(), "exp"));
		addScore(player, "aaa", 2, "");
		addScore(player, "site", 1, "�7cyonpvp.com");
	}
	
	public void removeScoreboard(Player player) {
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", " ", DisplaySlot.SIDEBAR);
	}
	
	private void addScore(Player player, String scoreId, int score, String text) {
		String part1 = text;
		String part2 = "";
		if (text.length() > 12) {
			int a = 12;
			while (text.substring(0, a).endsWith("�"))
				--a;
			part1 = text.substring(0, a);
			part2 = text.substring(a, text.length());
			if (!part2.startsWith("�"))
				for (int i = part1.length(); i > 0; i--) {
					if (part1.substring(i - 1, i).equals("�") && part1.substring(i, i + 1) != null) {
						part2 = part1.substring(i - 1, i + 1) + part2;
						break;
					}
				}
			if (!part2.startsWith("�"))
				part2 = "�f" + part2;
		}
		String id = "";
		for (int i = 0; i < (score + "").length(); i++)
			id = id + "�" + (score + "").charAt(i);
		ScoreboardAPI.addScoreOnObjectiveToPlayer(player, "main", scoreId, score, id, part1, part2);
	}

	public void updateScore(Player player, String scoreId, String text) {
		String part1 = text;
		String part2 = "";
		if (text.length() > 12) {
			int a = 12;
			while (text.substring(0, a).endsWith("�"))
				--a;
			part1 = text.substring(0, a);
			part2 = text.substring(a, text.length());
			if (!part2.startsWith("�"))
				for (int i = part1.length(); i > 0; i--) {
					if (part1.substring(i - 1, i).equals("�") && part1.substring(i, i + 1) != null) {
						part2 = part1.substring(i - 1, i + 1) + part2;
						break;
					}
				}
			if (!part2.startsWith("�"))
				part2 = "�f" + part2;
		}
		ScoreboardAPI.updateScoreOnObjectiveToPlayer(player, "main", scoreId, part1, part2);
	}
}
