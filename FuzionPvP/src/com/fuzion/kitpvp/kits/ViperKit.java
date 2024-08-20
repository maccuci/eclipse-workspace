package com.fuzion.kitpvp.kits;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.manager.kit.Kit;

public class ViperKit extends Kit {
	
	public ViperKit() {
		super("Viper", new ItemStack(Material.SPIDER_EYE), Group.BETA, "§7Deixe seus inimigos envenenados.");
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
				damaged.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 3 * 20, 0));
			}
		}
	}
}
