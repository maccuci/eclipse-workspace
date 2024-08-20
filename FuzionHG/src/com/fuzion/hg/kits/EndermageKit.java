package com.fuzion.hg.kits;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.kit.Kit;

public class EndermageKit extends Kit {
	
	private ArrayList<Block> endermages = new ArrayList<>();
	public static ArrayList<String> invencible = new ArrayList<>();
	private HashMap<Player, Integer> pnum = new HashMap<>();
	
	public EndermageKit() {
		super("Endermage", "Trap", new ItemStack(Material.ENDER_PORTAL_FRAME), Group.NORMAL, "§7Coloque seu portal em um bloco, e todos que passarem por baixo do mesmo irão ser puxados.");
		addItem(new ItemStack(Material.ENDER_PORTAL_FRAME, 1));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Action a = event.getAction();
		ItemStack item = event.getItem();
		final Block b = event.getClickedBlock();
		if (!a.toString().contains("BLOCK")) {
			return;
		}
		if (item == null) {
			return;
		}
		if (item.getType() != Material.ENDER_PORTAL_FRAME) {
			return;
		}
		if (!hasKit(player)) {
			return;
		}
		event.setCancelled(true);
		if ((b.getType() == Material.CACTUS) || (b.getType() == Material.TRAP_DOOR) || (b.getType() == Material.WALL_SIGN) || (b.getType() == Material.SIGN) || (b.getType() == Material.SIGN_POST)) {
			player.sendMessage("Você não pode colocar neste bloco.");
			return;
		}
		if (this.endermages.contains(b)) {
			return;
		}
		this.endermages.add(b);
		pnum.put(player, event.getPlayer().getInventory().getHeldItemSlot());
		item.setAmount(0);
		if (item.getAmount() == 0) {
			event.getPlayer().setItemInHand(null);
		}
		Material material = b.getType();
		byte dataValue = b.getData();
		Location portal = b.getLocation().clone().add(0.5D, 0.5D, 0.5D);
		portal.getBlock().setType(Material.ENDER_PORTAL_FRAME);
		for (int i = 0; i <= 5; i++) {
			int no = i;
			new BukkitRunnable() {
				public void run() {
					if (portal.getBlock().getType() != Material.ENDER_PORTAL_FRAME) {
						return;
					}
					if (no < 5) {
						for (Player gamer : portal.getWorld().getPlayers()) {
							if (gamer != player) {
								if (isEnderable(portal, gamer.getLocation())) {
									if(!AdminAPI.admin.contains(gamer.getUniqueId())) {
										if (gamer.getLocation().distance(portal) > 3.0D) {
											gamer.teleport(portal.clone().add(0.0D, 0.5D, 0.0D));
											player.teleport(portal.clone().add(0.0D, 0.5D, 0.0D));
											gamer.sendMessage("§5Você foi puxado por um endermage! Mate-o ou morra!");
											player.sendMessage("§dVocê poxou alguém com o seu portal! Vocês tem 5 segundos de invencibilidade.");
											invencible.add(gamer.getName());
											invencible.add(player.getName());
											ItemStack portalItem = new ItemStack(Material.ENDER_PORTAL_FRAME);
											player.getInventory().setItem(pnum.get(player), portalItem);
											endermages.remove(b);
											portal.getBlock().setTypeIdAndData(material.getId(), dataValue, true);
											Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
												public void run() {
													invencible.remove(gamer.getName());
													invencible.remove(player.getName());
												}
											}, 100L);
										}
									}
								}
							}
						}
					} else {
						ItemStack portalItem = new ItemStack(Material.ENDER_PORTAL_FRAME);
						portal.getBlock().setTypeIdAndData(material.getId(), dataValue, true);
						player.getInventory().setItem(pnum.get(player), portalItem);
						endermages.remove(b);
					}
				}
			}.runTaskLater(Main.getPlugin(), i * 20);
		}
	}
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		Entity vitima = event.getEntity();
		if (vitima.isDead()) {
			return;
		}
		if (!(vitima instanceof Player)) {
			return;
		}
		Player p = (Player) vitima;
		if (invencible.contains(p.getName())) {
			event.setCancelled(true);
			return;
		}
	}
	@EventHandler
	public void onHabilidadeDeHitReceberDano(EntityDamageByEntityEvent event) {
		Entity vitima = event.getDamager();
		if (event.getCause() != DamageCause.ENTITY_ATTACK) {
			return;
		}
		if (!(vitima instanceof Player)) {
			return;
		}
		if (!(vitima instanceof Player)) {
			return;
		}
		Player p = (Player) vitima;
		if (invencible.contains(p.getName())) {
			event.setCancelled(true);
			return;
		}
	}

	private boolean isEnderable(Location portal, Location player) {
		return (Math.abs(portal.getX() - player.getX()) < 5.0D) && (Math.abs(portal.getZ() - player.getZ()) < 5.0D);
	}
}
