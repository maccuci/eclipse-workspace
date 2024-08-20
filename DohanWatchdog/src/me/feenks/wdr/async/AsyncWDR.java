package me.feenks.wdr.async;

import me.feenks.core.DohanAPI;
import me.feenks.wdr.WDR;
import me.feenks.wdr.commands.WDRCommand;
import me.feenks.wdr.manager.CheckManager;

public class AsyncWDR extends Thread {
	
	private WDR plugin = WDR.getPlugin();
	
	@Override
	public void run() {
		//start all systems
		DohanAPI.debug("WDR has been actived.");
		
		plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, "BungeeCord");
		plugin.getCommand("wdr").setExecutor(new WDRCommand());
		
		CheckManager.initializateHacks("me.feenks.wdr.checks");
	}

}
