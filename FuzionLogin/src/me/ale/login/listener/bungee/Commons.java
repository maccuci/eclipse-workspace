package me.ale.login.listener.bungee;

import me.ale.login.Bungee;
import me.ale.login.check.Check;
import me.ale.login.event.bungee.PlayerCheckEvent;
import me.ale.login.event.bungee.PlayerPreCheckEvent;
import me.ale.login.exception.InvalidCheckException;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PreLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Commons implements Listener {
	
	@EventHandler
	public void onPreLogin(PreLoginEvent event) {
		if (event.isCancelled()) {
			return;
		}

		boolean isPremium;
		try {
			isPremium = Check.fastCheck(event.getConnection().getName());
		} catch (InvalidCheckException e) {
			e.printStackTrace();
			event.getConnection().disconnect();
			return;
		}

		PlayerPreCheckEvent checkEvent = new PlayerPreCheckEvent(event.getConnection(), isPremium);

		Bungee.getInstance().getProxy().getPluginManager().callEvent(checkEvent);

		if (event.isCancelled()) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onLogin(LoginEvent event) {
		if (event.isCancelled()) {
			return;
		}

		boolean isPremium;
		try {
			isPremium = Check.fastCheck(event.getConnection().getName());
		} catch (InvalidCheckException e) {
			e.printStackTrace();
			event.getConnection().disconnect();
			return;
		}

		PlayerCheckEvent checkEvent = new PlayerCheckEvent(event.getConnection(), isPremium);

		Bungee.getInstance().getProxy().getPluginManager().callEvent(checkEvent);

		if (event.isCancelled()) {
			event.setCancelled(true);
		}
	}

}
