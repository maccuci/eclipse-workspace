package com.fuzion.hg.event;

import org.bukkit.entity.Player;

public class SpectorJoinEvent extends PlayerCancellableEvent {

	public SpectorJoinEvent(Player player) {
		super(player);
	}
}
