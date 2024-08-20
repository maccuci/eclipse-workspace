package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class StomperKit extends Kit {
	
	public StomperKit() {
		super("Stomper", Material.IRON_BOOTS, null, KitRarity.MYSTIC, Rank.SIMPLE, "§7Converta o dano da sua queda em", "§7dano para os inimigos mais próximos.");
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
