package br.com.houldmc.rankup;

import org.bukkit.plugin.java.JavaPlugin;

import br.com.houldmc.rankup.manager.Manager;
import br.com.houldmc.rankup.player.clan.ClanManager;
import lombok.Getter;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	@Getter private Manager manager;
	
	@Override
	public void onEnable() {
		manager = new Manager();
		new ClanManager().setup();
		System.out.println("RankUP was loaded.");
	}
	
	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
	}
}
