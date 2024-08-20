package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class VikingKit extends Kit {
	
	public VikingKit() {
		super("Viking", "Dano", new ItemStack(Material.STONE_AXE), Group.BETA, "§7Seja mais forte com o seu machado.");
	}
	
	@EventHandler
	public void onDamageViking(EntityDamageByEntityEvent event) {
		if ((event.getDamager() instanceof Player)) {
			Player player = (Player) event.getDamager();
			if (hasKit(player)) {
				ItemStack item = ((Player) event.getDamager()).getItemInHand();
				if ((item != null) && (item.getType().name().contains("AXE"))) {
					event.setDamage(event.getDamage() + 1.5D);
				}
			}
		}
	}
}
