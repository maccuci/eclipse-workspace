package me.ale.pvp.kits;

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

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.bukkit.event.SchedulerEvent;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class BurKit extends Kit {
	
	private ArrayList<NovaDirection> directions;
	private HashMap<Arrow, Vector> arrows;
	
	public BurKit() {
		super("Bur", Material.ARROW, new ItemStack(Material.ARROW), KitRarity.MYSTIC, Rank.SIMPLE, "§7Clique com o botão direito do mouse no item para atirar flechas", "§7em todas as direções ao seu redor, o que causará", "§7grandes quantidades de dano aos jogadores atingidos.");	
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

	@EventHandler
	public void onPlayerInteractListener(PlayerInteractEvent event) {
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		if (event.getItem() == null)
			return;
		Player p = event.getPlayer();
		if (!hasKit(p))
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
		if (CooldownAPI.hasCooldown(p, getName())) {
			p.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(p).getRemaining()));
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
			arrow.setMetadata("bur", new FixedMetadataValue(Main.getPlugin(), p.getUniqueId()));
			arrows.put(arrow, vec);
		}
		p.playSound(p.getLocation(), Sound.SHOOT_ARROW, 0.5F, 1.0F);
		CooldownAPI.addCooldown(p, new Cooldown(getName(), 25L));
	}

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getDamager().hasMetadata("bur")) {
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
