package com.fuzion.hg.kits;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class MonkKit extends Kit {
	
	public MonkKit() {
		super("Monk", "Furtividade", new ItemStack(Material.BLAZE_ROD), Group.BETA,"§7Confunda seu inimigo trocando os itens dele.");
		addItem(new ItemStack(Material.BLAZE_ROD, 1));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		
		if(!hasKit(player))
			return;
		
		ItemStack item = player.getItemInHand();
		if (item == null)
			return;
		ItemStack ITEM = getItems().iterator().next().clone();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.isInCooldown(player.getUniqueId(), getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage("§cVocê está em cooldown durante "
					+ DateUtils.formatDifference(CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		
		if(item.getType() == Material.BLAZE_POWDER) {
			Player target = (Player)event.getRightClicked();
			
			if (event.getRightClicked().getType() == EntityType.PLAYER) {
				int random = new Random().nextInt(36);
	            while (random <= 8) {
	            	random = new Random().nextInt(36);
	            }
	            ItemStack hand = target.getItemInHand();
	            ItemStack inv = target.getInventory().getItem(random);
	            target.getInventory().setItem(random, hand);
	            target.setItemInHand(inv);
	            CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 15);
	     		cooldown.start();
			}
		}
	}
}
