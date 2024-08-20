package me.feenks.wdr.bungee;

import me.feenks.wdr.bungee.commands.WDRBungeeCommand;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

public class WDRBungee extends Plugin {
	
	@Override
	public void onEnable() {
		ProxyServer.getInstance().getPluginManager().registerCommand(this, new WDRBungeeCommand());
	}

}
