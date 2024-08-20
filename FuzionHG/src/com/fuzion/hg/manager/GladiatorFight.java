package com.fuzion.hg.manager;

import java.util.List;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.hg.Main;
import com.fuzion.hg.kits.GladiatorKit;

import lombok.Getter;

@Getter
public class GladiatorFight {
	
	public GladiatorFight() {}

	private Player gladiator;
	private Player target;
	private Location tpLocGladiator;
	private Location tpLocTarget;
	private BukkitRunnable witherEffect;
	private BukkitRunnable teleportBack;
	private List<Block> blocksToRemove;
	private Listener listener;

	public GladiatorFight(Player gladiator, Player target) {
		this.gladiator = gladiator;
		this.target = target;
		this.blocksToRemove = new ArrayList<Block>();
		send1v1();
		this.listener = new Listener() {
			@EventHandler
			public void onEntityDamage(EntityDamageByEntityEvent event) {
				if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player))) {
					Player recebe = (Player) event.getEntity();
					Player faz = (Player) event.getDamager();
					if ((GladiatorFight.this.isIn1v1(faz)) && (GladiatorFight.this.isIn1v1(recebe))) {
						return;
					}
					if ((GladiatorFight.this.isIn1v1(faz)) && (!GladiatorFight.this.isIn1v1(recebe))) {
						event.setCancelled(true);
					} else if ((!GladiatorFight.this.isIn1v1(faz)) && (GladiatorFight.this.isIn1v1(recebe))) {
						event.setCancelled(true);
					}
				}
			}

			public HashMap<String, Long> allowCheck = new HashMap<String, Long>();

			public void updateCheck(Player p) {
				this.allowCheck.put(p.getName(), Long.valueOf(System.currentTimeMillis()));
			}

			public boolean podeChecar(Player p) {
				return System.currentTimeMillis() - ((Long) this.allowCheck.get(p.getName())).longValue() >= 10000L;
			}

			@EventHandler(priority = EventPriority.LOWEST)
			public void onDeath(PlayerDeathEvent event) {
				Player p = event.getEntity();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (p == gladiator) {
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					GladiatorFight.this.dropItems(p, event.getDrops(), GladiatorFight.this.tpLocGladiator);
					event.getDrops().clear();
					GladiatorFight.this.teleportBack(target, gladiator);
					return;
				}
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
				GladiatorFight.this.dropItems(p, event.getDrops(), GladiatorFight.this.tpLocTarget);
				event.getDrops().clear();
				GladiatorFight.this.teleportBack(gladiator, target);
			}

			@EventHandler
			public void onQuit(PlayerQuitEvent event) {
				Player p = event.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (event.getPlayer().isDead()) {
					return;
				}
				if (p == gladiator) {
					target.sendMessage("§cO jogador " + p.getName() + " desistiu da batalha contra você, e agora você venceu!");
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					GladiatorFight.this.dropItems(p, GladiatorFight.this.tpLocGladiator);
					GladiatorFight.this.teleportBack(target, gladiator);
					return;
				}
				gladiator.sendMessage("§cO jogador " + target.getName() + " desistiu da batalha contra você, e agora você venceu!");
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
				GladiatorFight.this.dropItems(p, GladiatorFight.this.tpLocTarget);
				GladiatorFight.this.teleportBack(gladiator, target);
			}

			@EventHandler
			public void onKick(PlayerKickEvent event) {
				Player p = event.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (event.getPlayer().isDead()) {
					return;
				}
				if (p == gladiator) {
					target.sendMessage("§cO jogador " + gladiator.getName() + " desistiu da batalha contra você, e agora você venceu!");
					target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
					GladiatorFight.this.dropItems(p, GladiatorFight.this.tpLocGladiator);
					GladiatorFight.this.teleportBack(target, gladiator);
					return;
				}
				gladiator.sendMessage("§cO jogador " + target.getName() + " desistiu da batalha contra você, e agora você venceu!");
				gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
				GladiatorFight.this.dropItems(p, GladiatorFight.this.tpLocTarget);
				GladiatorFight.this.teleportBack(gladiator, target);
			}

			@EventHandler
			public void onQuitGladiator(final PlayerMoveEvent e) {
				if (!GladiatorFight.this.isIn1v1(e.getPlayer())) {
					return;
				}
				if (GladiatorFight.this.blocksToRemove.contains(e.getTo().getBlock())) {
					return;
				}
				if (!this.allowCheck.containsKey(e.getPlayer().getName())) {
					this.allowCheck.put(e.getPlayer().getName(), Long.valueOf(0L));
				}
				if (e.getPlayer() == gladiator) {
					new BukkitRunnable() {
						public void run() {
							if (!podeChecar(e.getPlayer())) {
								return;
							}
							e.getPlayer().setFallDistance(0.0F);
							GladiatorFight.this.teleportBack(target, gladiator);
							e.getPlayer().setFallDistance(0.0F);
							updateCheck(e.getPlayer());
						}
					}.runTaskLater(Main.getPlugin(), 30L);
				}
				new BukkitRunnable() {
					public void run() {
						if (!podeChecar(e.getPlayer())) {
							return;
						}
						e.getPlayer().setFallDistance(0.0F);
						GladiatorFight.this.teleportBack(gladiator, target);
						e.getPlayer().setFallDistance(0.0F);
						updateCheck(e.getPlayer());
					}
				}.runTaskLater(Main.getPlugin(), 30L);
			}

			@EventHandler
			public void onTeleport(PlayerTeleportEvent event) {
				if (event.isCancelled()) {
					return;
				}
				Player p = event.getPlayer();
				if (!GladiatorFight.this.isIn1v1(p)) {
					return;
				}
				if (!GladiatorKit.inGladiator(p)) {
					return;
				}
				if (GladiatorFight.this.blocksToRemove.contains(event.getTo().getBlock())) {
					return;
				}
				if (p == gladiator) {
					GladiatorFight.this.teleportBack(event.getTo(), GladiatorFight.this.tpLocTarget);
					return;
				}
				if (p == target) {
					GladiatorFight.this.teleportBack(GladiatorFight.this.tpLocGladiator, event.getTo());
				}
			}

		};
		Bukkit.getPluginManager().registerEvents(listener, Main.getPlugin());
	}

	public boolean isIn1v1(Player player) {
		return (player == this.gladiator) || (player == this.target);
	}

	public void destroy() {
		HandlerList.unregisterAll(this.listener);
	}

	public void send1v1() {
		Location loc = this.gladiator.getLocation();
		boolean hasGladi = true;
		while (hasGladi) {
			hasGladi = false;
			boolean stop = false;
			for (double x = -8.0D; x <= 8.0D; x += 1.0D) {
				for (double z = -9.0D; z <= 9.0D; z += 1.0D) {
					for (double y = 0.0D; y <= 15.0D; y += 1.0D) {
						Location l = new Location(loc.getWorld(), loc.getX() + x, 180.0D + y, loc.getZ() + z);
						if (l.getBlock().getType() != Material.AIR) {
							hasGladi = true;
							loc = new Location(loc.getWorld(), loc.getX() + 20.0D, loc.getY(), loc.getZ());
							stop = true;
						}
						if (loc.getBlock().getType() != Material.AIR) {
							hasGladi = true;
							loc = new Location(loc.getWorld(), loc.getX() + 20.0D, loc.getY(), loc.getZ());
							stop = true;
						}
						if (loc.getBlock().getType() != Material.AIR) {
							hasGladi = true;
							loc = new Location(loc.getWorld(), loc.getX() + 20.0D, loc.getY(), loc.getZ());
							stop = true;
						}
						if (stop) {
							break;
						}
					}
					if (stop) {
						break;
					}
				}
				if (stop) {
					break;
				}
			}
		}
		Block mainBlock = loc.getBlock();
		generateArena(mainBlock);
		this.tpLocGladiator = this.gladiator.getLocation().clone();
		this.tpLocTarget = this.target.getLocation().clone();
		String messagegla = "§eVocê desafiou o jogador §a" + this.target.getName() + "§f para uma batalha!";
		String messagetar = "§eVocê foi desafiado pelo jogador §a" + this.gladiator.getName() + "§f para uma batalha!";
		this.gladiator.sendMessage(messagegla);
		this.target.sendMessage(messagetar);
		this.target.showPlayer(this.gladiator);
		this.gladiator.showPlayer(this.target);
		Location l1 = new Location(mainBlock.getWorld(), mainBlock.getX() + 6.5D, 181.0D, mainBlock.getZ() + 6.5D);
		l1.setYaw(135.0F);
		Location l2 = new Location(mainBlock.getWorld(), mainBlock.getX() - 5.5D, 181.0D, mainBlock.getZ() - 5.5D);
		l2.setYaw(315.0F);
		this.target.teleport(l1);
		this.gladiator.teleport(l2);
		GladiatorKit.playersIn1v1.add(this.gladiator.getUniqueId());
		GladiatorKit.playersIn1v1.add(this.target.getUniqueId());
		this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.witherEffect = new BukkitRunnable() {
			public void run() {
				GladiatorFight.this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1200, 5));
				GladiatorFight.this.target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 1200, 5));
			}
		};
		this.witherEffect.runTaskLater(Main.getPlugin(), 2400L);
		this.teleportBack = new BukkitRunnable() {
			public void run() {
				GladiatorFight.this.teleportBack(GladiatorFight.this.tpLocGladiator, GladiatorFight.this.tpLocTarget);
			}
		};
		this.teleportBack.runTaskLater(Main.getPlugin(), 3600L);
	}

	public void teleportBack(Location loc, Location loc1) {
		GladiatorKit.playersIn1v1.remove(this.gladiator.getUniqueId());
		GladiatorKit.playersIn1v1.remove(this.target.getUniqueId());
		this.gladiator.teleport(loc);
		removeBlocks();
		this.gladiator.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		this.gladiator.removePotionEffect(PotionEffectType.WITHER);
		this.target.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void teleportBack(Player winner, Player loser) {
		GladiatorKit.playersIn1v1.remove(winner.getUniqueId());
		GladiatorKit.playersIn1v1.remove(loser.getUniqueId());
		winner.teleport(this.tpLocGladiator);
		removeBlocks();
		winner.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		winner.removePotionEffect(PotionEffectType.WITHER);

		loser.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 100000));
		loser.removePotionEffect(PotionEffectType.WITHER);
		stop();
		destroy();
	}

	public void generateArena(Block mainBlock) {
		for (double x = -8.0D; x <= 8.0D; x += 1.0D) {
			for (double z = -9.0D; z <= 9.0D; z += 1.0D) {
				for (double y = 0.0D; y <= 15.0D; y += 1.0D) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 180.0D + y,
							mainBlock.getZ() + z);
					l.getBlock().setType(Material.GLASS);
					GladiatorKit.gladiatorBlocks.add(l.getBlock());
					this.blocksToRemove.add(l.getBlock());
				}
			}
		}
		for (double x = -7.0D; x <= 7.0D; x += 1.0D) {
			for (double z = -8.0D; z <= 8.0D; z += 1.0D) {
				for (double y = 1.0D; y <= 15.0D; y += 1.0D) {
					Location l = new Location(mainBlock.getWorld(), mainBlock.getX() + x, 180.0D + y,
							mainBlock.getZ() + z);
					l.getBlock().setType(Material.AIR);
					GladiatorKit.gladiatorBlocks.remove(l.getBlock());
				}
			}
		}
	}

	public void generateArenaa(Block mainBlock) {
	}

	public void dropItems(Player p, Location l) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		for (ItemStack item : p.getPlayer().getInventory().getContents()) {
			if ((item != null) && (item.getType() != Material.AIR)) {
				items.add(item.clone());
			}
		}
		for (ItemStack item : p.getPlayer().getInventory().getArmorContents()) {
			if ((item != null) && (item.getType() != Material.AIR)) {
				items.add(item.clone());
			}
		}
		if ((p.getPlayer().getItemOnCursor() != null) && (p.getPlayer().getItemOnCursor().getType() != Material.AIR)) {
			items.add(p.getPlayer().getItemOnCursor().clone());
		}
		dropItems(p, items, l);
	}

	public void dropItems(Player p, List<ItemStack> items, Location l) {
		World world = l.getWorld();
		for (ItemStack item : items) {
			if ((item != null) && (item.getType() != Material.AIR)) {
				if ((item.getType() != Material.POTION) || (item.getDurability() != 8201)) {
					if (item.getType() != Material.SKULL_ITEM) {
						if (item.hasItemMeta()) {
							world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
						} else {
							world.dropItemNaturally(l, item);
						}
					}
				}
			}
		}
	}

	public void removeBlocks() {
		for (Block b : this.blocksToRemove) {
			if (b.getType() != Material.AIR) {
				b.setType(Material.AIR);
			}
			GladiatorKit.gladiatorBlocks.remove(b);
		}
		this.blocksToRemove.clear();
	}

	public void stop() {
		this.witherEffect.cancel();
		this.teleportBack.cancel();
	}
}
