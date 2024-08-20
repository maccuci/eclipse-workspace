package com.fuzion.kitpvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.manager.kit.Kit;

public class StomperKit extends Kit {
	
	public StomperKit() {
		super("Stomper", new ItemStack(Material.IRON_BOOTS), Group.BETA, "§7Converta o dano da sua queda em dano para os inimigos ao redor.");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onStomper(EntityDamageEvent event) {
		Entity entityStomper = event.getEntity();
		if (!(entityStomper instanceof Player))
			return;
		Player stomper = (Player) entityStomper;
		if (!hasKit(stomper))
			return;
		DamageCause cause = event.getCause();
		if (cause != DamageCause.FALL)
			return;
		double dmg = event.getDamage();
		boolean hasPlayer = false;
		for (Player stompado : Bukkit.getOnlinePlayers()) {
			if (stompado.getUniqueId() == stomper.getUniqueId())
				continue;
			if (stompado.getLocation().distance(stomper.getLocation()) > 5) {
				continue;
			}
			double dmg2 = dmg * (7 / 10d);
			if (stompado.isSneaking() && dmg2 > 8)
				dmg2 = 8;
			stompado.damage(dmg2, stomper);
			hasPlayer = true;
		}
		if (hasPlayer) {
			stomper.getWorld().playSound(stomper.getLocation(), Sound.ANVIL_LAND, 1, 1);
		}
	}
}
