package com.fuzion.core.master;

import org.bukkit.plugin.java.JavaPlugin;

public class CorePlugin extends JavaPlugin implements Plugin {
	
	@Override
	public void onEnable() {
		run();
	}
	
	@Override
	public void onDisable() {
		stop();
	}
}
