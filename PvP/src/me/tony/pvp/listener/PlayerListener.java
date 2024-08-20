package me.tony.pvp.listener;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
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
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import me.tony.commons.api.hologram.Hologram;
import me.tony.commons.api.item.ItemBuilder;
import me.tony.commons.bukkit.player.tag.TagManager;
import me.tony.commons.bukkit.player.tag.list.TagTypes;
import me.tony.commons.core.data.management.DataManager;
import me.tony.pvp.Main;
import me.tony.pvp.manager.level.LevelManager;
import me.tony.pvp.manager.menu.KitMenu;
import me.tony.pvp.manager.menu.MarketMenu;
import me.tony.pvp.manager.menu.WarpMenu;
import me.tony.pvp.manager.scoreboard.ScoreboardManager;

public class PlayerListener implements Listener {
	
	private List<Hologram> holograms = new ArrayList<>();
	
	@EventHandler
	void playerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ItemBuilder itemBuilder = new ItemBuilder();
		event.setJoinMessage(null);
		
		player.setFoodLevel(20);
		player.setHealth(20D);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		
		new TagManager().updateTagCommand(player, TagTypes.ARES);
		
		player.getInventory().setItem(3, itemBuilder.type(Material.PAPER).name("§bWarps §7(Clique)").build());
		player.getInventory().setItem(4, itemBuilder.type(Material.CHEST).name("§eSeus Kits §7(Clique)").build());
		player.getInventory().setItem(5, itemBuilder.type(Material.EMERALD).name("§aMercado §7(Clique)").build());
		player.getInventory().setItem(8, itemBuilder.type(Material.HOPPER).name("§8Mercado Norturno §7(Clique)").glow().build());
		
		new ScoreboardManager().createScoreboard(player);
	}
	
	@EventHandler
	void playerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		TagManager tagManager = new TagManager();
		
		event.setFormat(LevelManager.getFormattedFromColors(player.getLevel()) + tagManager.getTag(player).getPrefix() + player.getDisplayName() + " §6§l: §7" + event.getMessage().replace("%", "%%"));
	}
	
	@EventHandler
	void playerInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if(ItemBuilder.checkItem(item, "§eSeus Kits §7(Clique)")) {
			KitMenu.debug(player, 1);
		}
		
		if(ItemBuilder.checkItem(item, "§bWarps §7(Clique)")) {
			WarpMenu.warpsList(player);
		}
		
		if(ItemBuilder.checkItem(item, "§aMercado §7(Clique)")) {
			MarketMenu.normal(player);
			player.sendMessage("§a§lMestre do Mercado: §fEi! Você não pode acessar isto ainda. Estou organizando ainda, tente outra hora!");
		}
		
		if(ItemBuilder.checkItem(item, "§8Mercado Norturno §7(Clique)")) {
			player.sendMessage("§8§lLíder do Mercado Nortuno: §fNon est tamen paratum.");
			MarketMenu.nocture(player);
		}
	}
	
	@EventHandler
	void foodLevel(FoodLevelChangeEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	void breakBlocks(BlockBreakEvent event) {
		Block block = event.getBlock();
		
		if(block == null)
			return;
		
		if(block.hasMetadata("box")) {
			for(int i = 0; i < holograms.size(); i++) {
				Hologram hologram = holograms.get(i);
				hologram.remove();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	void placeBlocks(BlockPlaceEvent event) {
		Block block = event.getBlockPlaced();
		Material type = block.getType();
		
		if(type == Material.ENDER_CHEST) {
			Hologram hologram = new Hologram("§5Mercado Nortuno", block.getLocation().add(0.55, 0, 0.55), true);
			//hologram.addLine("§4Saia! O mercado nortuno está fechado.");
			hologram.addLine("§aO mercado nortuno está aberto!");
			block.setMetadata("box", new FixedMetadataValue(Main.getInstance(), true));
			for(Player player : Bukkit.getOnlinePlayers()) {
				if(Bukkit.getOnlinePlayers().length == 0)
					return;
				hologram.show(player);
			}
			holograms.add(hologram);
		}
	}
	
	@EventHandler
	void playerInteractBox(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		
		if(block == null)
			return;
		if(block.getType() == null)
			return;
		if(!block.hasMetadata("box"))
			return;
		if(event.getAction() == Action.LEFT_CLICK_BLOCK)
			return;
		
		if(block.hasMetadata("box") || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			event.setCancelled(true);
			MarketMenu.nocture_open(player);
		}
	}
	
	@EventHandler
	void scoreboardQuit(PlayerQuitEvent event) {
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
	public void scoreboardJoin(PlayerJoinEvent e) {
		e.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
	@EventHandler
	void motd(ServerListPingEvent event) {
		DataManager.handlerMotd("pvp", event);
	}
}
