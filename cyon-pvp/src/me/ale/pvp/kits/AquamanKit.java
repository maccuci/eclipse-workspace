package me.ale.pvp.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class AquamanKit extends Kit {
	
	public AquamanKit() {
		super("Aquaman", Material.WATER_BUCKET, null, KitRarity.MYSTIC, Rank.SIMPLE, "§7Afunde na água e ganhe poderes do deus dos mares.");
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(!hasKit(player)) {
			return;
		}
		
		if(event.getTo().getBlock().getType() == Material.WATER) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, Integer.MAX_VALUE, Integer.MAX_VALUE));
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3, Integer.MAX_VALUE));
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 3, Integer.MAX_VALUE));
			player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, Integer.MAX_VALUE));
		} else {
			if(player.hasPotionEffect(PotionEffectType.WATER_BREATHING) || player.hasPotionEffect(PotionEffectType.INCREASE_DAMAGE) || player.hasPotionEffect(PotionEffectType.FAST_DIGGING) || player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
				player.removePotionEffect(PotionEffectType.WATER_BREATHING);
				player.removePotionEffect(PotionEffectType.INCREASE_DAMAGE);
				player.removePotionEffect(PotionEffectType.FAST_DIGGING);
				player.removePotionEffect(PotionEffectType.NIGHT_VISION);
			}
		}
	}
}
