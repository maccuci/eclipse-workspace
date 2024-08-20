package com.fuzion.kitpvp.kits;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.fuzion.kitpvp.manager.kit.Kit;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;

public class ThorKit extends Kit {
	
	public ThorKit() {
		super("Thor", new ItemStack(Material.WOOD_AXE), Group.BETA, "§7Puxe os inimigos até você com a sua vara de pesca.");
		addItem(new ItemStack(Material.WOOD_AXE));
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
		Bat bat = (Bat)player.getWorld().spawnEntity(event.getClickedBlock().getLocation().add(0.5D, -1.0D, 0.5D), EntityType.BAT);
		event.getClickedBlock().getWorld().strikeLightningEffect(event.getClickedBlock().getLocation());
		for (Entity entity : bat.getNearbyEntities(5.0D, 100.0D, 5.0D)) {
			if (((entity instanceof Player)) && (entity != player)) {
				Vector vec = ((Player)entity).getLocation().add(player.getLocation().toVector()).toVector().normalize();
	              
	              ((Player)entity).setVelocity(vec.multiply(1.5D));
	              ((Player)entity).damage(6.0D, player);
			}
		}
		bat.remove();
		CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 30);
		cooldown.start();
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (event.getCause() == BlockIgniteEvent.IgniteCause.LIGHTNING) {
			event.setCancelled(true);
		}
	}
}
