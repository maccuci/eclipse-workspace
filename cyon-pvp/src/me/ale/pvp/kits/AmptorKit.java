package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class AmptorKit extends Kit {
	
	public AmptorKit() {
		super("Amptor", Material.PISTON_BASE, null, KitRarity.MYSTIC, Rank.SIMPLE, "§7Ao se agachar nada pode te ferir.");
	}
	
	@EventHandler
	public void onShift(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		
		if(!hasKit(player))
			return;
		
		player.damage(0);
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.PLAYER) {
			return;
		}

		if (!hasKit((Player)event.getEntity()))
			return;

		if (event.getDamager() instanceof Player || event.getEntity() instanceof Player) {
			Player damaged = (Player)event.getEntity();
			if (hasKit(damaged)) {
				damaged.damage(0);
				event.setCancelled(true);
			}
		}
	}
}
