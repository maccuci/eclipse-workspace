package com.fuzion.kitpvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.manager.kit.Kit;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;

public class LevitationKit extends Kit {
	
	public LevitationKit() {
		super("Levitation", new ItemStack(Material.TORCH), Group.BETA, "§7Entre na água para ganhar efeitos.");
		addItem(new ItemStack(Material.TORCH));
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
		if (CooldownAPI.isInCooldown(player.getUniqueId(), getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		
		event.setCancelled(true);
		for(Entity entity : player.getNearbyEntities(6, 5, 6)) {
			if((entity instanceof Player)) {
				Player other = (Player)entity;
				
				Vector vector = other.getLocation().getDirection().multiply(0).setY(0.8D);
				other.setVelocity(vector);
				CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 30);
				cooldown.start();
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
	              public void run() {
	            	  Vector vector = other.getLocation().getDirection().multiply(0).setY(1.2D);
	            	  other.setVelocity(vector);
	              }
	            }, 15L);
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
		              public void run() {
		            	  Vector vector = other.getLocation().getDirection().multiply(0).setY(1.2D);
		            	  other.setVelocity(vector);
		              }
		        }, 20L);
			}
		}
	}
}
