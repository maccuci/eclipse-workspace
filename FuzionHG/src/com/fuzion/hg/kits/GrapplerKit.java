package com.fuzion.hg.kits;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.kits.constructor.GrapplingHook;
import com.fuzion.hg.manager.kit.Kit;

public class GrapplerKit extends Kit {
	
	private Map<UUID, GrapplingHook> grapplerHooks = new HashMap<>();
	
	public GrapplerKit() {
		super("Grappler", "Contra maratonista", new ItemStack(Material.LEASH), Group.BETA, "§7Use o seu laço para se locomover pelo mapa do jogo.");
		addItem(new ItemStack(Material.LEASH, 1));
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerInteractListener(PlayerInteractEvent event) {
		if (!hasKit(event.getPlayer()))
			return;
		if (event.getItem() == null)
			return;
		Action a = event.getAction();
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		ItemStack ITEM = getItems().iterator().next().clone();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.isInCooldown(player.getUniqueId(), getName())) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 2 * 20, 5));
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		if (event.getAction().name().contains("LEFT")) {
			if (grapplerHooks.containsKey(player.getUniqueId())) {
				grapplerHooks.get(player.getUniqueId()).remove();
				grapplerHooks.remove(player.getUniqueId());
			}
			GrapplingHook hook = new GrapplingHook(player.getWorld(), ((CraftPlayer) player).getHandle());
			Vector direction = player.getLocation().getDirection();
			hook.spawn(player.getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()));
			hook.move(direction.getX() * 5.0D, direction.getY() * 5.0D, direction.getZ() * 5.0D);
			grapplerHooks.put(player.getUniqueId(), hook);
		} else if (event.getAction().name().contains("RIGHT")) {
			if (grapplerHooks.containsKey(player.getUniqueId())) {
				if (!grapplerHooks.get(player.getUniqueId()).isHooked()) {
					player.sendMessage("§cSua corda ainda não agarrou em nada!");
					return;
				}
				GrapplingHook hook = grapplerHooks.get(player.getUniqueId());
				Location loc = hook.getBukkitEntity().getLocation();
				Location pLoc = player.getLocation();
				double d = loc.distance(player.getLocation());
				double t = d;
				double v_x = (1.0D + 0.04000000000000001D * t) * ((isNear(loc, pLoc) ? 0 : loc.getX() - pLoc.getX()) / t);
				double v_y = (0.9D + 0.03D * t) * ((isNear(loc, pLoc) ? 0.1 : loc.getY() - pLoc.getY()) / t);
				double v_z = (1.0D + 0.04000000000000001D * t) * ((isNear(loc, pLoc) ? 0 : loc.getZ() - pLoc.getZ()) / t);
				Vector v = player.getVelocity();
				v.setX(v_x);
				v.setY(v_y);
				v.setZ(v_z);
				player.setVelocity(v.multiply(3.5));
				if (player.getLocation().getY() < hook.getBukkitEntity().getLocation().getY()) {
					player.setFallDistance(0);
				}
				player.getWorld().playSound(player.getLocation(), Sound.STEP_GRAVEL, 1.0F, 1.0F);
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerLeashEntityListener(PlayerLeashEntityEvent event) {
		if (!hasKit(event.getPlayer()))
			return;
		Player p = event.getPlayer();
		if (p.getItemInHand() == null)
			return;
		ItemStack item = p.getItemInHand();
		ItemStack ITEM = getItems().iterator().next().clone();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		item.setDurability(ITEM.getDurability());
		event.setCancelled(true);
		if (grapplerHooks.containsKey(p.getUniqueId())) {
			if (grapplerHooks.get(p.getUniqueId()).isHooked()) {
				GrapplingHook hook = grapplerHooks.get(p.getUniqueId());
				Location loc = hook.getBukkitEntity().getLocation();
				Location playerLoc = p.getLocation();
				double d = loc.distance(playerLoc);
				double t = d;
				double v_x = (1.0D + 0.04000000000000001D * t) * ((isNear(loc, playerLoc) ? 0 : loc.getX() - playerLoc.getX()) / t);
				double v_y = (0.9D + 0.03D * t) * ((isNear(loc, playerLoc) ? 0.1 : loc.getY() - playerLoc.getY()) / t);
				double v_z = (1.0D + 0.04000000000000001D * t) * ((isNear(loc, playerLoc) ? 0 : loc.getZ() - playerLoc.getZ()) / t);
				Vector v = p.getVelocity();
				v.setX(v_x);
				v.setY(v_y);
				v.setZ(v_z);
				p.setVelocity(v.multiply(3.5));
				if (playerLoc.getY() < hook.getBukkitEntity().getLocation().getY()) {
					p.setFallDistance(0);
				}
				p.getWorld().playSound(playerLoc, Sound.STEP_GRAVEL, 1.0F, 1.0F);
			}
		}
	}

	private boolean isNear(Location loc, Location playerLoc) {
		return loc.distance(playerLoc) < 1.5;
	}

	@EventHandler
	public void onPlayerItemHeldListener(PlayerItemHeldEvent e) {
		if (grapplerHooks.containsKey(e.getPlayer().getUniqueId())) {
			grapplerHooks.get(e.getPlayer().getUniqueId()).remove();
			grapplerHooks.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		if (grapplerHooks.containsKey(e.getPlayer().getUniqueId())) {
			grapplerHooks.get(e.getPlayer().getUniqueId()).remove();
			grapplerHooks.remove(e.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		if (!(event.getDamager() instanceof Player))
			return;
		if (!(event.getEntity() instanceof Player))
			return;
		Player p = (Player) event.getEntity();
		if (!hasKit(p))
			return;
		CooldownAPI cooldown = new CooldownAPI(p.getUniqueId(), getName(), 10);
		cooldown.start();
	}
}
