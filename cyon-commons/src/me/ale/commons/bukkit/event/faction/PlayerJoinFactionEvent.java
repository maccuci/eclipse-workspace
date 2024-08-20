package me.ale.commons.bukkit.event.faction;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.ale.commons.bukkit.event.PlayerCancellableEvent;

@Getter
public class PlayerJoinFactionEvent extends PlayerCancellableEvent {
	
	private String factionName, message;

	public PlayerJoinFactionEvent(Player player, String factionName, String message) {
		super(player);
		this.factionName = factionName;
		this.message = message;
	}
}
