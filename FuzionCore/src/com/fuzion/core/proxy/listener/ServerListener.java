package com.fuzion.core.proxy.listener;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerListener implements Listener {
	
	@EventHandler
	public void onMotd(ProxyPingEvent event) {
		ServerPing ping = event.getResponse();
		ping.setDescription("          §e§m---§r§6[ §lFuzionMC §8» §c§lPvP Network §6]§r§e§m---§r\n" 
			+	"                    §bDisponível para todos!");
		ping.getVersion().setName("1.0");
	}

}
