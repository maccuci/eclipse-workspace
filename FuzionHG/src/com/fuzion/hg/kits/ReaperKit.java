package com.fuzion.hg.kits;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class ReaperKit extends Kit {
	
	public ReaperKit() {
		super("Reaper", "Efeitos", new ItemStack(Material.WOOD_HOE), Group.BETA, "§7Deixe seus inimigos em decomposição.");
	}
	
	@EventHandler
	public void onViper(EntityDamageByEntityEvent event) {
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
			if (r.nextInt(45) == 0) {
				damaged.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 3 * 20, 0));
			}
		}
	}
}
