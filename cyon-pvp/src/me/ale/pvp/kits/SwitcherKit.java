package me.ale.pvp.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
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

public class SwitcherKit extends Kit {
	
	public SwitcherKit() {
		super("Switcher", Material.SNOW_BALL, new ItemStack(Material.SNOW_BALL), KitRarity.RARE, Rank.SIMPLE, "§7Acerte uma bola de neve e troque de lugar", "§7com o jogador acertado.");
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
		Snowball s = player.launchProjectile(Snowball.class);
		Vector v = player.getLocation().getDirection().normalize().multiply(10);
		s.setVelocity(v);
		s.setMetadata("switcher", new FixedMetadataValue(Main.getPlugin(), true));
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 5L));
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) event.getDamager();
			if (s.getShooter() instanceof Player) {
				if (s.hasMetadata("switcher")) {
					Player damager = (Player)s.getShooter();
					Player damaged = (Player) event.getEntity();
					
		            Location hitLoc = damaged.getLocation();
		            Location shooterLoc = damager.getLocation();
		            
		            damaged.teleport(shooterLoc);
		            damager.teleport(hitLoc);
				}
			}
		}
	}
}
