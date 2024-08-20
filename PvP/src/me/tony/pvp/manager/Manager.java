package me.tony.pvp.manager;

import org.bukkit.Bukkit;

import me.tony.pvp.Main;
import me.tony.pvp.listener.PlayerListener;
import me.tony.pvp.manager.kit.KitManager;

public class Manager {
	
	private Main plugin;
	private KitManager kitManager;
	public static boolean FULL_IRON_MODE;
	
	public Manager() {
		this.plugin = Main.getInstance();
		getPlugin().saveDefaultConfig();
		kitManager = new KitManager();
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);

		KitManager.initializateKits("me.tony.pvp.kits");
		
		System.out.println("Starting the plugin " + plugin.getName() + " version " + plugin.getDescription().getVersion() + "...");
	}
	
	public Main getPlugin() {
		return plugin;
	}
	
	public KitManager getKitManager() {
		return kitManager;
	}
}
