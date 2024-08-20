package com.fuzion.core.master;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Core extends JavaPlugin {
	
	public abstract void running();
	public abstract void stoping();
	public static Plugin getPlugin() {
		return null;
	}
	
	@Override
	public void onEnable() {
			running();
	}
	
	@Override
	public void onDisable() {
		stoping();
	}
}
