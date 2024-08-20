package me.ale.pvp.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class ReverseKit extends Kit {
	
	public ReverseKit() {
		super("Reverse", Material.DRAGON_EGG, new ItemStack(Material.DRAGON_EGG), KitRarity.MYSTIC, Rank.SIMPLE, "§7Roube a velocidade de todos ao redor de 4 blocos", "§7e tenha a velocidade de todos em você.");	
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (!hasKit(player))
			return;
		Action a = event.getAction();
		ItemStack item = player.getItemInHand();
		if (!a.name().contains("RIGHT") && !a.name().contains("LEFT"))
			return;
		if (item == null)
			return;
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.hasCooldown(player, getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		event.setCancelled(true);
		for(Entity entity : player.getNearbyEntities(4, 4, 4)) {
			if((entity instanceof Player)) {
				Player other = (Player)entity;
				List<Player> list = new ArrayList<>();
				list.add(other);
				int value = list.size();
				
				other.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 10 * 20, 3));
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, value));
			}
		}
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 40L));
	}
}
