package me.tony.bedwars.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.tony.bedwars.manager.item.ItemBuilder;
import me.tony.bedwars.manager.scoreboard.ScoreboardManager;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		event.setJoinMessage(null);
		
		player.getInventory().clear();
		player.getInventory().setArmorContents(null);
		
		player.getInventory().setItem(4, new ItemBuilder().type(Material.COMPASS).name("§aPlay BedWars §7(Click)").glow().build());
		player.getInventory().setItem(8, new ItemBuilder().type(Material.CHEST).name("§aCosmetcs §7(Click)").build());
		
		new ScoreboardManager().debug(player);
	}

}
