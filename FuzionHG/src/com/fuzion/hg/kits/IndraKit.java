package com.fuzion.hg.kits;

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

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class IndraKit extends Kit {
	
	public IndraKit() {
		super("Indra", "Místico", new ItemStack(Material.RECORD_11), Group.BETA, "§7Clique no seu item para aumenta a sua velocidade e a sua força.");
		addItem(new ItemStack(Material.RECORD_11));
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
		ItemStack ITEM = getItems().iterator().next().clone();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.isInCooldown(player.getUniqueId(), getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		event.setCancelled(true);
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 10 * 20, 3));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 10 * 20, 3));
		player.playSound(player.getLocation(), Sound.WITHER_SPAWN, 6, 6);
		for(Entity entity : player.getNearbyEntities(6, 6, 6)) {
			if(entity instanceof Player) {
				Player near = (Player)entity;
				near.playSound(near.getLocation(), Sound.WITHER_SPAWN, 6, 6);
			}
		}
		CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 30);
		cooldown.start();
	}
}
