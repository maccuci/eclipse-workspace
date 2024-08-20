package me.ale.pvp.kits;

import java.util.Random;

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

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;

public class LiedKit extends Kit {
	
	public LiedKit() {
		super("Lied", Material.LAPIS_BLOCK, new ItemStack(Material.LAPIS_BLOCK), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Clique no seu item para atacar todos", "§7em sua volta no redor de 6 blocos", "§7e confunda seus inimigos.");
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
		new TimeLordKit().drawCircle(player.getLocation(), 6, EnumParticle.SMOKE_NORMAL, 0);
		for(Entity entity : player.getNearbyEntities(6, 6, 6)) {
			if (entity instanceof LivingEntity) {
				if(entity.getType() == EntityType.PLAYER) {
					Player other = (Player)entity;
					other.damage(6);
					other.sendMessage("§cVocê levou 6 corações de dano pois estava no raio de um Lied.");
					int random = new Random().nextInt(36);
		            while (random <= 8) {
		            	random = new Random().nextInt(36);
		            }
		            ItemStack hand = other.getItemInHand();
		            ItemStack inv = other.getInventory().getItem(random);
		            other.getInventory().setItem(random, hand);
		            other.setItemInHand(inv);
				}
			}
		}
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 30L));
	}
}
