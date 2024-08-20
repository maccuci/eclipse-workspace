package com.fuzion.kitpvp.manager.protection;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ProtectionListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ProtectionManager.addProtectionToPlayer(player);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location playerLocation = player.getLocation();
		Location checkLocation = player.getWorld().getSpawnLocation();
		double distance = 150;
		
		if(playerLocation.distance(checkLocation) > distance) {
			if(ProtectionManager.hasProtected(player)) {
				ProtectionManager.removeProtectionToPlayer(player);
			}
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(!(event.getEntity() instanceof Player))
			return;
		
		if(event.getEntityType() == EntityType.PLAYER) {
			Player player = (Player)event.getEntity();
			
			if(ProtectionManager.hasProtected(player)) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onDamagePlayer(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof Player) && (!(event.getDamager() instanceof Player)))
			return;
		
		if(event.getEntityType() == EntityType.PLAYER) {
			Player player = (Player)event.getEntity();
			
			if(ProtectionManager.hasProtected(player)) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent event) {
		Player player = (Player)event.getCaught();
		
		if(ProtectionManager.hasProtected(player)) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		
		ProtectionManager.addProtectionToPlayer(player);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		ProtectionManager.removeProtectionToPlayer(player);
	}
}
