package me.feenks.wdr;

import org.bukkit.plugin.java.JavaPlugin;

import me.feenks.wdr.async.AsyncWDR;

public class WDR extends JavaPlugin {
	
	public static WDR getPlugin() {
		return WDR.getPlugin(WDR.class);
	}
	
	public static final String PREFIX = "§f[§c§lWDR§f] ";
	
	@Override
	public void onEnable() {
		AsyncWDR async = new AsyncWDR();
		async.start();
	}

}
