package me.ale.commons.api.admin;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ale.commons.CyonAPI;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.Rank;
import me.ale.commons.core.account.manager.RankManager;

public class AdminManager {
	
	private Set<Player> admin = new HashSet<>();
	
	public void changeMode(Player player, AdminMode mode) {
		switch (mode) {
		case PLAYER:
			if(admin.contains(player)) {
				admin.remove(player);
				player.setGameMode(mode.getGameMode());
				player.sendMessage(CyonAPI.WARNING_PREFIX + "Você saiu do modo admin.");
				player.sendMessage(CyonAPI.WARNING_PREFIX + "Você está visivel para TODOS.");
				setVanishPlayer(player, Rank.NORMAL);
			}
			break;
			
		case ADMIN:
			if(!admin.contains(player)) {
				admin.add(player);
				player.setGameMode(mode.getGameMode());
				player.sendMessage(CyonAPI.WARNING_PREFIX + "Você entrou no modo admin.");
				player.sendMessage(CyonAPI.WARNING_PREFIX + "Você está invisivel para FRIEND e abaixo.");
				setVanishPlayer(player, Rank.HELPER);
			}
			break;

		default:
			break;
		}
	}
	
	public void setVanishPlayer(Player player, Rank rank) {
		Bukkit.getOnlinePlayers().forEach(players -> {
			if(players.getUniqueId() == player.getUniqueId())
				return;
				RankManager manager = BukkitMain.getPlugin().getRankManager();
				if (rank != null && manager.getRank(players.getUniqueId()).ordinal() <= rank.ordinal()) {
					if(players.canSee(player)) {
						players.hidePlayer(player);
					}
					return;
				}
				if(!players.canSee(players)) {
					players.showPlayer(player);
				}
		});
	}
	
	public boolean isAdmin(Player player) {
		return player != null && admin.contains(player);
	}
}
