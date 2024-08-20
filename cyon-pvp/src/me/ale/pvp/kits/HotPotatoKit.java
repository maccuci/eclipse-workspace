package me.ale.pvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class HotPotatoKit extends Kit {

	public HotPotatoKit() {
		super("Hotpotato", Material.POTATO_ITEM, new ItemStack(Material.POTATO_ITEM), KitRarity.RARE, Rank.SIMPLE, "§7Coloque um explosivo na cabeça do seu oponente.");
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEntityEvent event) {
		Player player = event.getPlayer();
		
		if (!hasKit(player))
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
		
		event.setCancelled(true);
		if((event.getRightClicked() instanceof Player)) {
			Player clicked = (Player)event.getRightClicked();
			clicked.getInventory().setHelmet(new ItemStack(Material.TNT));
			clicked.updateInventory();
			player.updateInventory();
			clicked.sendMessage("§cAlgum Hotpotato colocou uma batata explosiva em você! Você tem 5 segundos para retira-la.");
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					if (clicked.getInventory().getHelmet().getType() == Material.TNT) {
						clicked.getWorld().createExplosion(clicked.getLocation(), 3, true);
					}
				}
			}, 100L);
		}
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 35L));
	}
}
