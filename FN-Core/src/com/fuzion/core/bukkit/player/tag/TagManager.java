package com.fuzion.core.bukkit.player.tag;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.fuzion.core.FuzionAPI;

public class TagManager {
	
	public static char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	public static final HashMap<Player, Tag> playerTag = new HashMap<>();
	
	public void setTag(Player player, Tag tag) {
		playerTag.put(player, tag);
	}
	
	public Tag getTag(Player player) {
		return playerTag.get(player);
	}
	
	public void findTag(Player player) {
		setTag(player, Tag.valueOf(FuzionAPI.getAccountManager().getAccount(player).getGroup().name()));
	}
	
	public boolean hasTag(Player player, Tag tag) {
		return getTag(player).equals(tag);
	}
	
	public boolean hasTagPermission(Player player, Tag tag) {
		return FuzionAPI.getAccountManager().getAccount(player).hasGroupPermission(tag.getGroup());
	}
	
	@SuppressWarnings("deprecation")
	public void update(Player player) {
		for(Player online : Bukkit.getOnlinePlayers()) {
			Scoreboard scoreboard = online.getScoreboard();
			if(scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
				scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			}
			Team team = scoreboard.getTeam(alphabet[getTag(player).ordinal()] + player.getName());
			if(team == null) {
				team = scoreboard.registerNewTeam(alphabet[getTag(player).ordinal()] + player.getName());
			}
			team.setPrefix(getTag(player).getPrefix());
			team.addPlayer(player);
			online.setScoreboard(scoreboard);
			player.setScoreboard(scoreboard);
		}
	}
}
