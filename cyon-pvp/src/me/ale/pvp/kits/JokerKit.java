package me.ale.pvp.kits;

import java.util.HashMap;
import java.util.Map;

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

public class JokerKit extends Kit {
	
	private Map<Player, ItemStack[]> inventoryContents = new HashMap<>();
	
	public JokerKit() {
		super("Joker", Material.GOLD_NUGGET, new ItemStack(Material.GOLD_NUGGET), KitRarity.MYSTIC, Rank.SIMPLE, "§7Confunda todos os jogadores ao redor", "§7de 5 blocos.");
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
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		
		event.setCancelled(true);
		for(Entity entity : player.getNearbyEntities(5.0, 5.0, 5.0)) {
			if (entity instanceof LivingEntity) {
				if(entity.getType() == EntityType.PLAYER) {
					Player other = (Player)entity;
					other.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10 * 20, 4));
					other.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 4));
					other.damage(2);
					inventoryContents.put(player, other.getInventory().getContents());
					other.getInventory().clear();
					Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						
						@Override
						public void run() {
							other.getInventory().setContents(inventoryContents.get(player));
						}
					}, 15L);
				}
			}
		}
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 35L));
	}
}
