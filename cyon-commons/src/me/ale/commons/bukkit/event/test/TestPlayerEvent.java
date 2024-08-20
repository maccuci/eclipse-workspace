package me.ale.commons.bukkit.event.test;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.ale.commons.bukkit.event.PlayerCancellableEvent;

@Getter
public class TestPlayerEvent extends PlayerCancellableEvent {
	
	private String message;

	public TestPlayerEvent(Player player, String message) {
		super(player);
		this.message = message;
	}
}
