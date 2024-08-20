package me.tony.bedwars.manager;

import org.bukkit.Bukkit;

import lombok.Getter;
import me.tony.bedwars.Main;
import me.tony.bedwars.data.mysql.MySQL;
import me.tony.bedwars.listener.PlayerListener;
import me.tony.bedwars.manager.language.LanguageManager;
import me.tony.bedwars.manager.team.TeamManager;

@Getter
public class Manager {
	
	private Main plugin;
	
	private TeamManager teamManager;
	private MySQL mySQL = new MySQL();
	private LanguageManager languageManager;
	
	public Manager() {
		plugin = Main.getPlugin();
		
		teamManager = new TeamManager();
		languageManager = new LanguageManager();
		
		teamManager.insertDefaultTeams();
		Bukkit.getPluginManager().registerEvents(new PlayerListener(), plugin);
		
		if (mySQL.openConnection()) {
			System.out.println("The primary and secondary connections have been established.");
		} else {
			System.out.println("Error the mysql connections have not been established.");
		}
		languageManager.loadTranslations();
		languageManager.addLanguage(1, "teste", "Esta mensagem foi um teste.");
		languageManager.addLanguage(2, "teste", "This menssage was a test.");
	}
}
