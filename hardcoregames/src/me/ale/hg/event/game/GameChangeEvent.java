package me.ale.hg.event.game;

import lombok.Getter;
import me.ale.hg.event.Event;
import me.ale.hg.manager.game.GameType;

@Getter
public class GameChangeEvent extends Event {
	
	private GameType oldStage, newStage;
	
	public GameChangeEvent(GameType oldStage, GameType newStage) {
		this.oldStage = oldStage;
		this.newStage = newStage;
	}
}
