package me.ale.pvp.listener;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.ale.pvp.Main;
import me.ale.pvp.api.npc.FakePlayerAPI;
import me.ale.pvp.api.npc.PacketReader;
import me.ale.pvp.manager.ItemBuilder;
import me.ale.pvp.manager.ScoreboardManager;
import me.ale.pvp.manager.menu.FactionMenu;
import me.ale.pvp.manager.menu.KitMenu;
import me.ale.pvp.manager.menu.TestMenu;
import me.ale.pvp.manager.menu.WarpsMenu;

public class PlayerListener implements Listener {
	
	private ItemBuilder builder = new ItemBuilder();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		PacketReader reader = new PacketReader(player);
		
		event.setJoinMessage(null);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.getInventory().setItem(0, builder.type(Material.CHEST).name("§eSeus Kits §7(Clique)").build());
		player.getInventory().setItem(1, builder.type(Material.COMPASS).name("§eWarps §7(Clique)").build());
		player.getInventory().setItem(4, builder.type(Material.SKULL_ITEM).skin(player.getName()).name("§ePerfil §7(Clique)").build());
		new ScoreboardManager().createScoreboard(player);
		reader.inject();
	}
	
	@EventHandler
	public void test(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		
		Main.getPlugin().setFakePlayer(new FakePlayerAPI("§aAZUL", player.getLocation(), "http://textures.minecraft.net/texture/71e82a346f0c5cd2bd2a18bd2dbb5eb43e1fd2581d72b198e1b79d17708e0e91"));
		Main.getPlugin().getFakePlayer().changeSkin();
		Main.getPlugin().getFakePlayer().spawn();
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(ItemBuilder.checkItem(player.getItemInHand(), Material.CHEST, "§eSeus Kits §7(Clique)")) {
			new KitMenu().open(player, 1);
			return;
		}
		
		if(ItemBuilder.checkItem(player.getItemInHand(), Material.COMPASS, "§eWarps §7(Clique)")) {
			new WarpsMenu().open(player);
			return;
		}
	}
	
	@EventHandler
	public void onInteractBlock(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if(event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		
		if(block.getType() == Material.NOTE_BLOCK) {
			event.setCancelled(true);
			new FactionMenu().open(player);
			return;
		}
		
		if(block.getType() == Material.FURNACE) {
			event.setCancelled(true);
			new TestMenu().open(player);
			return;
		}
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
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		
		event.setRespawnLocation(player.getWorld().getSpawnLocation());
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.getInventory().setItem(0, builder.type(Material.CHEST).name("§eSeus Kits §7(Clique)").build());
		player.getInventory().setItem(1, builder.type(Material.COMPASS).name("§eWarps §7(Clique)").build());
		player.getInventory().setItem(4, builder.type(Material.SKULL_ITEM).skin(player.getName()).name("§§ePerfil §7(Clique)").build());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
		
		Scoreboard board = event.getPlayer().getScoreboard();
		if (board != null) {
			for (Team t : board.getTeams()) {
				t.unregister();
				t = null;
			}
			for (Objective ob : board.getObjectives()) {
				ob.unregister();
				ob = null;
			}
			board = null;
		}
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}
