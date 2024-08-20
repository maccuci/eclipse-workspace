package me.tony.commons.bukkit;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.plugin.java.JavaPlugin;

import me.tony.commons.core.Commons;
import me.tony.commons.core.CommonsPlatform;

public class BukkitMain extends JavaPlugin {
	
	public static BukkitMain getPlugin() {
		return BukkitMain.getPlugin(BukkitMain.class);
	}
	
	CommonsPlatform platform = new BukkitPlatform();
	
	@Override
	public void onEnable() {
		platform.setClassInicializate(BukkitMain.class);
		
		Commons.getTagManagement().createDefaultsTags();
		
		getLogger().log(new LogRecord(Level.INFO, "Commons was actived."));
	}
}
