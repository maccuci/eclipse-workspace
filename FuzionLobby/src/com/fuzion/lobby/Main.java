package com.fuzion.lobby;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.fuzion.lobby.listener.PlayerListener;
import com.fuzion.lobby.manager.ScoreboardManager;

import lombok.Getter;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	@Getter
	private ScoreboardManager scoreboardManager;
	
	@Override
	public void onEnable() {
		getLogger().info("Ativado");
		
		scoreboardManager = new ScoreboardManager();
		
		PluginManager pm = Bukkit.getPluginManager();
	    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		pm.registerEvents(new PlayerListener(), this);
	}
}
