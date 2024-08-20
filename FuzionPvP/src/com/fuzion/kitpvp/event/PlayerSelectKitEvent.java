package com.fuzion.kitpvp.event;

import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.event.PlayerCancellableEvent;
import com.fuzion.kitpvp.manager.kit.Kit;

import lombok.Getter;

@Getter
public class PlayerSelectKitEvent extends PlayerCancellableEvent {
	
	private Kit kit;

	public PlayerSelectKitEvent(Player player, Kit kit) {
		super(player);
		this.kit = kit;
	}
	
	

}
