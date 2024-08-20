package me.ale.pvp.kits;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class MusicKit extends Kit {
	
	public MusicKit() {
		super("Music", Material.NOTE_BLOCK, new ItemStack(Material.NOTE_BLOCK), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Deixe seus inimigos surdos com a sua música.");	
	}
	@SuppressWarnings("deprecation")
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
			clicked.getInventory().setHelmet(new ItemBuilder().type(Material.NOTE_BLOCK).name("§aTocador").build());
			clicked.updateInventory();
			Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new BukkitRunnable() {
				
				@Override
				public void run() {
					if (clicked.getInventory().getHelmet().getType() == Material.NOTE_BLOCK) {
						clicked.damage(3.5);
						clicked.getWorld().playEffect(clicked.getLocation().add(0, clicked.getLocation().getY() + 0.2, 0), Effect.NOTE, 1);
					} else {
						cancel();
					}
				}
			}, 30L, 30L);
			CooldownAPI.addCooldown(player, new Cooldown(getName(), 25L));
		}
	}
}
