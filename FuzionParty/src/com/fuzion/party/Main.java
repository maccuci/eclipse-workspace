package com.fuzion.party;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzion.party.listener.PlayerListener;
import com.fuzion.party.manager.classes.ClassManager;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	@Override
	public void onEnable() {
		System.out.println("Ativado");
		ClassManager.initializateKits("com.fuzion.party.kits");
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}
}
