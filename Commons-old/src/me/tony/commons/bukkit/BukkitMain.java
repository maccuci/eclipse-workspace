package me.tony.commons.bukkit;

import me.tony.commons.Core;
import me.tony.commons.bukkit.manager.CoreManager;

public class BukkitMain extends Core {
	
	private CoreManager coreManager;

	public void running() {
		coreManager = new CoreManager(this);
	}

	public void stoping() {
		
	}
	
	public boolean isLoaded() {
		return true;
	}
	
	public CoreManager getCoreManager() {
		return coreManager;
	}
}
