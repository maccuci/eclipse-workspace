package com.fuzion.kitpvp.kits;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.manager.kit.Kit;

public class AnchorKit extends Kit {
	
	public AnchorKit() {
		super("Anchor", new ItemStack(Material.ANVIL), Group.BETA, "§7Não leve knockback.");
	}
	
	@EventHandler
	public void onPlayerDamagePlayerListener(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			if (!p.hasMetadata("anchor")) {
				if (e.getDamager() instanceof Player) {
					Player d = (Player) e.getDamager();
					if (hasKit(p) || hasKit(d)) {
							p.getWorld().playSound(p.getLocation(), Sound.ANVIL_LAND, 0.15F, 1.0F);
							if (e.getDamage() < ((Damageable) p).getHealth()) {
								e.setCancelled(true);
								p.damage(e.getFinalDamage());
						}
					}
				}
			}
		}
	}
}
