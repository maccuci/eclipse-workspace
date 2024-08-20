package com.fuzion.hg.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import com.fuzion.hg.manager.game.GameManager;

public class InvincibilityListener implements Listener {
	
	private boolean isInvincibility() {
		return GameManager.isInvincibility();
	}

	@EventHandler
	public void onRegen(EntityRegainHealthEvent event) {
		if (isInvincibility())
			event.setCancelled(true);
	}

	@EventHandler
	public void onFoodLevelChange(FoodLevelChangeEvent event) {
		if (isInvincibility())
			event.setCancelled(true);
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (isInvincibility())
			event.setCancelled(true);
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (isInvincibility())
			event.setCancelled(true);
	}

}
