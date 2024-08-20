package me.tony.bedwars.manager.scoreboard;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import me.tony.bedwars.manager.team.Team;
import me.tony.bedwars.manager.team.TeamManager;


public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		ScoreboardAPI.createObjectiveToPlayer(player, "main", "  §e§lBEDWARS - TESTER  ", DisplaySlot.SIDEBAR);
		addScore(player, "a", 10, "");
		addScore(player, "kills", 9, "Kills: §a0");
		addScore(player, "deaths", 8, "Deaths: §a0");
		addScore(player, "position", 7, "Position: §e-/-");
		addScore(player, "aa", 6, "");
		addScore(player, "coins", 5, "Coins: §60");
		addScore(player, "xp", 4, "XP: §b0");
		addScore(player, "xp", 3, "Titles: §e0");
		addScore(player, "aaa", 2, "");
		addScore(player, "aaaa", 1, "§7www.bedwars.net");
	}
	
	public void debug(Player player) {
		ScoreboardAPI.createObjectiveToPlayer(player, "main", "  §e§lBEDWARS - TESTER  ", DisplaySlot.SIDEBAR);
		TeamManager teamManager = new TeamManager();
		addScore(player, "a", 10, "");
		addScore(player, "teams", 9, "Teams:");
		addScore(player, "aa", 8, "");
		for(int i = 0; i < teamManager.getTeamList().size(); i++) {
			Team team = teamManager.getTeamList().get(i);
			
			addScore(player, team.getName(), i, team.getTag() + "§r" + team.getName());
		}
		addScore(player, "aaaa", 1, "§7www.bedwars.net");
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
