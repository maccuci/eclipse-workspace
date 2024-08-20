package com.fuzion.kitpvp;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzion.core.bukkit.event.UpdateScheduler;
import com.fuzion.kitpvp.listeners.CombatLogListener;
import com.fuzion.kitpvp.listeners.DeathListener;
import com.fuzion.kitpvp.listeners.ItemFrameListener;
import com.fuzion.kitpvp.listeners.KnockBackListener;
import com.fuzion.kitpvp.listeners.PlayerListener;
import com.fuzion.kitpvp.manager.command.CommandLoader;
import com.fuzion.kitpvp.manager.kit.KitManager;
import com.fuzion.kitpvp.manager.menu.KitMenu;
import com.fuzion.kitpvp.manager.menu.WarpMenu;
import com.fuzion.kitpvp.manager.onevsone.Warp1v1;
import com.fuzion.kitpvp.manager.position.PositionManager;
import com.fuzion.kitpvp.manager.protection.ProtectionListener;
import com.fuzion.kitpvp.manager.warp.WarpManager;

import lombok.Getter;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	public void onLoad() {
		for (World world : Bukkit.getWorlds()) {
			world.setThundering(false);
			world.setStorm(false);
			world.setAutoSave(false);
			world.setTime(0);
		}
	}
	
	@Getter
	private PositionManager positionManager;
	
	@Getter
	private WarpManager warpManager;
	
	@Override
	public void onEnable() {
		System.out.println("Ativado.");
		positionManager = new PositionManager();
		positionManager.run();
		warpManager = new WarpManager();
		new UpdateScheduler().run();
		
		KitManager.initializateKits("com.fuzion.kitpvp.kits");
		new CommandLoader().loadCommandsFromPackage("com.fuzion.kitpvp.command");
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new KitMenu(), this);
		pm.registerEvents(new WarpMenu(), this);
		pm.registerEvents(new DeathListener(), this);
		pm.registerEvents(new KnockBackListener(), this);
		pm.registerEvents(new ProtectionListener(), this);
		pm.registerEvents(new ItemFrameListener(), this);
		pm.registerEvents(new Warp1v1(), this);
		pm.registerEvents(new CombatLogListener(), this);
	}
}
