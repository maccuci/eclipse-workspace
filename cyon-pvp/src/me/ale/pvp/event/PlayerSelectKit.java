package me.ale.pvp.event;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.ale.commons.bukkit.event.PlayerCancellableEvent;
import me.ale.pvp.manager.kit.Kit;

@Getter
public class PlayerSelectKit extends PlayerCancellableEvent {
	
	private Kit kit;

	public PlayerSelectKit(Player player, Kit kit) {
		super(player);
		this.kit = kit;
	}

}
