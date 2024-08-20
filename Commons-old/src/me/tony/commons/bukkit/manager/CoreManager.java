package me.tony.commons.bukkit.manager;

import org.bukkit.Bukkit;

import me.tony.commons.Core;
import me.tony.commons.api.hologram.HologramListener;
import me.tony.commons.api.hologram.TempHologram;
import me.tony.commons.bukkit.BukkitMain;
import me.tony.commons.bukkit.event.UpdateScheduler;
import me.tony.commons.bukkit.player.permissions.PermissionManager;
import me.tony.commons.core.data.management.DataManager;
import me.tony.commons.util.ClassLoader;

public class CoreManager {
	
	private UpdateScheduler updateScheduler;
	private ClassLoader classLoader;
	private DataManager dataManager;
	private BukkitMain plugin;
	private PermissionManager permissionManager;
	
	public CoreManager(Core core) {
		plugin = BukkitMain.getPlugin(BukkitMain.class);
		updateScheduler = new UpdateScheduler(this);
		classLoader = new ClassLoader(this);
		dataManager = new DataManager(this);
		permissionManager = new PermissionManager(this);
		
		dataManager.openConnection();
		updateScheduler.run();
		classLoader.load();
		
		permissionManager.loadRanks();
		
		Bukkit.getPluginManager().registerEvents(new HologramListener(), plugin);
		Bukkit.getPluginManager().registerEvents(new TempHologram(), plugin);
	}
	
	public BukkitMain getPlugin() {
		return plugin;
	}
	
	public DataManager getDataManager() {
		return dataManager;
	}
	
	public UpdateScheduler getUpdateScheduler() {
		return updateScheduler;
	}
	
	public ClassLoader getClassLoader() {
		return classLoader;
	}
}
