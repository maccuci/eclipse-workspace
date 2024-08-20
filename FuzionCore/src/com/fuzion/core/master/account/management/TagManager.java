package com.fuzion.core.master.account.management;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.fuzion.core.api.json.JSONChatClickEventType;
import com.fuzion.core.api.json.JSONChatExtra;
import com.fuzion.core.api.json.JSONChatHoverEventType;
import com.fuzion.core.api.json.JSONChatMessage;
import com.fuzion.core.master.account.Tag;

import net.md_5.bungee.api.ChatColor;

public class TagManager {
	
	private Player player;
	
	public TagManager(Player player) {
		this.player = player;
	}
	
	public static char[] alphabet = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
	
	public static HashMap<Player, Tag> playerTag = new HashMap<>();
	
	public void setTag(Tag tag) {
		playerTag.put(this.player, tag);
	}
	
	public Tag getTag() {
		return playerTag.get(this.player);
	}
	
	public boolean hasTag(Tag tag) {
		return getTag().equals(tag);
	}
	
	public boolean hasTagPermission(Tag tag) {
		return new GroupManager().hasGroupPermission(player.getUniqueId(), tag.getGroup());
	}
	
	public void findTag() {
		setTag(Tag.valueOf(new GroupManager().getGroup(player.getUniqueId()).name()));
	}
	
	public void sendTags() {
		ArrayList<Tag> list = new ArrayList<>();
		JSONChatMessage chatMessage = new JSONChatMessage(ChatColor.GREEN + "Selecione uma " + ChatColor.WHITE + "tag" + ChatColor.GREEN + " dentre as listadas abaixo:\n", null, null);
		for(Tag tag : Tag.values()) {
			JSONChatExtra chatExtra = new JSONChatExtra(String.valueOf(tag.equals(Tag.NORMAL) ? "§7§lNORMAL" : ", ") + tag.getName());
			chatExtra.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, ChatColor.YELLOW + "Exemplo: " + tag.getName() + player.getName() + "\n" + ChatColor.GOLD + "Clique para selecionar!");
			chatExtra.setClickEvent(JSONChatClickEventType.RUN_COMMAND, "/tag " + tag.name());
			if(hasTagPermission(tag)) {
				list.add(tag);
				chatMessage.addExtra(chatExtra);
			}
		}
		if(list.size() == 0) {
			this.player.sendMessage(ChatColor.RED + "Você não possui nenhuma " + ChatColor.WHITE + "tag" + ChatColor.RED + " disponível.");
			return;
		}
		JSONChatExtra finalPoint = new JSONChatExtra(ChatColor.RESET + ".");
		finalPoint.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, "");
		chatMessage.addExtra(finalPoint);
		chatMessage.sendToPlayer(this.player);
		return;
	}
	
	@SuppressWarnings("deprecation")
	public void update() {
		for(Player online : Bukkit.getOnlinePlayers()) {
			Scoreboard scoreboard = online.getScoreboard();
			if(scoreboard.equals(Bukkit.getScoreboardManager().getMainScoreboard())) {
				scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			}
			Team team = scoreboard.getTeam(alphabet[getTag().ordinal()] + this.player.getName());
			if(team == null) {
				team = scoreboard.registerNewTeam(alphabet[getTag().ordinal()] + this.player.getName());
			}
			team.setPrefix(getTag().getName());
			team.addPlayer(this.player);
			online.setScoreboard(scoreboard);
			this.player.setScoreboard(scoreboard);
		}
	}

}
