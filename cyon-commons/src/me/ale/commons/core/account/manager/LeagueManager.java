package me.ale.commons.core.account.manager;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ale.commons.bukkit.event.league.PlayerLeagueChangeEvent;
import me.ale.commons.core.account.League;

public class LeagueManager {
	
	private StatsManager manager;
	
	public LeagueManager(StatsManager manager) {
		this.manager = manager;
	}
	
	public void setLeague(UUID uuid, League league) {
		PlayerLeagueChangeEvent event = new PlayerLeagueChangeEvent(Bukkit.getPlayer(uuid), getLeague(uuid), league);
		Bukkit.getPluginManager().callEvent(event);
		manager.set(uuid, "league", league);
	}
	
	public League getLeague(UUID uuid) {
		try {
			return League.valueOf(manager.getString(uuid, "league"));
		} catch(Exception exception) {
			exception.printStackTrace();
			Player player = Bukkit.getPlayer(uuid);
			if(player.isOnline()) {
				player.kickPlayer("");
			}
			return League.APPRENTICE;
		}
	}
}
