package com.fuzion.party.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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
import com.fuzion.party.manager.ScoreboardManager;
import com.fuzion.party.manager.ScoreboardManager.ScoreboardType;
import com.fuzion.party.manager.menu.ClassesMenu;
import com.fuzion.party.manager.minegame.PointManager;

public class PlayerListener implements Listener {
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(null);
		player.setFoodLevel(20);
		player.getInventory().setArmorContents(null);
		player.getInventory().clear();
		player.getInventory().addItem(new ItemBuilder().type(Material.ENDER_CHEST).name("§aSelecione sua classe").lore("§7Utilize as mesmas para te dar vantagens durante a party.").build());
		PointManager.pointsMap.put(player, 0);
		ScoreboardManager.setScoreboard(player, ScoreboardType.GAME);
		TagManager tagManager = new TagManager(player);
		tagManager.findTag();
		tagManager.update();
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(Bukkit.getOnlinePlayers().length == 0)
				break;
			new TagManager(online).update();
			ScoreboardManager.setScoreboard(online, ScoreboardType.GAME);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		
		if(ItemBuilder.checkItem(item, "§aSelecione sua classe")) {
			ClassesMenu.open(player);
		}
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
	public void onQuit(PlayerQuitEvent event) {
		event.setQuitMessage(null);
	}
}
