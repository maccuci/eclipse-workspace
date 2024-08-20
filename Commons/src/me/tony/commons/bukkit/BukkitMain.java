package me.tony.commons.bukkit;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import lombok.Getter;
import me.tony.commons.bukkit.api.menu.MenuListener;
import me.tony.commons.bukkit.commands.CommandLoader;
import me.tony.commons.bukkit.event.UpdateScheduler;
import me.tony.commons.bukkit.listener.AccountListener;
import me.tony.commons.bukkit.listener.AntiAfkListener;
import me.tony.commons.core.Commons;
import me.tony.commons.core.CommonsPlatform;

public class BukkitMain extends JavaPlugin {

	public static BukkitMain getPlugin() {
		return BukkitMain.getPlugin(BukkitMain.class);
	}

	CommonsPlatform platform = new BukkitPlatform();

	@Getter
	private ProtocolManager protocolManager;

	@Override
	public void onLoad() {
		protocolManager = ProtocolLibrary.getProtocolManager();
	}

	@Override
	public void onEnable() {
		platform.setClassInicializate(BukkitMain.class);
		new UpdateScheduler().run();

		if (Commons.getMySQL().openConnection()) {
			System.out.println("The primary and secondary connections have been established.");
		} else {
			System.out.println("Error the mysql connections have not been established.");
			return;
		}
		
		CommandLoader.loadCommandsFromPackage("me.tony.commons.bukkit.commands.registry");

		Commons.getTagCommon().loadTags();
		Commons.getGroupCommon().loadGroups();

		getServer().getPluginManager().registerEvents(new AccountListener(), this);
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		getServer().getPluginManager().registerEvents(new AntiAfkListener(), this);

		getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		getLogger().log(new LogRecord(Level.INFO, "Commons was actived."));
	}
}
