package com.vexy.thepit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.vexy.thepit.listener.PlayerListener;
import com.vexy.thepit.mysql.Mysql;
import com.vexy.thepit.player.PitPlayerManager;
import com.vexy.thepit.player.scoreboard.PlayerScoreboard;
import com.vexy.thepit.player.scoreboard.Scroller;

import lombok.Getter;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	
	@Getter private Mysql mysql;
	
	@Getter private PitPlayerManager pitPlayerManager;
	
	@Override
	public void onEnable() {
		mysql = new Mysql("localhost", "3306", "thepit", "root", "");
		mysql.start();
		mysql.createTables();
		pitPlayerManager = new PitPlayerManager();
		
		Scroller.run();
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			new PlayerScoreboard().updateTitle(p);
		}
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
	}
}
