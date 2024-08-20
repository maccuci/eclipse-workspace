package com.fuzion.kitpvp.event;

import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.event.PlayerCancellableEvent;

import lombok.Getter;

@Getter
public class PlayerWarpJoinEvent extends PlayerCancellableEvent {
	
	private String warp;
	
	public PlayerWarpJoinEvent(Player player, String warp) {
		super(player);
		this.warp = warp;
	}

}
