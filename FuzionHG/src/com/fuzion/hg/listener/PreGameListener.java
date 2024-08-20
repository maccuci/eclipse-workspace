package com.fuzion.hg.listener;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.bar.BarAPI;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.hg.manager.ScoreboardManager;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.kit.KitMatch;
import com.fuzion.hg.manager.menu.KitMenu;
import com.fuzion.hg.manager.menu.LuckMatchMenu;
import com.fuzion.hg.manager.menu.PlayersMenu;

public class PreGameListener implements Listener {
	
	private static final ItemBuilder builder = new ItemBuilder();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		if(GameManager.isPreGame()) {
			event.setJoinMessage(null);
			Player player = event.getPlayer();
			GameManager.addPlayer(player);
			player.getInventory().setArmorContents(null);
			player.getInventory().clear();
			player.getInventory().addItem(builder.type(Material.CHEST).name("§eSeletor de kits").build());
			player.getInventory().setItem(8, builder.type(Material.ENDER_CHEST).name("§6Surpresa da partida!").build());
			
			//new ScoreboardManager().setScoreboard(player);
			new ScoreboardManager().waiting(player);
			for(Player players : Bukkit.getOnlinePlayers()) {
				new ScoreboardManager().waiting(players);
			}
			TagManager tagManager = new TagManager(player);
			tagManager.findTag();
			tagManager.update();
			BarAPI.setMessage(player, "§e§k0000§r §6§lBEM VINDO AO FUZION HG §e§k0000", 30);
			new GroupManager().set(player.getUniqueId(), "playergroup", Group.DONO.name());
			player.sendMessage("§6Sem bem-vindo ao servidor de hardcoregames! O servidor está atualmente em BETA!");
			player.sendMessage(" ");
			player.sendMessage("§eAdicionamos o sistema de surpresa da partida! Cada partida você terá a chance de tirar um kit aleatório do servidor!");
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(ItemBuilder.checkItem(player.getItemInHand(), "§eSeletor de kits")) {
			KitMenu.open(player, 1);
			return;
		}
		if(player.getItemInHand().getType() == Material.ENDER_CHEST) {
			if(KitMatch.hasKitMatch(player, KitMatch.getKitMatch(player))) {
				player.sendMessage("§cVocê já abriu a sua supresa da partida!");
				return;
			}
			LuckMatchMenu.open(player);
		}
		
		if(ItemBuilder.checkItem(player.getItemInHand(), "§aJogadores")) {
			PlayersMenu.open(player, GameManager.getSpectors(), 1);
		}
		
		if(ItemBuilder.checkItem(player.getItemInHand(), "§6Item do seu kit")) {
			Kit kit = KitManager.getKit(new KitManager().getKitName(player));
			for(ItemStack item : kit.items) {
				player.getInventory().addItem(item);
				player.updateInventory();
			}
			player.getInventory().setItemInHand(new ItemStack(Material.AIR));
			player.sendMessage("§eVocê recebeu o item do seu kit de volta!");
		}
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent event) {
		if(GameManager.isPreGame())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(GameManager.isPreGame()) {
			if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(GameManager.isPreGame()) {
			if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		if(GameManager.isPreGame()) {
			event.setQuitMessage(null);
			GameManager.removePlayer(event.getPlayer());
		}
	}
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		Kit kit = new KitManager().getKit(event.getPlayer());
		
		if(kit == null)
			return;
		
		for(ItemStack items : kit.items) {
			if(event.getItemDrop().getItemStack() == items) {
				event.setCancelled(true);
				return;
			}
		}
	}
	
	@EventHandler
	public void onPortal(PlayerPortalEvent event) {
		event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if (event.getCause() == TeleportCause.NETHER_PORTAL || event.getCause() == TeleportCause.END_PORTAL)
			event.setCancelled(true);
	}
	

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (GameManager.DAMAGE == true) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		if (!(event.getEntity() instanceof Player))
			return;
		if (GameManager.DAMAGE == true) {
			event.setCancelled(false);
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent event) {
		if (event.getEntityType() == EntityType.GHAST || event.getEntityType() == EntityType.PIG_ZOMBIE) {
			event.setCancelled(true);
			return;
		}
		Random random = new Random();
		if (event.getSpawnReason() != SpawnReason.CUSTOM)
			return;
		if (random.nextInt(5) != 0) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void teleportSpawn(PlayerMoveEvent event) {
		if(!GameManager.isPreGame())
			return;
		Player player = event.getPlayer();
		Location playerLocation = player.getLocation();
		Location checkLocation = player.getWorld().getSpawnLocation();
		double distance = 45;
		
		if(playerLocation.distance(checkLocation) > distance) {
			player.teleport(checkLocation);
			player.sendMessage("§cVocê não pode ir tão longe antes de iniciar a partida.");
			return;
		}
	}
}
