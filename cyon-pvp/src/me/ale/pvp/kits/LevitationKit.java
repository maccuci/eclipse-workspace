package me.ale.pvp.kits;

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

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class LevitationKit extends Kit {
	
	public LevitationKit() {
		super("Levitation", Material.TORCH, new ItemStack(Material.TORCH), KitRarity.MYSTIC, Rank.SIMPLE, "�7Levite todos os jogadores ao seu redor.");
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
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Voc� est� em cooldown durante �c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		
		event.setCancelled(true);
		for(Entity entity : player.getNearbyEntities(6, 5, 6)) {
			if((entity instanceof Player)) {
				Player other = (Player)entity;
				
				Vector vector = other.getLocation().getDirection().multiply(0).setY(0.8D);
				other.setVelocity(vector);
				CooldownAPI.addCooldown(player, new Cooldown(getName(), 35L));
				
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
