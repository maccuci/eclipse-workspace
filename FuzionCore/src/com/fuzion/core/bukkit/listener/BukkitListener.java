package com.fuzion.core.bukkit.listener;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.bukkit.command.ModeratorCommand;
import com.fuzion.core.bukkit.event.SchedulerEvent;
import com.fuzion.core.bukkit.event.SchedulerEvent.SchedulerType;
import com.fuzion.core.bukkit.event.stats.LoadStats;
import com.fuzion.core.bukkit.event.stats.StatsLoadEvent;
import com.fuzion.core.bukkit.manager.ReportManager;
import com.fuzion.core.master.account.Account;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.AccountManager;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.KitFavoritesManager;
import com.fuzion.core.master.account.management.PunishManager;
import com.fuzion.core.master.account.management.StatsManager;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.core.master.account.management.PunishManager.Durations;
import com.fuzion.core.master.account.management.PunishManager.Types;

public class BukkitListener implements Listener {
	
	@EventHandler
	public void playerLoginEvent(PlayerLoginEvent event) {
		PunishManager manager = new PunishManager(null, null, event.getPlayer(), null, 0);
		if(manager.isPunished() && manager.getType().equals(Types.BAN)) {
			if(manager.getDuration().equals(Durations.PERMANENT)) {
				event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "§cSua conta está suspensa permanentemente!\n\nMotivo: " + manager.get("reason") + "\n\nCompre seu unban em:\nloja.fuzion-network.com");
			} else {
				long expire = manager.getExpire();
				int seconds = (int)((expire - System.currentTimeMillis()) / 1000L);
				if(seconds > 0) {
					event.disallow(PlayerLoginEvent.Result.KICK_BANNED, "§cSua conta está suspensa temporariamente!\n\nMotivo: " + manager.get("reason") + "\nExpira em: " + ItemBuilder.getMessage(expire) + "\n\nCompre seu unban em:\nloja.fuzion-network.com");
				} else {
					manager.unBan();
					Bukkit.getConsoleSender().sendMessage("§eJogador " + event.getPlayer().getName() + " foi desbanido automaticamente."); 
				}
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		UUID uuid = player.getUniqueId();
		Account account = new Account(player, uuid, player.getName(), 0, 0, 0, 0, 0);
		new GroupManager().create(account.getNickname(), account.getUniqueId());
		StatsLoadEvent loadEvent = new StatsLoadEvent(player, new StatsManager(), LoadStats.SUCESS);
		AccountManager.craftAccount(player);
		new KitFavoritesManager().load(player);
		Bukkit.getPluginManager().callEvent(loadEvent);
		PunishManager punish = new PunishManager(null, null, player, null, 0);
		if(punish.isPunished() && punish.getType().equals(Types.MUTE)) {
			if(punish.getDuration().equals(Durations.TEMPORARY)) {
				long expire = punish.getExpire();
				int seconds = (int)((expire - System.currentTimeMillis()) / 1000L);
				if(seconds <= 0) {
					player.sendMessage("§aSeu " + Types.MUTE.name() + " temporário expirou!");
					punish.unMute();
				}
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		GroupManager groupManager = new GroupManager();
		TagManager tagManager = new TagManager(player);
		
		if(ModeratorCommand.staffChat.contains(player)) {
			event.setCancelled(true);
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(groupManager.hasGroupPermission(online.getUniqueId(), Group.YOUTUBERPLUS)) {
					if(ModeratorCommand.staffChat.contains(online)) {
						online.sendMessage("§e§l[STAFF] " + tagManager.getTag().getName() + player.getDisplayName() + "§f: " + event.getMessage().replace("%", "%%"));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(ModeratorCommand.freeze.contains(player)) {
			event.setTo(player.getLocation());
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		if(event.getEntity().getType() == EntityType.PLAYER) {
			Player player = (Player)event.getEntity();
			
			if(ModeratorCommand.freeze.contains(player)) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onDamageEntity(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		if(event.getEntity().getType() == EntityType.PLAYER) {
			Player player = (Player)event.getEntity();
			
			if(ModeratorCommand.freeze.contains(player)) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent event) {
/*		Player player = event.getPlayer();
		
		if(ModeratorCommand.freeze.contains(player)) {
			event.setCancelled(true);
			return;
		}*/
	}
	
	@EventHandler
	public void onStats(StatsLoadEvent event) {
		StatsManager statsManager = event.getStatsManager();
		Account account = new Account(event.getPlayer(), event.getPlayer().getUniqueId(), event.getPlayer().getName(), 0, 0, 0, 0, 0);
		
		if(event.getLoadStats() == LoadStats.FAILED) {
			return;
		}
		statsManager.create(account.getNickname(), account.getUniqueId());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		
		if(ModeratorCommand.freeze.contains(player)) {
			ModeratorCommand.freeze.remove(player);
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onReports(SchedulerEvent event) {
		if(event.getType() != SchedulerType.MINUTE)
			return;
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(new GroupManager().hasGroupPermission(player.getUniqueId(), Group.TRIAL)) {
				if(ReportManager.reports.size() <= 0)
					return;
				player.sendMessage("§bVocê possui §3§l" + ReportManager.reports.size() + " REPORTS §baté o momento.");
			}
		}
	}
}
