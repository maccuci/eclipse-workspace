package me.ale.commons.bukkit;

import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import lombok.Getter;
import me.ale.commons.api.menu.MenuListener;
import me.ale.commons.bukkit.event.UpdateScheduler;
import me.ale.commons.bukkit.listener.PlayerListener;
import me.ale.commons.core.CorePlugin;
import me.ale.commons.core.account.manager.FactionManager;
import me.ale.commons.core.account.manager.PackColletionManager;
import me.ale.commons.core.account.manager.RankManager;
import me.ale.commons.core.account.manager.StatsManager;
import me.ale.commons.core.command.CommandLoader;
import me.ale.commons.core.mysql.DatabaseConnector;
import me.ale.commons.core.permission.PermissionManager;
import me.ale.commons.core.permission.listener.PermissionListener;

public class BukkitMain extends CorePlugin {
	
	public static BukkitMain getPlugin() {
		return BukkitMain.getPlugin(BukkitMain.class);
	}

	@Getter
	private DatabaseConnector databaseConnector;
	
	@Getter
	private StatsManager statsManager;
	
	@Getter
	private RankManager rankManager;
	
	@Getter
	private PackColletionManager packManager;
	
	@Getter
	private FactionManager factionManager;
	
	@Override
	public void run() {
		databaseConnector = new DatabaseConnector();
		try {
			databaseConnector.open();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		statsManager = new StatsManager();
		rankManager = new RankManager();
		packManager = new PackColletionManager();
		factionManager = new FactionManager();
		factionManager.setup();
		new PermissionManager().run();
		new UpdateScheduler().run();
		
		new CommandLoader().loadCommandsFromPackage("me.ale.commons.bukkit.command");
		
		System.out.println("CyonCommons ativado.");
		
		getServer().getScheduler().runTaskTimer(this, new UpdateScheduler(), 1, 1);
		
		PluginManager pm = Bukkit.getPluginManager();
		
		pm.registerEvents(new PlayerListener(), this);
		pm.registerEvents(new MenuListener(), this);
		pm.registerEvents(new PermissionListener(), this);
	}

	@Override
	public void stop() {
		
	}

}
