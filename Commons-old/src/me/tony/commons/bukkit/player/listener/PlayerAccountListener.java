package me.tony.commons.bukkit.player.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.tony.commons.bukkit.player.account.AccountManager;

public class PlayerAccountListener implements Listener {
	
	@EventHandler
	void createAccount(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		AccountManager accountManager = new AccountManager();
		
		accountManager.craftAccount(player).load(true);
	}

}
