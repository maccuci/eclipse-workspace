package me.tony.bedwars;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.tony.bedwars.manager.Manager;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	@Getter private Manager manager;
	
	@Override
	public void onEnable() {
		manager = new Manager();
		
		getLogger().log(new LogRecord(Level.INFO, "Bedwars was actived."));
	}
}
