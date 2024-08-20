package com.fuzion.hg.kits;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class LocalizerKit extends Kit {
	
	public LocalizerKit() {
		super("Localizer", "Contra maratonista", new ItemStack(Material.BOOK), Group.BETA, "§7Junte todos os jogadores do servidor e todos que estiverem a 30 blocos de distancia você pode se teleportar até um deles.");
		addItem(new ItemStack(Material.BOOK));
	}
	
	@SuppressWarnings("deprecation")
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
		Player near = null;
		double closest = 30.0D;
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online != player && (online.getLocation().distanceSquared(player.getLocation()) < closest)) {
				near = online;
				closest = online.getLocation().distanceSquared(player.getLocation());
			}
		}
		
		if(near != null) {
			player.teleport(near);
			for (int i = 0; i < 10; i++) {
				 player.getWorld().playEffect(player.getLocation().add(0.0D, i, 0.0D), Effect.ENDER_SIGNAL, 0);
                 near.getWorld().playEffect(player.getLocation().add(0.0D, i, 0.0D), Effect.ENDER_SIGNAL, 0);
			}
			
			player.sendMessage("§aVocê teleportou até o jogador §f" + near.getName());
			near.sendMessage("§cUm jogador com o kit lozalizer te achou! Corra!");
			near.setNoDamageTicks(40);
            player.setNoDamageTicks(40);
            CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 35);
    		cooldown.start();
		} else {
			player.sendMessage("§cNão há jogadores por perto.");
		}
	}
}
