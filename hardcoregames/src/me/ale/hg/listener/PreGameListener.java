package me.ale.hg.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.ale.hg.api.hologram.HologramAPI;
import me.ale.hg.api.item.ItemBuilder;
import me.ale.hg.event.game.GameChangeEvent;
import me.ale.hg.event.player.PlayerSelectKitEvent;
import me.ale.hg.manager.ScoreboardManager;
import me.ale.hg.manager.game.GameManager;
import me.ale.hg.manager.game.GameType;
import me.ale.hg.manager.kit.Kit;
import me.ale.hg.manager.kit.KitManager;
import me.ale.hg.manager.menu.CustomKitMenu;
//import me.ale.hg.manager.menu.KitMenu;
import me.ale.hg.manager.menu.MarketMenu;

public class PreGameListener implements Listener {
	
	private static final ItemBuilder builder = new ItemBuilder();
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
			event.setJoinMessage(null);
			player.getInventory().clear();
			player.getInventory().addItem(builder.type(Material.CHEST).name("§aSeus Kits").build());
			player.getInventory().addItem(builder.type(Material.EMERALD).name("§aLoja de Kits").build());
			
			HologramAPI hologram = new HologramAPI();
			hologram.setText("§aBem-Vindo ao servidor " + player.getName());
			hologram.setLocation(player.getLocation().add(0.5, 0.5, 0.5));
			hologram.spawntemp(10);
			new ScoreboardManager().createScoreboard(player);
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		
		if(ItemBuilder.checkItem(item, "§aSeus Kits")) {
			new CustomKitMenu().open(player);
		}
		
		if(ItemBuilder.checkItem(item, "§aLoja de Kits")) {
			MarketMenu.open(player, 1);
		}
	}
	
	@EventHandler
	public void onGame(GameChangeEvent event) {
		GameType stage = event.getNewStage();
		GameManager.setType(stage);
		GameManager.startTime(stage);
	}
	
	@EventHandler
	public void onSelect(PlayerSelectKitEvent event) {
		Player player = event.getPlayer();
		Kit kit = event.getKit();
		new KitManager().setKit(player, kit);
		player.sendMessage("§aVocê selecionou o kit " + kit.getName());
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerQuitListener(PlayerQuitEvent e) {
		Scoreboard board = e.getPlayer().getScoreboard();
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
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerJoinListener(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
}
