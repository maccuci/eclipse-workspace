package com.fuzion.hg.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.api.admin.AdminAPI.AdminMode;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.hg.Main;
import com.fuzion.hg.event.SpectorJoinEvent;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.game.GameStage;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.kit.KitMatch;
import com.fuzion.hg.manager.timer.GameTimer;
import com.fuzion.hg.util.NameUtils;

import lombok.Getter;

public class DeathListener implements Listener {
	
	public static Set<UUID> relogProcess = new HashSet<>();
	private static Set<UUID> playerRelogged = new HashSet<>();
	GroupManager groupManager = new GroupManager();
	@Getter
	private static HashMap<UUID, String> deathMessages = new HashMap<>();
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onLogin(PlayerLoginEvent event) {
		Player player = event.getPlayer();
		
		if(!GameManager.isPreGame()) {
			if(!GameManager.isSpector(player)) {
				if (relogProcess.contains(player.getUniqueId()))
					event.allow();
			}
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		if (relogProcess.contains(player.getUniqueId())) {
			playerRelogged.add(player.getUniqueId());
			new BukkitRunnable() {
				@Override
				public void run() {
					relogProcess.remove(player.getUniqueId());
				}
			}.runTaskLater(Main.getPlugin(), 60 * 20);
			return;
		}
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		String originalDeathMessage = event.getDeathMessage();
		event.setDeathMessage(null);
		event.getDrops().clear();
		
		deathPlayer(player);
		if(groupManager.hasGroupPermission(player.getUniqueId(), Group.PREMIUM)) {
			if(GameTimer.getTimer() <= 300) {
				player.setHealth(20D);
				player.setFoodLevel(20);
				player.setSaturation(5);
				player.setFireTicks(0);
				Random r = new Random();
				int x = 100 + r.nextInt(400);
				int z = 100 + r.nextInt(400);
				if (r.nextBoolean())
					x = -x;
				if (r.nextBoolean())
					z = -z;
				World world = player.getWorld();
				int y = world.getHighestBlockYAt(x, z);
				Location loc = new Location(world, x, y, z);
				if (!loc.getChunk().isLoaded()) {
					loc.getChunk().load();
				}
				player.teleport(loc.clone().add(0, 0.5, 0));
				player.sendMessage("§6Você renasceu! Você pode renascer até os 5 minutos de jogo, mas você não recebe o item do seu kit novamente. Boa Sorte!");
				new BukkitRunnable() {
					@Override
					public void run() {
						player.getInventory().addItem(new ItemStack(Material.COMPASS));
					}
				}.runTaskLater(Main.getPlugin(), 1);
				return;
			}
		}
		DamageCause cause = null;
		Player killer = null;
		if (player.getLastDamageCause() != null && player.getLastDamageCause().getCause() != null)
			cause = player.getLastDamageCause().getCause();
		String causeString = cause.toString().toLowerCase();
		switch (cause) {
		case PROJECTILE:
			if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) player.getLastDamageCause();
				if (e.getDamager() instanceof Projectile) {
					Projectile projectile = (Projectile) e.getDamager();
					ProjectileSource shooter = (ProjectileSource)projectile.getShooter();
					if (shooter instanceof Player) {
						killer = (Player) shooter;
						causeString = "projectile_player";
					} else if (shooter instanceof Entity) {
						causeString = "projectile_entity";
					} else {
						causeString = null;
					}
				}
			}
			break;
		case ENTITY_ATTACK:
			if (player.getLastDamageCause() instanceof EntityDamageByEntityEvent) {
				EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) player.getLastDamageCause();
				if (e.getDamager() instanceof Player) {
					causeString = "entity_attack_player";
					killer = player.getKiller();
				} else {
					causeString = "entity_attack_entity";
				}
			} else {
				causeString = null;
			}
			break;
		case CUSTOM:
			if (event.getDeathMessage() != null)
				if (originalDeathMessage.contains("desistiu")) {
					causeString = "leave";
					break;
				}
			causeString = "border";
		default:
			break;
		}
		if (killer != null) {
			for(Player online : Bukkit.getOnlinePlayers()) {
				String strDeathMessage = "§eO Jogador §7" + killer.getName() + "(§8" + new KitManager().getKitName(killer) + "§7) §ematou o jogador §7" + player.getName() + "(§8" + new KitManager().getKitName(player) + "§7) §eusando " + NameUtils.getItemName(killer.getItemInHand());
				online.sendMessage(strDeathMessage);
				deathMessages.put(player.getUniqueId(), strDeathMessage);
				GameManager.addKill(killer);
			}
		}
		KitMatch.removeKitMatch(player);
		if(groupManager.hasGroupPermission(player.getUniqueId(), Group.TRIAL)) {
			GameManager.removePlayer(player);
			new AdminAPI().updateMode(player, AdminMode.ADMIN);
		} else if(groupManager.hasGroupPermission(player.getUniqueId(), Group.PREMIUM)) {
			GameManager.removePlayer(player);
			SpectorJoinEvent spectorJoinEvent = new SpectorJoinEvent(player);
			Bukkit.getPluginManager().callEvent(spectorJoinEvent);
		}
		GameManager.checkWinner();
		if(!(groupManager.hasGroupPermission(player.getUniqueId(), Group.ULTRA)) && !groupManager.hasGroupPermission(player.getUniqueId(), Group.TRIAL)) {
			int number = GameManager.getPlayerSize() + 1;
			if (number <= 10) {
				player.kickPlayer("§eParabéns! Você ficou entre os 10 jogadores da partida.\n O jogador §7" + killer.getName() + " §ete matou!\n§6Deseja espectar a partida? Compre vip em: www.loja.fuzion-network.com");
				return;
			}
			player.kickPlayer("§eQue pena, você se foi muito cedo.\n O jogador §7" + killer.getName() + " §ete matou!\n§6Deseja espectar a partida? Compre vip em: www.loja.fuzion-network.com");
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(!GameManager.isInvincibility())
			return;
		if(player.isDead()) {
			event.setQuitMessage(null);
			return;
		}
		event.setQuitMessage(null);
		
		if(GameManager.isSpector(player)) {
			event.setQuitMessage(null);
			return;
		}
		
		if (!relogProcess.contains(player.getUniqueId())) {
			relogProcess.add(player.getUniqueId());
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if (!playerRelogged.contains(player.getUniqueId())) {
						for(Player online : Bukkit.getOnlinePlayers()) {
							KitMatch.removeKitMatch(player);
							online.sendMessage("§eO Jogador §7" + player.getName() + "(§8" + new KitManager().getKitName(player) + "§7) §edemorou para entrar novamente na partida.");
							deathPlayer(player);
							if(groupManager.hasGroupPermission(player.getUniqueId(), Group.TRIAL)) {
								new AdminAPI().updateMode(player, AdminMode.ADMIN);
							} else if(groupManager.hasGroupPermission(player.getUniqueId(), Group.PREMIUM)) {
								GameManager.removePlayer(player);
								//GameManager.addSpector(player);
								SpectorJoinEvent spectorJoinEvent = new SpectorJoinEvent(player);
								Bukkit.getPluginManager().callEvent(spectorJoinEvent);
							}
							relogProcess.remove(player.getUniqueId());
							GameManager.checkWinner();
						}
					} else {
						playerRelogged.remove(player.getUniqueId());
					}
				}
			}.runTaskLater(Main.getPlugin(), 60 * 20);
			return;
		}
		if(GameManager.getStage() == GameStage.GAME) {
			for(Player online : Bukkit.getOnlinePlayers()) {
				KitMatch.removeKitMatch(player);
				deathPlayer(player);
				online.sendMessage("§eO Jogador §7" + player.getName() + "(§8" + new KitManager().getKitName(player) + "§7) §esaiu da partida.");
			}
			if(groupManager.hasGroupPermission(player.getUniqueId(), Group.TRIAL)) {
				new AdminAPI().updateMode(player, AdminMode.ADMIN);
			} else if(groupManager.hasGroupPermission(player.getUniqueId(), Group.PREMIUM)) {
				GameManager.removePlayer(player);
				//GameManager.addSpector(player);
				SpectorJoinEvent spectorJoinEvent = new SpectorJoinEvent(player);
				Bukkit.getPluginManager().callEvent(spectorJoinEvent);
			}
			GameManager.checkWinner();
		}
	}
	
	public static void deathPlayer(Player p) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		PlayerInventory inv = p.getInventory();
		for (ItemStack item : inv.getContents())
			if (checkNotNull(item))
				items.add(item.clone());
		for (ItemStack item : inv.getArmorContents())
			if (checkNotNull(item))
				items.add(item.clone());
		if (checkNotNull(p.getItemOnCursor()))
			items.add(p.getItemOnCursor().clone());
		dropAndClear(p, items, p.getLocation());
	}

	private static boolean checkNotNull(ItemStack item) {
		return item != null && item.getType() != Material.AIR;
	}
	
	public static void dropAndClear(Player p, List<ItemStack> items, Location l) {
		Kit kit = new KitManager().getKit(p);
		if (kit != null) {
			Iterator<ItemStack> iterator = items.iterator();
			while (iterator.hasNext()) {
				ItemStack item = iterator.next();
				if(kit.getIcon() == item.getType()) {
					iterator.remove();
					break;
				}
			}
		}
		dropItems(items, l);
		p.closeInventory();
		p.getInventory().setArmorContents(new ItemStack[4]);
		p.getInventory().clear();
		p.setItemOnCursor(null);
		for (PotionEffect pot : p.getActivePotionEffects()) {
			p.removePotionEffect(pot.getType());
			break;
		}
	}
	
	public static void dropItems(List<ItemStack> items, Location l) {
		World world = l.getWorld();
		for (ItemStack item : items) {
			if (item == null || item.getType() == Material.AIR)
				continue;
			if (item.hasItemMeta())
				world.dropItemNaturally(l, item.clone()).getItemStack().setItemMeta(item.getItemMeta());
			else
				world.dropItemNaturally(l, item);
		}
	}
}
