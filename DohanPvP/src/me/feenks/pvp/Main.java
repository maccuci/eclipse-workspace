package me.feenks.pvp;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import me.feenks.core.DohanAPI;
import me.feenks.core.bukkit.api.scoreboard.CustomScoreboard;
import me.feenks.pvp.listeners.KnockbackListener;
import me.feenks.pvp.listeners.PlayerListener;
import me.feenks.pvp.manager.ability.AbilityManager;

public class Main extends JavaPlugin {
	
	public static Main getPlugin() {
		return Main.getPlugin(Main.class);
	}
	
	private CustomScoreboard customScoreboard;
	@Getter private String craftBukkitVersion;
	@Getter private double horMultiplier = 1D;
	@Getter private double verMultiplier = 1D;
	
	@Override
	public void onEnable() {
		DohanAPI.debug("PvP plugin has been inicializade.");
		customScoreboard = new CustomScoreboard();
		
		this.craftBukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
		
		AbilityManager.initializateAbilities("me.feenks.pvp.abilities");
		
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new KnockbackListener(), this);
	}
	
	public CustomScoreboard getCustomScoreboard() {
		return customScoreboard;
	}
}
