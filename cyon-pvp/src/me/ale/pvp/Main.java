package me.ale.pvp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.core.server.ServerController;
import me.ale.commons.core.server.ServerType;
import me.ale.pvp.kits.MinionsKit;
import me.ale.pvp.listener.CombatLogListener;
import me.ale.pvp.listener.PlayerListener;
import me.ale.pvp.manager.achievement.AchievementListener;
import me.ale.pvp.manager.achievement.AchievementManager;
import me.ale.pvp.manager.command.CommandLoader;
import me.ale.pvp.manager.kit.KitManager;
import me.ale.pvp.manager.menu.KitMenu;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	@Getter
	private AchievementManager achievementManager;
	
	@Override
	public void onEnable() {
		ServerController.setServerType(ServerType.PVP);
		KitManager.initializateKits("me.ale.pvp.kits");
		KitMenu.kitNameSelection.clear();
		KitMenu.kitNameCheck.clear();
		MinionsKit.wolfs.clear();
		achievementManager = new AchievementManager(this);
		
		new CommandLoader().loadCommandsFromPackage("me.ale.pvp.commands");
		
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new CombatLogListener(), this);
		pm.registerEvents(new CooldownAPI(), this);
		pm.registerEvents(new AchievementListener(), this);
	}
}
