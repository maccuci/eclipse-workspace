package me.ale.pvp.kits;

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

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class AladdinKit extends Kit {
	
	public AladdinKit() {
		super("Aladdin", Material.CARPET, new ItemStack(Material.CARPET), KitRarity.RARE, Rank.SIMPLE, "§7Chame seu tapete mágico para voar nele.");
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
        CooldownAPI.addCooldown(player, new Cooldown(getName(), 15L));
	}
}
