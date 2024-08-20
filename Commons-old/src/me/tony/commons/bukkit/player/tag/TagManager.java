package me.tony.commons.bukkit.player.tag;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.tony.commons.api.item.ItemBuilder;
import me.tony.commons.bukkit.player.tag.list.TagTypes;

public class TagManager {
	
	public static char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	private static final Map<UUID, TagTypes> playerTag = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	public void updateTag(Player player) {
		for(Player players : Bukkit.getOnlinePlayers()) {
			if(Bukkit.getOnlinePlayers().length == 0)
				return;
			Scoreboard scoreboard = players.getScoreboard();
			if(scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
				scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			}
			Team team = scoreboard.getTeam(alphabet[getTag(player).ordinal()] + player.getName());
			if(team == null) {
				team = scoreboard.registerNewTeam(alphabet[getTag(player).ordinal()] + player.getName());
			}
			team.setPrefix(getTag(player).getPrefix());
			team.addPlayer(player);
			players.setScoreboard(scoreboard);
			player.setScoreboard(scoreboard);
		}
	}
	
	public void updateTagCommand(Player player, TagTypes tag) {
		try {
			if(getFormattedFromColors(player.getLevel()) == null || tag.getPrefix() == null || player == null) {
				player.getPlayer().kickPlayer("§cSua conta foi corrompida. Entre novamente para atualizar.");
			}
			setTag(player, tag);
			updateTag(player);
		} catch (Exception e) {
		}
	}
	
	public boolean hasTag(Player player, TagTypes tag) {
		return getTag(player).equals(tag);
	}
	
	public TagTypes getTag(Player player) {
		return playerTag.get(player.getUniqueId());
	}
	
	public TagTypes getTag(UUID uniqueId) {
		return playerTag.get(uniqueId);
	}
	
	public void setTag(Player player, TagTypes tag) {
		setTag(player.getUniqueId(), tag);
	}
	
	public void setTag(UUID uniqueId, TagTypes tag) {
		playerTag.put(uniqueId, tag);
	}
	
	private String getFormattedFromColors(int level) {
		String format = "";
		
		if(level <= 10) {
			format = "§8(§a§l" + level + "§8) ";
		} else if(level <= 20) {
			format = "§8(§e§l" + level + "§8) ";
		} else if(level <= 30) {
			format = "§8(§6§l" + level + "§8) ";
		} else if(level <= 40) {
			format = "§8(§c§l" + level + "§8) ";
		} else if(level <= 50) {
			format = "§8(§4§l" + level + "§8) ";
		} else if(level >= 60 && level <= 1000) {
			format = "§8(§5§l" + level + "§8) ";
		} else if(level >= 1000) {
			format = "§8(§b§l" + ItemBuilder.formatNumber(level) + "§8) ";
		}
		
		return format;
	}
	
}
