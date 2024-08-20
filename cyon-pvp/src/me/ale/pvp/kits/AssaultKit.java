package me.ale.pvp.kits;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;
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

public class AssaultKit extends Kit {
	
	private HashMap<String, BukkitTask> task = new HashMap<>();
	
	public AssaultKit() {
		super("Assault", Material.SAPLING, new ItemStack(Material.SAPLING), KitRarity.MYSTIC, Rank.SIMPLE, "§7Tenha uma arma que da tiros instantâneos.");
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
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	Snowball s = player.launchProjectile(Snowball.class);
            	Vector v = player.getLocation().getDirection().normalize().multiply(10);
				s.setVelocity(v);
            	s.setMetadata("assault", new FixedMetadataValue(Main.getPlugin(), true));
            	player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
            }
          }, 2L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	Snowball s = player.launchProjectile(Snowball.class);
            	Vector v = player.getLocation().getDirection().normalize().multiply(10);
				s.setVelocity(v);
            	s.setMetadata("assault", new FixedMetadataValue(Main.getPlugin(), true));
            	player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
            }
          }, 3L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	Snowball s = player.launchProjectile(Snowball.class);
            	Vector v = player.getLocation().getDirection().normalize().multiply(10);
				s.setVelocity(v);
            	s.setMetadata("assault", new FixedMetadataValue(Main.getPlugin(), true));
            	player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
            }
          }, 4L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	Snowball s = player.launchProjectile(Snowball.class);
            	Vector v = player.getLocation().getDirection().normalize().multiply(10);
				s.setVelocity(v);
            	s.setMetadata("assault", new FixedMetadataValue(Main.getPlugin(), true));
            	player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
            }
          }, 5L);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
            public void run() {
            	Snowball s = player.launchProjectile(Snowball.class);
            	Vector v = player.getLocation().getDirection().normalize().multiply(10);
				s.setVelocity(v);
            	s.setMetadata("assault", new FixedMetadataValue(Main.getPlugin(), true));
            	player.setVelocity(player.getEyeLocation().getDirection().multiply(-0.6F));
            }
          }, 6L);
		player.sendMessage(CyonAPI.SERVER_PREFIX + "Você disparou o seu tiro com sucesso.");
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 25L));
	}
	
	@EventHandler
	public void onDamageLaser(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && e.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) e.getDamager();
			if (s.getShooter() instanceof Player) {
				if (s.hasMetadata("assault")) {
					Player damaged = (Player) e.getEntity();
					damaged.damage(3);
				}
			}
		}
	}
	
	@EventHandler
	public void itemheld(PlayerItemHeldEvent event) {
		Player player = event.getPlayer();

		if (task.containsKey(player.getName())) {
			task.get(player.getName()).cancel();
			task.remove(player.getName());
			player.playSound(player.getLocation(), Sound.ITEM_BREAK, 3, 3);
		}
	}
}
