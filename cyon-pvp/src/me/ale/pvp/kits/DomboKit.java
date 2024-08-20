package me.ale.pvp.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class DomboKit extends Kit {
	
	private HashMap<Player, Integer> combos = new HashMap<>();
	
	public DomboKit() {
		super("Dombo", Material.STONE_SWORD, null, KitRarity.MYSTIC, Rank.SIMPLE, "§7Chegue a 5 hits dado em um jogador sem", "§7o mesmo te dar um hit e ganhe efeitos.");
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity().getType() != EntityType.PLAYER || event.getDamager().getType() != EntityType.PLAYER) {
			return;
		}

		if (!hasKit((Player)event.getDamager()))
			return;

		if (event.getDamager() instanceof Player || event.getEntity() instanceof Player) {
			Player damager = (Player) event.getDamager();
			Player damaged = (Player) event.getEntity();

			if (!combos.containsKey(damager)) {
				combos.put(damager, 0);
			}

			int value = combos.get(damager);
			combos.put(damager, value + 1);

			switch (value) {
			case 5:
				if (damager.hasPotionEffect(PotionEffectType.SPEED)) {
					damager.removePotionEffect(PotionEffectType.SPEED);
				}
				damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 0));
				break;

			case 8:
				if (damager.hasPotionEffect(PotionEffectType.SPEED)) {
					damager.removePotionEffect(PotionEffectType.SPEED);
				}
				damager.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 1));
				break;

			case 10:
				new BukkitRunnable() {
					int combo = 0;

					@Override
					public void run() {
						combo++;
						if (combo < 10) {
							damaged.damage(2);
							damaged.setVelocity(damaged.getEyeLocation().getDirection().multiply(-0.5).setY(0.5));
						} else {
							cancel();
						}
					}
				}.runTaskTimer(Main.getPlugin(), 0, 5);
				break;
			}
			combos.remove(damager);
		}
	}
}
