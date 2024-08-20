package com.fuzion.kitpvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.manager.kit.Kit;


public class HotpotatoKit extends Kit {
	
	public HotpotatoKit() {
		super("Hotpotato", new ItemStack(Material.POTATO_ITEM), Group.BETA, "§7Plante uma batata explosiva em um jogador.");
		addItem(new ItemStack(Material.TNT));
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		
		if (!hasKit(player))
			return;
		ItemStack item = player.getItemInHand();
		if (item == null)
			return;
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.isInCooldown(player.getUniqueId(), getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		
		event.setCancelled(true);
		if((event.getRightClicked() instanceof Player)) {
			Player clicked = (Player)event.getRightClicked();
			clicked.getInventory().setHelmet(new ItemStack(Material.TNT));
			clicked.updateInventory();
			player.updateInventory();
			clicked.sendMessage("§cAlgum Hotpotato colocou uma batata explosiva em você! Você tem 5 segundos para retira-la.");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					if (clicked.getInventory().getHelmet().getType() == Material.TNT) {
						clicked.getWorld().createExplosion(clicked.getLocation(), 3, true);
					}
				}
			}, 100L);
		}
		CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 30);
		cooldown.start();
	}
}
