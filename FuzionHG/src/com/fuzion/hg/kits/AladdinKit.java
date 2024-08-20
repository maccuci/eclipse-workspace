package com.fuzion.hg.kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.kit.Kit;

public class AladdinKit extends Kit {
	
	public AladdinKit() {
		super("Aladdin", "Não encontrado", new ItemStack(Material.CARPET), Group.BETA, "§7Voe no seu tapete mágico.");
		addItem(new ItemStack(Material.CARPET));
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
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		
		event.setCancelled(true);
		FallingBlock fallingBlock = player.getWorld().spawnFallingBlock(player.getLocation().clone().add(0, 1, 0), Material.CARPET, (byte) 0);
		fallingBlock.setDropItem(false);
		fallingBlock.setVelocity(new Vector(0, 0, 0));
		fallingBlock.setPassenger(player);
		Location location = player.getLocation().clone();
		location.setPitch(0F);
		Vector vector = player.getVelocity().clone();
		int height = 7;
        Vector jump = vector.multiply(0.1D).setY(0.17D * height);
        Vector see = location.getDirection().normalize().multiply(1.5D);
        fallingBlock.setVelocity(jump.add(see));
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				fallingBlock.remove();
			}
		}, 50L);
        CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 15);
		cooldown.start();
	}
}
