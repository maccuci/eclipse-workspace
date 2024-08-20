package com.fuzion.hg.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;

import com.fuzion.core.bukkit.event.SchedulerEvent;
import com.fuzion.hg.manager.game.GameManager;

public class BorderListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBorder(SchedulerEvent event) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			if(isOnWarning(player) && !GameManager.isPreGame()) {
				player.sendMessage("§cVocê está próximo da borda.");
			} else {
				if ((isNotInBoard(player)) || (player.getLocation().getY() > 129.0D)) {
					if(GameManager.isPreGame()) {
						player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
						continue;
					} else {
						player.sendMessage("§cVocê está fora da borda do mundo!");
						EntityDamageEvent damageEvent = new EntityDamageEvent(player, DamageCause.CUSTOM, 4);
						if (damageEvent.isCancelled()) {
							damageEvent.setCancelled(false);
						}
						player.setLastDamageCause(damageEvent);
						player.damage(4.0D);
					}
				}
			}
		}
	}
	
	private boolean isNotInBoard(Player player) {
		return (player.getLocation().getBlockX() > 500) || (player.getLocation().getBlockX() < -500) || (player.getLocation().getBlockZ() > 500) || (player.getLocation().getBlockZ() < -500);
	}

	private boolean isOnWarning(Player player) {
		return (!isNotInBoard(player)) && ((player.getLocation().getBlockX() > 480) || (player.getLocation().getBlockX() < -480) || (player.getLocation().getBlockZ() > 480) || (player.getLocation().getBlockZ() < -480));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (isOnWarning(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (isOnWarning(event.getPlayer())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		if (isOnWarning(event.getPlayer())) {
			event.setCancelled(true);
		}
	}
}
