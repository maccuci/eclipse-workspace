package me.tony.he4rt.cosmetics;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.tony.he4rt.cosmetics.api.menu.MenuListener;
import me.tony.he4rt.cosmetics.event.UpdateScheduler;
import me.tony.he4rt.cosmetics.list.CosmeticsManager;
import me.tony.he4rt.cosmetics.listener.PlayerListener;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	@Override
	public void onEnable() {
		new UpdateScheduler().run();
		
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(), this);
		
		CosmeticsManager.initializateCosmetics();
		
		ConsoleCommandSender commandSender = getServer().getConsoleSender();
		
		System.out.println("|========================|");
		commandSender.sendMessage("§6Plugin was §eactived.");
		commandSender.sendMessage("§aAuthor: §fTxnyHe4rt");
		System.out.println("|========================|");
	}
}
