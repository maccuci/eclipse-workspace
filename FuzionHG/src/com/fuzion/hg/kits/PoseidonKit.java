package com.fuzion.hg.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.fuzion.core.bukkit.event.SchedulerEvent;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class PoseidonKit extends Kit {
	
	public PoseidonKit() {
		super("Poseidon", "Celestial", new ItemStack(Material.WATER_BUCKET), Group.BETA, "§7Mergulhe na água para ganhar efeitos.");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPoseidon(SchedulerEvent e) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			if (hasKit(p)) {
				Block b = p.getLocation().getBlock();
				if (b.getType() == Material.WATER) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 80, 0));
				} else if (b.getType() == Material.STATIONARY_WATER) {
					p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 80, 0));
					p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 80, 1));
					p.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 80, 0));
				}
			}
		}
	}
}
