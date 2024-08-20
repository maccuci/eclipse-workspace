package com.fuzion.lobby.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.lobby.manager.ScoreboardManager;
import com.fuzion.lobby.manager.menu.CosmeticMenu;
import com.fuzion.lobby.manager.menu.ProfileMenu;
import com.fuzion.lobby.manager.menu.ServersMenu;

public class PlayerListener implements Listener {
	
	private static final ItemBuilder builder = new ItemBuilder();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(null);
		player.setFoodLevel(20);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.getInventory().setItem(0, builder.type(Material.COMPASS).name("§6§lEscolha o servidor").glow().build());
		player.getInventory().setItem(1, builder.type(Material.SKULL_ITEM).name("§e§lSeu perfil - " + player.getName()).durability(3).skin(player.getName()).build());
		player.getInventory().setItem(2, builder.type(Material.INK_SACK).name("§f§lJogadores §a§lON").durability(10).build());
		player.getInventory().setItem(7, builder.type(Material.NETHER_STAR).name("§a§lLobbies").build());
		player.getInventory().setItem(8, builder.type(Material.ENDER_CHEST).name("§5§lCosméticos").build());
		new ScoreboardManager().test(player);
		TagManager tagManager = new TagManager(player);
		tagManager.findTag();
		tagManager.update();
		for(Player online : Bukkit.getOnlinePlayers()) {
			new TagManager(online).update();
		}
		player.sendMessage("§4█§c█§4█§0██§4█§c█§4█\n" + 
				"§c█§4█§c█§0██§c█§4█§c█\n" + 
				"§0████████\n" + 
				"§0██§e█§0█§0█§e█§0█§0█\n" + 
				"§0█§f█§7█§e█§0█§7█§f█§0█\n" + 
				"§0█§f█§8█§e██§8█§f█§0█\n" + 
				"§e█████§b██§e█\n" + 
				"§e██████§b█§e█");
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		String message = event.getMessage().replace("%", "%%");
		Player player = event.getPlayer();
		TagManager tagManager = new TagManager(player);
		GroupManager groupManager = new GroupManager();
		boolean permission = groupManager.hasGroupPermission(player.getUniqueId(), Group.YOUTUBER);
		
			event.setFormat(tagManager.getTag().getName() + player.getName() + "§6§l: " + (permission ? "§f" : "§7") + message);
	}
	
	@EventHandler
	public void onFood(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent event) {
		if(event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if(item.getType() == Material.INK_SACK || item.getDurability() == 10) {
			player.setItemInHand(builder.type(Material.INK_SACK).name("§f§lJogadores §c§lOFF").durability(8).build());
			for(Player players : Bukkit.getOnlinePlayers()) {
				if(!(new GroupManager().getGroup(players.getUniqueId()).ordinal() < Group.TRIAL.ordinal())) {
					if(players.canSee(player)) {
						players.hidePlayer(player);
					}
				} else if(!players.canSee(player)) {
					players.showPlayer(player);
				}
			}
			player.updateInventory();
		} 
		if(ItemBuilder.checkItem(item, "§f§lJogadores §c§lOFF")) {
			player.setItemInHand(builder.type(Material.INK_SACK).name("§f§lJogadores §a§lON").durability(10).build());
			for(Player players : Bukkit.getOnlinePlayers()) {
				player.showPlayer(players);
			}
			player.updateInventory();
		}
		
		if(ItemBuilder.checkItem(item, "§5§lCosméticos")) {
			if(!new GroupManager().hasGroupPermission(player.getUniqueId(), Group.BETA)) {
				player.sendMessage("§cVocê não possui o grupo §1§lBETA §cpara usar isto agora!");
				return;
			}
			CosmeticMenu.open(player);
		}
		
		if(ItemBuilder.checkItem(item, "§6§lEscolha o servidor")) {
			ServersMenu.open(player);
		}
		
		if(ItemBuilder.checkItem(item, "§e§lSeu perfil - " + player.getName())) {
			ProfileMenu.open(player);
		}
	}
	
	@EventHandler
	public void onExplode(ExplosionPrimeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onCreature(CreatureSpawnEvent event) {
		if(event.getSpawnReason() != SpawnReason.CUSTOM) {
			event.setCancelled(true);
		}
	}
}
