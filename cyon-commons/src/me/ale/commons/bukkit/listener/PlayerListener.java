package me.ale.commons.bukkit.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.ale.commons.CyonAPI;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.bukkit.event.faction.PlayerJoinFactionEvent;
import me.ale.commons.bukkit.event.league.PlayerLeagueChangeEvent;
import me.ale.commons.bukkit.menu.FactionMenu;
import me.ale.commons.bukkit.menu.FactionMenu.FactionSettings;
import me.ale.commons.bukkit.permission.PermissionBukkitManager;
import me.ale.commons.core.account.Rank;
import me.ale.commons.core.account.manager.FactionManager;
import me.ale.commons.core.account.manager.PackColletionManager;
import me.ale.commons.core.account.manager.StatsManager;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		BukkitMain.getPlugin().getStatsManager().create(player.getName(), player.getUniqueId());
		BukkitMain.getPlugin().getRankManager().create(player.getName(), player.getUniqueId());
		new PackColletionManager().create(player.getName(), player.getUniqueId());
		
		BukkitMain.getPlugin().getRankManager().set(player.getUniqueId(), "playerrank", Rank.OWNER.name());
		PermissionBukkitManager manager = new PermissionBukkitManager();
		manager.refreshPerms(player);
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		Scoreboard board = e.getPlayer().getScoreboard();
		if (board != null) {
			for (Team t : board.getTeams()) {
				t.unregister();
				t = null;
			}
			for (Objective ob : board.getObjectives()) {
				ob.unregister();
				ob = null;
			}
			board = null;
		}
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	@EventHandler
	public void onPlayerLeagueChange(PlayerLeagueChangeEvent event) {
		if (event.getPlayer() != null && event.getNewLeague().ordinal() > event.getOldLeague().ordinal()) {
			event.getPlayer().sendMessage(CyonAPI.WARNING_PREFIX + "Você evoluiu para a league " + event.getNewLeague().getColor() + event.getNewLeague().name());
			Bukkit.getOnlinePlayers().forEach(players -> {
				players.sendMessage(CyonAPI.WARNING_PREFIX + "O jogador " + event.getPlayer().getName() + " evoluiu para a league " + event.getNewLeague().getColor() + event.getNewLeague().name());
			});
		}
	}
	
	@EventHandler
	public void onPlayerJoinFaction(PlayerJoinFactionEvent event) {
		Player player = event.getPlayer();
		player.sendMessage("§aMensagem de boas vindas:");
		player.sendMessage("§7" + event.getMessage());
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		FactionManager f = BukkitMain.getPlugin().getFactionManager();
		StatsManager stats = BukkitMain.getPlugin().getStatsManager();
		
		if(FactionMenu.settings.containsKey(player)) {
			if(FactionMenu.settings.get(player) == FactionSettings.MESSAGE) {
				if(f.hasOwnerPlayer(player)) {
					f.getFaction(player).setMessage(event.getMessage());
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Você alterou a mensagem de boas vindas da faction.");
					event.setCancelled(true);
					FactionMenu.settings.remove(player);
					FactionMenu.settings(player);
					return;
				} else {
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Você não pode executar essa ação.");
				}
			} else if(FactionMenu.settings.get(player) == FactionSettings.MONEY) {
				Integer integer = 0;
				try {
					integer = Integer.valueOf(event.getMessage());
				} catch (Exception e) {
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Você só pode inserir apenas números.");
					return;
				}
				if(stats.get(player.getUniqueId(), "money") <= 100) {
					int value = stats.get(player.getUniqueId(), "money") - integer;
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Você não pode adicionar money na faction ainda. Faltam " + value + " de money.");
					return;
				}
				int value = stats.get(player.getUniqueId(), "money") - integer;
				stats.set(player.getUniqueId(), "money", value);
				f.updateFaction(player, 0, 0, integer);
				player.sendMessage("§aVocê depositou " + integer + " de money para a faction.");
				event.setCancelled(true);
				FactionMenu.settings(player);
				FactionMenu.settings.remove(player);
				return;
			}
		}
	}
}
