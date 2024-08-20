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

public class FiremanKit extends Kit {
	
	public FiremanKit() {
		super("Fireman", "Efeitos", new ItemStack(Material.LAVA_BUCKET), Group.BETA, "§7Leve consigo um balde de água e não leve dano para lava.");
		addItem(new ItemStack(Material.WATER_BUCKET, 1));
	}
	
	@EventHandler
	public void onFireman(EntityDamageEvent event) {
		if (!GameManager.isGame()) {
			return;
		}
		Entity entity = event.getEntity();
		if (!(entity instanceof Player)) {
			return;
		}
		Player fireman = (Player) entity;
		if (!hasKit(fireman)) {
			return;
		}
		EntityDamageEvent.DamageCause fire = event.getCause();
		if ((fire == EntityDamageEvent.DamageCause.FIRE) || (fire == EntityDamageEvent.DamageCause.LAVA) || (fire == EntityDamageEvent.DamageCause.FIRE_TICK) || (fire == EntityDamageEvent.DamageCause.LIGHTNING)) {
			event.setCancelled(true);
		}
	}
}
