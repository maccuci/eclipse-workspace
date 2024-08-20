package me.tony.commons.bukkit.manager.management;

import me.tony.commons.bukkit.manager.CoreManager;

public abstract class Management {
	
	private String name;
	private CoreManager coreManager;
	
	public Management(CoreManager coreManager, String name) {
		this.name = name;
		this.coreManager = coreManager;
		
		System.out.println("The manager " + name + " was loaded correcly!");
	}
	
	public abstract boolean initialize();
	
	public String getName() {
		return name;
	}
	
	public CoreManager getCoreManager() {
		return coreManager;
	}
}
