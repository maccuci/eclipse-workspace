package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;

public class AntiTower extends Kit {
	
	public AntiTower() {
		super("AntiTower", "Torre", new ItemStack(Material.GOLD_BOOTS), Group.BETA, "§7Não leve dano de queda de stompers.");
	}
	
	@EventHandler
	public void onStomper(EntityDamageEvent event) {
		if (!GameManager.isGame()) {
			return;
		}
		if (event.isCancelled()) {
			return;
		}
		Entity stomper = event.getEntity();
		if (!(stomper instanceof Player)) {
			return;
		}
		Player stomped = (Player) stomper;
		if (!hasKit(stomped)) {
			return;
		}
		EntityDamageEvent.DamageCause cause = event.getCause();
		if (cause != EntityDamageEvent.DamageCause.FALL) {
			return;
		}
		double dmg = event.getDamage();
		if (dmg > 4.0D) {
			event.setCancelled(true);
			stomped.damage(4.0D);
		}
	}
}
