package me.ale.pvp.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
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
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class MinionsKit extends Kit {

	public static List<Wolf> wolfs = new ArrayList<>();
	
	public MinionsKit() {
		super("Minions", Material.GOLD_SPADE, new ItemStack(Material.GOLD_SPADE), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Tenha a habilidade de invocar cachorros que", "§7atacam tudo que você encosta.");
	}
	
	public void removeWolfs(Player player) {
		if(hasKit(player)) {
			for(Wolf wolf : wolfs) {
				if (wolf.isValid())
					wolf.remove();
			}
		}
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
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c"
					+ DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}

		event.setCancelled(true);
		for (int i = 0; i < 5; i++) {
			Wolf wolf = (Wolf) player.getWorld().spawnEntity(player.getLocation(), EntityType.WOLF);

			wolf.setTamed(true);
			wolf.setOwner(player);
			wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1));
			wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));

			wolf.setCustomName(player.getName() + "'s minion");
			wolf.setCustomNameVisible(true);

			wolfs.add(wolf);
		}
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 35L));
	}
}
