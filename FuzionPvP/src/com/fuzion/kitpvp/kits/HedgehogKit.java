package com.fuzion.kitpvp.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.bukkit.event.SchedulerEvent;
import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.manager.kit.Kit;
import com.fuzion.kitpvp.manager.protection.ProtectionManager;

public class HedgehogKit extends Kit {
	
	private ArrayList<NovaDirection> directions;
	private HashMap<Arrow, Vector> arrows;
	
	public HedgehogKit() {
		super("Hedgehog", new ItemStack(Material.ARROW), Group.BETA, "�7Clique com o bot�o direito do mouse no item para atirar flechas em todas as dire��es ao seu redor, o que causar� grandes quantidades de dano aos jogadores atingidos.");
		addItem(new ItemStack(Material.ARROW));
		directions = new ArrayList<>();
		ArrayList<Double> list = new ArrayList<>();
		list.add(0.0);
		list.add(22.5);
		list.add(45.0);
		list.add(67.5);
		list.add(90.0);
		list.add(112.5);
		list.add(135.0);
		list.add(157.5);
		list.add(180.0);
		list.add(202.5);
		list.add(225.0);
		list.add(247.5);
		list.add(270.0);
		list.add(292.5);
		list.add(315.0);
		list.add(337.5);
		for (double i : list) {
			directions.add(new NovaDirection(i, 67.5));
			directions.add(new NovaDirection(i, 45.0));
			directions.add(new NovaDirection(i, 22.5));
			directions.add(new NovaDirection(i, 0.0));
			directions.add(new NovaDirection(i, -22.5));
			directions.add(new NovaDirection(i, -45));
			directions.add(new NovaDirection(i, -67.5));
		}
		directions.add(new NovaDirection(90.0, 0.0));
		directions.add(new NovaDirection(-90.0, 0.0));
		directions.add(new NovaDirection(0.0, 90.0));
		directions.add(new NovaDirection(0.0, -90.0));
		list.clear();
		list = null;
		arrows = new HashMap<>();
	}
	
	@EventHandler
	public void onUpdate(SchedulerEvent event) {
		Iterator<Entry<Arrow, Vector>> entrys = arrows.entrySet().iterator();
		while (entrys.hasNext()) {
			Entry<Arrow, Vector> entry = entrys.next();
			Arrow arrow = entry.getKey();
			Vector vec = entry.getValue();
			if (!arrow.isDead()) {
				arrow.setVelocity(vec.normalize().multiply(vec.lengthSquared() / 4));
				if (arrow.isOnGround() || arrow.getTicksLived() >= 100) {
					arrow.remove();
				}
			} else {
				entrys.remove();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerInteractListener(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (event.getItem() == null)
			return;
		Player p = event.getPlayer();
		if (!hasKit(p))
			return;
		if(ProtectionManager.hasProtected(p))
			return;
		ItemStack item = event.getItem();
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (event.getAction().name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		p.updateInventory();
		if (CooldownAPI.isInCooldown(p.getUniqueId(), getName())) {
			p.sendMessage("�cVoc� est� em cooldown durante �c" + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(p.getUniqueId(), getName())));
			p.playSound(p.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			return;
		}

		Location loc = p.getLocation();
		for (NovaDirection d : directions) {
			final Arrow arrow = loc.getWorld().spawn(loc.clone().add(0, 1, 0), Arrow.class);
			double pitch = ((d.pitch + 90) * Math.PI) / 180;
			double yaw = ((d.yaw + 90) * Math.PI) / 180;
			double x = Math.sin(pitch) * Math.cos(yaw);
			double y = Math.sin(pitch) * Math.sin(yaw);
			double z = Math.cos(pitch);
			Vector vec = new Vector(x, z, y);
			arrow.setShooter(p);
			arrow.setVelocity(vec.multiply(2));
			arrow.setMetadata("hedgehog", new FixedMetadataValue(Main.getPlugin(), p.getUniqueId()));
			arrows.put(arrow, vec);
		}
		p.playSound(p.getLocation(), Sound.SHOOT_ARROW, 0.5F, 1.0F);
		CooldownAPI cooldown = new CooldownAPI(p.getUniqueId(), getName(), 25);
		cooldown.start();
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager().hasMetadata("hedgehog")) {
			if (e.getDamager() instanceof Arrow) {
				Arrow arrow = (Arrow) e.getDamager();
				if (arrow.getShooter() instanceof Player) {
					Player s = (Player) arrow.getShooter();
					if (e.getEntity() instanceof Player) {
						Player p = (Player) e.getEntity();
						if (s.getUniqueId() == p.getUniqueId()) {
							e.setCancelled(true);
							return;
						}
						if (damaged.contains(p.getUniqueId())) {
							e.setCancelled(true);
							return;
						}
					}
					e.setDamage(5.0D);
					if (e.getEntity() instanceof Player) {
						Player p = (Player) e.getEntity();
						damaged.add(p.getUniqueId());
						new BukkitRunnable() {
							@Override
							public void run() {
								damaged.remove(p.getUniqueId());
							}
						}.runTaskLater(Main.getPlugin(), 10);
					}
				}
			}
		}
	}
	
	private Set<UUID> damaged = new HashSet<>();
	
	private static class NovaDirection {
		private double pitch;
		private double yaw;

		public NovaDirection(double pitch, double yaw) {
			this.pitch = pitch;
			this.yaw = yaw;
		}

	}
}
