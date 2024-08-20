package com.fuzion.kitpvp.kits;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.manager.kit.Kit;

public class MagmaKit extends Kit {
	
	public MagmaKit() {
		super("Magma", new ItemStack(Material.LAVA_BUCKET), Group.BETA, "§7Deixe quem te enconstar pegando fogo.");
	}
	
	@EventHandler
	public void onMagma(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		Player damager = (Player) event.getDamager();
		if (!hasKit(damager))
			return;
		Random r = new Random();
		Player damaged = (Player) event.getEntity();
		if (damaged instanceof Player) {
			if (r.nextInt(65) == 0) {
				damaged.setFireTicks(100);
			}
		}
	}
}
