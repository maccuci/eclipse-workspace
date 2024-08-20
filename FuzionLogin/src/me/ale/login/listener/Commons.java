package me.ale.login.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import me.ale.login.Bukkit;
import me.ale.login.check.Check;
import me.ale.login.event.PlayerCheckEvent;
import me.ale.login.exception.InvalidCheckException;

public class Commons implements Listener {
	
	@EventHandler
	public void onCheck(PlayerCheckEvent event) {
		boolean isPremium = event.isPremium();
		
		if(isPremium) {
			event.getPlayer().sendMessage("§aVocê foi identificado como um jogador original.");
		}
	}

	@EventHandler
	private void onJoin(PlayerJoinEvent event) {
		if (Bukkit.getStorage().getPremiumMap().get(event.getPlayer().getName()) == null) {
			try {
				Bukkit.getStorage().setPremium(event.getPlayer().getName(),
						Check.fastCheck(event.getPlayer().getName()));
			} catch (InvalidCheckException e) {
				e.printStackTrace();
			}
		}

		PlayerCheckEvent checkEvent = new PlayerCheckEvent(event.getPlayer(),
				Bukkit.getStorage().getState(event.getPlayer().getName()));
		Bukkit.getPlugin().getServer().getPluginManager().callEvent(checkEvent);
	}

	@EventHandler
	private void onLeave(PlayerQuitEvent event) {
		if (!Bukkit.getStorage().getPremiumMap().containsKey(event.getPlayer().getName())) {
			return;
		}

		Bukkit.getStorage().removeVerified(event.getPlayer().getName(),
				Bukkit.getStorage().getState(event.getPlayer().getName()));
	}
}
