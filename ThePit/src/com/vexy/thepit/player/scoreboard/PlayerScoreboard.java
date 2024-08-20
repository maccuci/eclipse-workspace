package com.vexy.thepit.player.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.vexy.thepit.Main;
import com.vexy.thepit.player.PitPlayer;
import com.vexy.thepit.player.PitPlayerManager;
import com.vexy.thepit.util.Formatter;
import com.vexy.thepit.util.ScoreboardAPI;

public class PlayerScoreboard {
	
	PitPlayerManager pitManager = Main.getPlugin().getPitPlayerManager();
	
	public void showScoreboard(Player player) {
		PitPlayer pitPlayer = pitManager.retriever(player.getUniqueId());
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", "  " + Scroller.getTitle() + "  ", DisplaySlot.SIDEBAR);
		addScore(player, "a", 8, " ");
		addScore(player, "level", 7, "§fLevel: " + Formatter.formatChat(pitPlayer));
		addScore(player, "xp", 6, "§fXP: §b" + pitPlayer.getXp());
		addScore(player, "pretige", 5, "§fPrestígio: §e" + Formatter.formatPresitige(pitPlayer.getPrestige()) + pitPlayer.getPrestige());
		addScore(player, "aa", 4, " ");
		addScore(player, "gold", 3, "§fMoedas: §6" + pitPlayer.getGold());
		addScore(player, "aaa", 2, " ");
		addScore(player, "site", 1, "§7www.thepit.com");
	}
	
	public void update(Player player) {
		PitPlayer pitPlayer = pitManager.retriever(player.getUniqueId());
		updateScore(player, "level", "§fLevel: " + Formatter.formatChat(pitPlayer));
		updateScore(player, "xp", "§fXP: §b" + pitPlayer.getXp());
		updateScore(player, "pretige", "§fPrestígio: " + Formatter.formatPresitige(pitPlayer.getPrestige()) + pitPlayer.getPrestige());
		updateScore(player, "gold", "§fMoedas: §6" + pitPlayer.getGold());
	}
	
	public void updateTitle(Player player) {
		ScoreboardAPI.setObjectiveDisplayNameToPlayer(player, "main", "  " + Scroller.getTitle() + "  ");
	}
	
	public void hideScoreboard(Player player) {
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
