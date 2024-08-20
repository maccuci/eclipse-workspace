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

public class SnailKit extends Kit {
	
	public SnailKit() {
		super("Snail", "Efeitos", new ItemStack(Material.WEB), Group.BETA, "§7Deixe seus inimigos mais lentos.");
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
				damaged.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 3 * 20, 0));
			}
		}
	}
}
