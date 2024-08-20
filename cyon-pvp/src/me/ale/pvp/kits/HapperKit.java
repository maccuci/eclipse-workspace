package me.ale.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class HapperKit extends Kit {
	
	private ArrayList<Player> used = new ArrayList<>();
	
	public HapperKit() {
		super("Happer", Material.HOPPER, new ItemStack(Material.HOPPER), KitRarity.MYSTIC, Rank.SIMPLE, "§7Ganhe uma super força e uma super", "§7velocidade mas logo após ganhe os", "§7efeitos opostos a estes.");
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
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
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		
		player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 7 * 20, 3));
		player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 7 * 20, 2));

		 Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				used.add(player);
			}
		}, 140L);
		 
		 Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				
			@Override
			public void run() {
				if(used.contains(player)) {
					player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 7 * 20, 3));
					player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 7 * 20, 2));
					used.remove(player);
				}
			}
		}, 141L);
		 CooldownAPI.addCooldown(player, new Cooldown(getName(), 15L));
	}
}
