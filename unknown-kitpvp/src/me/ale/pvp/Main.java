package me.ale.pvp;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import me.ale.pvp.api.menu.MenuListener;
import me.ale.pvp.api.npc.FakePlayerAPI;
import me.ale.pvp.api.scheduler.SchedulerEvent;
import me.ale.pvp.api.scheduler.SchedulerEvent.SchedulerType;
import me.ale.pvp.listener.PlayerListener;
import me.ale.pvp.manager.injector.ServerInfoInjector;
import me.ale.pvp.manager.kit.KitManager;
import me.ale.pvp.manager.mysql.Database;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	@Getter
	private Database databaseGame;
	
	@Getter
	@Setter
	@NonNull
	private FakePlayerAPI fakePlayer;
	
	@Override
	public void onEnable() {
		System.out.print("Actived.");
		databaseGame = new Database();
		
		new KitManager().initializateKits("me.ale.pvp.kits");
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		new ServerInfoInjector().inject(this);
		
		new BukkitRunnable() {
			
			@Override
			public void run() {
				getServer().getPluginManager().callEvent(new SchedulerEvent(SchedulerType.TICK));
			}
		}.runTaskTimer(getPlugin(), 0, 1);
	}
}
