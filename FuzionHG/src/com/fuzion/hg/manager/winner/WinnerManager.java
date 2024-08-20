package com.fuzion.hg.manager.winner;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.fuzion.hg.Main;

public class WinnerManager {
	
	private File file;
	private YamlConfiguration configuration;
	
	public WinnerManager() {
		file = new File(Main.getPlugin().getDataFolder() + "/winner.yml");
		configuration = YamlConfiguration.loadConfiguration(file);
	}
	
	public void addWinner(String winnerName) {
		configuration.set("Winner", winnerName);
		try {
			configuration.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getWinner() {
		if(configuration.getString("Winner") == null) {
			return "Nenhum";
		}
		return configuration.getString("Winner");
	}
	
	public boolean hasWinner(Player player) {
		return getWinner().contains(player.getName());
	}
}
