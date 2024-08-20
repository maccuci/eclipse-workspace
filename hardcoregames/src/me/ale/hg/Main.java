package me.ale.hg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.ale.hg.api.menu.MenuListener;
import me.ale.hg.event.UpdateScheduler;
import me.ale.hg.listener.PlayerDamage;
import me.ale.hg.listener.PreGameListener;
import me.ale.hg.manager.customkit.CustomKitManager;
import me.ale.hg.manager.game.GameManager;
import me.ale.hg.manager.kit.KitManager;
//import me.ale.hg.manager.timer.PreGameTimer;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	@Getter
	private GameManager gameManager;
	
	@Getter
	private CustomKitManager customKit;
	
	@Override
	public void onEnable() {
		System.out.println("HardcoreGames ativado.");
		//PreGameTimer.startCountdown();
		
		new UpdateScheduler().run();
		new KitManager().initializateKits("me.ale.hg.kits");
		//GameManager.setType(GameType.WAITING);
		gameManager = new GameManager();
		customKit = new CustomKitManager();
		customKit.create();
		
		Bukkit.getPluginManager().registerEvents(new PreGameListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDamage(), this);
		Bukkit.getPluginManager().registerEvents(new MenuListener(), this);
	}

}
