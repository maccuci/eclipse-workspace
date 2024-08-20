package me.ale.pvp.kits;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class MonkKit extends Kit {
	
	public MonkKit() {
		super("Monk", Material.BLAZE_POWDER, new ItemStack(Material.BLAZE_POWDER), KitRarity.COMMON, Rank.NORMAL, "§7Troque o item que o seu inimigo esta", "§7segurando por outro.");	
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		
		if(!hasKit(player))
			return;
		
		ItemStack item = player.getItemInHand();
		if (item == null)
			return;
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.hasCooldown(player, getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
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
	            CooldownAPI.addCooldown(player, new Cooldown(getName(), 15L));
			}
		}
	}
}
