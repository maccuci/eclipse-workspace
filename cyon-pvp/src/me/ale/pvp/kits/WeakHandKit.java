package me.ale.pvp.kits;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class WeakHandKit extends Kit {
	
	public WeakHandKit() {
		super("WeakHand", Material.SKULL_ITEM, null, KitRarity.COMMON, Rank.NORMAL, "§7Encoste em algum jogador e deixe o mesmo", "§7mais fraco.");
	}
	
	@EventHandler
	public void onSnail(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (!(event.getDamager() instanceof Player))
			return;
		Player damager = (Player) event.getDamager();
		if (!hasKit(damager))
			return;
		Random r = new Random();
		int CHANCE = 75;
		Player damaged = (Player) event.getEntity();
		if (damaged instanceof Player) {
			if (r.nextInt(CHANCE) == 0) {
				damaged.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 5 * 20, 0));
			}
		}
	}

}
