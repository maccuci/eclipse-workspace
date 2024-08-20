package me.feenks.core.bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.feenks.core.bukkit.utils.thread.Async;
import me.feenks.core.master.data.DataController;

public class BukkitMain extends JavaPlugin {
	
	public static BukkitMain getPlugin() {
		return BukkitMain.getPlugin(BukkitMain.class);
	}
	
	@Getter private DataController data = new DataController();
	
	@Override
	public void onEnable() {
		data.getLoadAsyncExecutor().execute(new Async());
	}
}
