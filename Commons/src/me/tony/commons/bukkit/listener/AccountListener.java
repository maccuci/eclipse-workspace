package me.tony.commons.bukkit.listener;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.tony.commons.core.Commons;
import me.tony.commons.core.account.CommonsPlayer;
import me.tony.commons.core.managements.TagCommon;

public class AccountListener implements Listener {
	
	@EventHandler
	public synchronized void asyncPreLogin(AsyncPlayerPreLoginEvent event) {
		if (Bukkit.getPlayer(event.getUniqueId()) != null) {
			event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
			event.setKickMessage("§cSua conta já está carregada no servidor.");
			return;
		}
		try {
			CommonsPlayer account = Commons.getAccount(event.getUniqueId());
			
			if(account == null) {
				account = new CommonsPlayer(event.getUniqueId(), event.getName());
				account.load();
			} else {
				account.setJoinData(event.getName(), event.getAddress().getHostName());
			}
		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.SEVERE, e, "Error to load account.");
		}
	}
	
	@EventHandler
	public void playerJoin(PlayerJoinEvent event) {
		new TagCommon().listTags(event.getPlayer());
	}
}
