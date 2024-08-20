package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class BoxerKit extends Kit {
	
	public BoxerKit() {
		super("Boxer", Material.QUARTZ, null, KitRarity.RARE, Rank.SIMPLE, "§7Receba menos dano e dê mais dano.");
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player))) {
			Player damager = (Player)event.getDamager();
			Player damaged = (Player)event.getEntity();
			
			if(event.getDamage() < 1.0) {
				return;
			}
			
			if(!hasKit(damager))
				return;
			
			damaged.damage(event.getDamage() + 2);
		}
	}
}
