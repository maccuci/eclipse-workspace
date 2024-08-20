package br.com.houldmc.rankup.manager.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.api.scoreboard.ScoreboardAPI;
import br.com.houldmc.rankup.manager.rank.RankManager;
import br.com.houldmc.rankup.manager.rank.list.RankList;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;
import br.com.houldmc.rankup.player.clan.ClanManager;

public class ScoreboardManager {
	
	public void createScoreboard(Player player) {
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		RankManager rankManager = new RankManager();
		ClanManager clanManager = new ClanManager();
		
		ScoreboardAPI.createObjectiveIfNotExistsToPlayer(player, "main", "  §5§lRANKUP MEDIEVAL  ", DisplaySlot.SIDEBAR);
		addScore(player, "a", 11, "");
		addScore(player, "rank", 10, "Rank: " + rankManager.getRankName(player));
		if(clanManager.hasPlayerClan(player.getUniqueId())) {
			addScore(player, "clan", 9, "Clan: §a" + new ClanManager().getClanName(player));
		} else {
			addScore(player, "clan", 9, "§cVocê não possui um clan");
		}
		addScore(player, "aa", 8, "");
		addScore(player, "money", 7, "Moedas: §6" + ItemBuilder.format(rankUPPlayer.getMoney()));
		addScore(player, "cash", 6, "Cash: §e" + rankUPPlayer.getCash());
		addScore(player, "souls", 5, "Gemas: §2" + rankUPPlayer.getSouls());
		addScore(player, "aaa", 4, "");
		addScore(player, "players", 3, "Jogadores: §b" + Bukkit.getOnlinePlayers().size());
		addScore(player, "aaaa", 2, "");
		addScore(player, "footer", 1, "§7houldmc.com.br");
	}
	
	public void updateRank(Player player, RankList rank) {
		updateScore(player, "rank", "Rank: " + rank.getTag());
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
