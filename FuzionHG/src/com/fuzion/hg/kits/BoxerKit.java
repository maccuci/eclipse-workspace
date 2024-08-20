package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class BoxerKit extends Kit {
	
	public BoxerKit() {
		super("Boxer", "Dano", new ItemStack(Material.STONE_SWORD), Group.BETA, "§7Leve menos dano e dê mais dano.");
	}
	
	@EventHandler
	public void onHitBoxer(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof Player)) {
			Player player = (Player) event.getDamager();
			if ((hasKit(player)) && (player.getItemInHand().getType() == Material.AIR)) {
				event.setDamage(event.getDamage() + 2.0D);
			}
		}
	}

	@EventHandler
	public void onDamageBoxer(EntityDamageByEntityEvent event) {
		if ((event.getEntity() instanceof Player)) {
			Player player = (Player) event.getEntity();
			if ((hasKit(player)) && (event.getDamage() > 1.0D)) {
				event.setDamage(event.getDamage() - 1.0D);
			}
		}
	}
}
