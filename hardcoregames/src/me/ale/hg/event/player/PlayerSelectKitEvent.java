package me.ale.hg.event.player;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.ale.hg.event.PlayerCancellableEvent;
import me.ale.hg.manager.kit.Kit;

@Getter
public class PlayerSelectKitEvent extends PlayerCancellableEvent {
	
	private Kit kit;

	public PlayerSelectKitEvent(Player player, Kit kit) {
		super(player);
		this.kit = kit;
	}
	
	

}
