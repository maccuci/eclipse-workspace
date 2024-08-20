package com.fuzion.kitpvp.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.manager.kit.Kit;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;

public class SwitcherKit extends Kit {
	
	public SwitcherKit() {
		super("Switcher", new ItemStack(Material.SNOW_BALL), Group.BETA, "§7Teleporte-se para a localização do jogador que você acertou a bola de neve.");
		addItem(new ItemStack(Material.SNOW_BALL, 16));
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
		Snowball s = player.launchProjectile(Snowball.class);
		Vector v = player.getLocation().getDirection().normalize().multiply(10);
		s.setVelocity(v);
		s.setMetadata("switcher", new FixedMetadataValue(Main.getPlugin(), true));
		CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 30);
		cooldown.start();
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) event.getDamager();
			if (s.getShooter() instanceof Player) {
				if (s.hasMetadata("switcher")) {
					Player damager = (Player)s.getShooter();
					Player damaged = (Player) event.getEntity();
					
		            Location hitLoc = damaged.getLocation();
		            Location shooterLoc = damager.getLocation();
		            
		            damaged.teleport(shooterLoc);
		            damager.teleport(hitLoc);
				}
			}
		}
	}
}
