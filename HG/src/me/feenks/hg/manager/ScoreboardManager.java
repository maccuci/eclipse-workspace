package me.feenks.hg.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import lombok.Getter;
import me.feenks.hg.HardcoreGames;
import me.feenks.hg.api.item.ItemBuilder;
import me.feenks.hg.api.scoreboard.ScoreboardAPI;
import me.feenks.hg.utils.Scroller;

public class ScoreboardManager {
	
	@Getter public Scroller scroller = new Scroller();
	private String title;
	
	public void createScoreboard(Player player) {
		title = scroller.getText();
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", "  " + title + "  ", DisplaySlot.SIDEBAR);
		addScore(player, "a", 8, "");
		addScore(player, "timer", 7, "§7Iniciando em: §e" + ItemBuilder.format(HardcoreGames.getPlugin().getGameStage().getDefaultTimer()));
		addScore(player, "players", 6, "§7Jogadores: §e" + Bukkit.getOnlinePlayers().size());
		addScore(player, "aa", 5, "");
		addScore(player, "kit", 4, "§7Kit: §e" + new KitManager().getKitName(player));
		addScore(player, "kills", 3, "§7Kills: §e");
		addScore(player, "a", 2, "");
		addScore(player, "site", 1, "§7www.hardcoregames.net");
	}
	
	public void updateTitleText() {
		title = scroller.getText();
	}
	
	public void updateTitle(Player p) {
		ScoreboardAPI.setObjectiveDisplayNameToPlayer(p, "main", "  " + title + "  ");
	}
	
	public void removeScoreboard(Player player) {
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", " ", DisplaySlot.SIDEBAR);
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
