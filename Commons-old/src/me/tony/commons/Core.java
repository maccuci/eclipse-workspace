package me.tony.commons;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class Core extends JavaPlugin implements Plugin {
	
	public void onEnable() {
		running();
	}
	
	public void onDisable() {
		stoping();
	}
	
	public abstract boolean isLoaded();
}
