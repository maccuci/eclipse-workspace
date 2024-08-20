package me.ale.commons.bukkit.event.league;

import org.bukkit.entity.Player;

import me.ale.commons.bukkit.event.PlayerCancellableEvent;
import me.ale.commons.core.account.League;

public class PlayerLeagueChangeEvent extends PlayerCancellableEvent {
	
	private League oldLeague;
	private League newLeague;

	public PlayerLeagueChangeEvent(Player player, League oldLeague, League newLeague) {
		super(player);
		this.oldLeague = oldLeague;
		this.newLeague = newLeague;
	}
	
	public League getOldLeague() {
		return oldLeague;
	}

	public League getNewLeague() {
		return newLeague;
	}

}
