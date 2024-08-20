package com.fuzion.hg;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzion.core.bukkit.event.UpdateScheduler;
import com.fuzion.hg.listener.BorderListener;
import com.fuzion.hg.listener.DamageListener;
import com.fuzion.hg.listener.DeathListener;
import com.fuzion.hg.listener.InvincibilityListener;
import com.fuzion.hg.listener.PlayerListener;
import com.fuzion.hg.listener.PreGameListener;
import com.fuzion.hg.manager.command.CommandLoader;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.game.GameStage;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.menu.KitMenu;
import com.fuzion.hg.manager.postion.PositionManager;
import com.fuzion.hg.manager.timer.PreGameTimer;
import com.fuzion.hg.manager.winner.WinnerManager;

import lombok.Getter;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	public static final int PREGAME_TIMER = 300;
	public static final int INVINCIBILITY_TIMER = 120;
	public static final int GAME_TIMER = INVINCIBILITY_TIMER;
	@Getter
	private WinnerManager winnerManager;
	
	@Getter
	private PositionManager positionManager;
	
	@Override
	public void onEnable() {
		getLogger().info("Ativado.");
		GameManager.setStage(GameStage.WAITING);
		saveDefaultConfig();
		
		new PreGameTimer().pulse();
		
		positionManager = new PositionManager();
		positionManager.run();
		
		KitManager.initializateKits("com.fuzion.hg.kits");
		new CommandLoader().loadCommandsFromPackage("com.fuzion.hg.command");
		new UpdateScheduler().run();
		PlayerListener.prepareRecipes();
		winnerManager = new WinnerManager();
		
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PreGameListener(), this);
		pm.registerEvents(new InvincibilityListener(), this);
		pm.registerEvents(new KitMenu(), this);
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new DamageListener(), this);
		pm.registerEvents(new BorderListener(), this);
	}
}
