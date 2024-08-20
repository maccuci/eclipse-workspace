package me.tony.pvp;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import me.tony.pvp.manager.Manager;

public class Main extends JavaPlugin {
	
	private static Manager manager;
	
	@Override
	public void onLoad() {
		for(World world : Bukkit.getWorlds()) {
			world.setThundering(false);
			world.setStorm(false);
			world.setAutoSave(false);
			world.setWeatherDuration(1000);
			world.setTime(6000L);
		}
	}
	
	@Override
	public void onEnable() {
		manager = new Manager();
	}
	
	public static Manager getManager() {
		return manager;
	}
	
	public static Main getInstance() {
		return Main.getPlugin(Main.class);
	}
}
