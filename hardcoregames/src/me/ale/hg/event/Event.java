package me.ale.hg.event;

import org.bukkit.event.HandlerList;

public abstract class Event extends org.bukkit.event.Event {
	
	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
