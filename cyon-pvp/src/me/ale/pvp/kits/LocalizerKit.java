package me.ale.pvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
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

public class LocalizerKit extends Kit {
	
	public LocalizerKit() {
		super("Localizer", Material.BOOK, new ItemStack(Material.BOOK), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Junte todos os jogadores do servidor e todos que", "§7estiverem a 30 blocos de distancia você pode", "§7se teleportar até um deles.");	
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
		Player near = null;
		double closest = 30.0D;
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online != player && (online.getLocation().distanceSquared(player.getLocation()) < closest)) {
				near = online;
				closest = online.getLocation().distanceSquared(player.getLocation());
			}
		}
		
		if(near != null) {
			player.teleport(near);
			for (int i = 0; i < 10; i++) {
				 player.getWorld().playEffect(player.getLocation().add(0.0D, i, 0.0D), Effect.ENDER_SIGNAL, 0);
                 near.getWorld().playEffect(player.getLocation().add(0.0D, i, 0.0D), Effect.ENDER_SIGNAL, 0);
			}
			
			player.sendMessage("§aVocê teleportou até o jogador §f" + near.getName());
			near.sendMessage("§cUm jogador com o kit lozalizer te achou! Corra!");
			near.setNoDamageTicks(40);
            player.setNoDamageTicks(40);
            CooldownAPI.addCooldown(player, new Cooldown(getName(), 35L));
		}
	}
}
