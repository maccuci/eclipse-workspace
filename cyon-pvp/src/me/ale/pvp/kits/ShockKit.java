package me.ale.pvp.kits;

import java.util.HashMap;

import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class ShockKit extends Kit {
	
	private HashMap<Player, Integer> shocks = new HashMap<>();
	
	public ShockKit() {
		super("Shock", Material.IRON_FENCE, new ItemStack(Material.IRON_FENCE), KitRarity.MYSTIC, Rank.SIMPLE, "§7Clique no seu item para empurrar seus inimigos e", "§7dar um shock neles.");	
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		Player clicked = (Player)event.getRightClicked();
		ItemStack item = event.getPlayer().getItemInHand();
		
		if (!hasKit(player))
			return;
		if (item == null)
			return;
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.hasCooldown(player, getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c"
					+ DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		
		clicked.getWorld().playEffect(clicked.getLocation(), Effect.POTION_SWIRL, 1);
		clicked.setVelocity(new Vector(1.0, 1.5, 1.0));
		clicked.damage(1.5);
		if(!shocks.containsKey(player)) {
			shocks.put(player, 1);
		} else {
			shocks.put(player, shocks.get(player) + 1);
			player.sendMessage(CyonAPI.WARNING_PREFIX + "Você utilizou o shock " + shocks.get(player) + (shocks.get(player) > 1 ? "vezes" : "vez") + " ao chegar em 3 vezes, você entrará em cooldown.");
			return;
		}
		
		if(shocks.get(player) >= 3) {
			CooldownAPI.addCooldown(player, new Cooldown(getName(), 30L));
			return;
		}
	}
}
