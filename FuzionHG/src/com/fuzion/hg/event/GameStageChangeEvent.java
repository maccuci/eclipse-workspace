package com.fuzion.hg.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.fuzion.hg.manager.game.GameStage;

import lombok.Getter;

public class GameStageChangeEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled = false;
	@Getter
	private GameStage oldStage, newStage;
	
	public GameStageChangeEvent(GameStage oldStage, GameStage newStage) {
		this.oldStage = oldStage;
		this.newStage = newStage;
	}


	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.cancelled = cancel;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
