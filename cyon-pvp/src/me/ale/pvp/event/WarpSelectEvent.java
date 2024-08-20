package me.ale.pvp.event;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.ale.commons.bukkit.event.PlayerCancellableEvent;
import me.ale.pvp.manager.warp.Warp;

@Getter
public class WarpSelectEvent extends PlayerCancellableEvent {
	
	private Warp warp;

	public WarpSelectEvent(Player player, Warp warp) {
		super(player);
		this.warp = warp;
	}

}
