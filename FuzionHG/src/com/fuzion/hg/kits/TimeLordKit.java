package com.fuzion.hg.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.kit.Kit;

public class TimeLordKit extends Kit {
	
	private List<Player> freeze = new ArrayList<>();
	
	public TimeLordKit() {
		super("TimeLord", "Contra times e Trap", new ItemStack(Material.WATCH), Group.BETA, "§7Pare o tempo ao seu redor.");
		addItem(new ItemStack(Material.WATCH, 1));
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
			player.sendMessage("§cVocê está em cooldown durante "
					+ DateUtils.formatDifference(CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		
		event.setCancelled(true);
		for(Entity entity : player.getNearbyEntities(5, 5, 5)) {
			if (entity instanceof LivingEntity) {
				if(entity.getType() == EntityType.PLAYER) {
					Player other = (Player)entity;
					
					freeze.add(other);
					other.sendMessage("§cVocê foi congelado por um TimeLord!");
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						
						@Override
						public void run() {
							freeze.remove(other);
							other.sendMessage("§aVocê agora não está mais congelado.");
						}
					}, 100L);
				}
			}
		}
		CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 25);
		cooldown.start();
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(freeze.contains(player)) {
			event.setTo(player.getLocation());
		}
	}
}
