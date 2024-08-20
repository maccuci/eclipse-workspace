package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class StomperKit extends Kit {
	
	public StomperKit() {
		super("Stomper", "Torre", new ItemStack(Material.IRON_BOOTS), Group.BETA, "§7Converta o dano de queda em dano para os inimigos ao redor.");
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player) {
			Player player = (Player)event.getEntity();
			
			if(event.getCause() == DamageCause.FALL) {
				if(!hasKit(player))
					return;
				
				for(Entity entity : player.getNearbyEntities(3, 3, 3)) {
					if(entity instanceof Player) {
						Player stomps = (Player)entity;
						if(!stomps.isSneaking()) {
							stomps.damage(event.getDamage() * 1.2D, player);
						}
					}
				}
				if(event.getDamage() > 4.0D) {
					event.setDamage(4.0D);
				}
			}
		}
	}
}
