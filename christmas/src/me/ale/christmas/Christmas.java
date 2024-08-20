package me.ale.christmas;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.christmas.listener.PlayerListener;
import me.ale.christmas.util.menu.MenuListener;
import me.ale.christmas.util.update.SchedulerEvent;
import me.ale.christmas.util.update.SchedulerEvent.SchedulerType;

public class Christmas extends JavaPlugin {
	
	public static Christmas getPlugin() {
		return Christmas.getPlugin(Christmas.class);
	}
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		System.out.println("Christmas plugin has been actived!");
		
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				getServer().getPluginManager().callEvent(new SchedulerEvent(SchedulerType.TICK));
			}
		}.runTaskTimer(getPlugin(), 0, 1);
	}
	
	@Override
	public void onDisable(){
		System.out.println("Christmas plugin has been desactived!");
	}
}
