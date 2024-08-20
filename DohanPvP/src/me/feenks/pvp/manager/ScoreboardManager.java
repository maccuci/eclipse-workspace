package me.feenks.pvp.manager;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import me.feenks.pvp.Main;

public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		Main.getPlugin().getCustomScoreboard().createObjectiveIfNotExistsToPlayer(player, "main", "§5§lDohanPvP", DisplaySlot.SIDEBAR);
		addScore(player, "a", 10, " ");
		addScore(player, "kills", 9, "Kills §7» §a0");
		addScore(player, "deaths", 8, "Deaths §7» §c0");
		addScore(player, "streak", 7, "Killstreak §7» §e0");
		addScore(player, "xp", 6, "XP §7» §b0");
		addScore(player, "aa", 5, " ");
		addScore(player, "kit", 4, "Kit §7» §6Nenhum");
		addScore(player, "rank", 3, "Rank §7» Unranked");
		addScore(player, "aaa", 2, " ");
		addScore(player, "site", 1, "§7dohanmc.com.br");
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
		Main.getPlugin().getCustomScoreboard().addScoreOnObjectiveToPlayer(player, "main", scoreId, score, id, part1, part2);
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
		Main.getPlugin().getCustomScoreboard().updateScoreOnObjectiveToPlayer(player, "main", scoreId, part1, part2);
	}

}
