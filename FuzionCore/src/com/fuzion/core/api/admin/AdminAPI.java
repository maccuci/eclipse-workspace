package com.fuzion.core.api.admin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;

public class AdminAPI {
	
	public enum AdminMode {
		ADMIN, PLAYER;
	}
	
	public static List<UUID> admin = new ArrayList<>();
	private HashMap<UUID, Group> vanishGroup = new HashMap<>();
	
	public void updateMode(Player player, AdminMode mode) {
		if(mode == AdminMode.ADMIN) {
			admin.add(player.getUniqueId());
			player.setGameMode(GameMode.CREATIVE);
			player.sendMessage("§aVocê entrou no modo ADMIN!");
			player.sendMessage("§7Você está invisivel para YOUTUBER e abaixo.");
			vanish(player, Group.YOUTUBER);
		} if(mode == AdminMode.PLAYER) {
			admin.remove(player.getUniqueId());
			player.setGameMode(GameMode.SURVIVAL);
			player.sendMessage("§aVocê saiu do modo ADMIN!");
			player.sendMessage("§7Você está visível para TODOS.");
			vanish(player, Group.NORMAL);
		}
	}
	
	public boolean isAdmin(Player p) {
		return admin.contains(p.getUniqueId());
	}

	public int playersInAdmin() {
		return admin.size();
	}
	
	@SuppressWarnings("deprecation")
	public void vanish(Player player, Group group) {
		GroupManager groupManager = new GroupManager();
		
		if(!isAdmin(player))
			return;
		
		if(vanishGroup.containsKey(player.getUniqueId()))
			vanishGroup.remove(player.getUniqueId());
		
		vanishGroup.put(player.getUniqueId(), group);
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online == player)
				return;
			
			if(!(groupManager.getGroup(online.getUniqueId()).ordinal() < group.ordinal())) {
				if(online.canSee(player)) {
					online.hidePlayer(player);
				}
			} else if(!online.canSee(player)) {
				 online.showPlayer(player);
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void updateVanish(Player player) {
		GroupManager groupManager = new GroupManager();
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online == player)
				return;
			Group group = vanishGroup.get(online.getUniqueId());
			
			if((group != null) && (groupManager.getGroup(online.getUniqueId()).ordinal() < group.ordinal())) {
				if (player.canSee(online)) {
					player.hidePlayer(online);
				}
			} else if(!player.canSee(online)) {
				 player.showPlayer(online);
			}
		}
	}

	public void removeAdmin(Player p) {
		admin.remove(p.getUniqueId());
	}
}
