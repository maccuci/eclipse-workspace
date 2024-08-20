package com.fuzion.hg.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class WarlockKit extends Kit {
	
	public WarlockKit() {
		super("Warlock", "Magia", new ItemStack(Material.GLOWSTONE_DUST), Group.BETA, "§7Chame um bruxo invisivel para te ajudar na hora do combate.");
		addItem(new ItemBuilder().type(Material.GLOWSTONE_DUST).build());
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (!hasKit(player))
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
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
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
			Zombie zombie = (Zombie)player.getWorld().spawnEntity(player.getLocation().add(1, 0, 1), EntityType.ZOMBIE);
			EntityEquipment equipment = zombie.getEquipment();
			equipment.setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
			equipment.setChestplate(new ItemBuilder().type(Material.LEATHER_CHESTPLATE).durability(5).glow().build());
			equipment.setItemInHand(new ItemStack(Material.DIAMOND_HOE));
			zombie.setTarget(near);
			zombie.setCanPickupItems(false);
			zombie.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 9999, 1), true);
			zombie.damage(3D, near);
			
			CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 40);
			cooldown.start();
		}
	}
}
