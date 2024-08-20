package me.tony.commons.bukkit.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class SchedulerEvent extends Event {

	public static final HandlerList handlers = new HandlerList();
	private SchedulerType type;
	private long currentTick;

	public SchedulerEvent(SchedulerType type) {
		this(type, -1);
	}
	
	public SchedulerEvent(SchedulerType type, long currentTick) {
		this.type = type;
		this.currentTick = currentTick;
	}

	public SchedulerType getType() {
		return type;
	}

	public long getCurrentTick() {
		return currentTick;
	}
	
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public static enum SchedulerType {
		TICK, SECOND, MINUTE;
	}
}
