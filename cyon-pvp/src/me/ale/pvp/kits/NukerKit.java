package me.ale.pvp.kits;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class NukerKit extends Kit {
	
	public NukerKit() {
		super("Nuker", Material.RED_MUSHROOM, new ItemStack(Material.RED_MUSHROOM), KitRarity.COMMON, Rank.NORMAL, "§7Clique no item e todos em sua volta", "§7irão se jogados para cima.");	
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
		
		player.getWorld().playEffect(player.getEyeLocation().add(0.5D, 0.0D, 0.5D), Effect.POTION_BREAK, 8201);
		player.getWorld().playEffect(player.getEyeLocation().add(0.5D, 0.0D, 0.5D), Effect.POTION_BREAK, 8201);
		for (Entity entity : player.getNearbyEntities(5, 5, 5)) {
			if (entity instanceof Player) {
				Player tar = (Player) entity;
	        tar.setVelocity(tar.getVelocity().add(new Vector(0.0D, 1.5D, 0.0D)));
	        tar.sendMessage("§cVocê foi lançado para cima.");
	      }
		}
	}
}
