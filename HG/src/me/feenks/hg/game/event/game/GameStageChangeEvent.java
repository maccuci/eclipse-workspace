package me.feenks.hg.game.event.game;

import me.feenks.hg.game.event.CustomEvent;
import me.feenks.hg.game.stage.GameStage;

public class GameStageChangeEvent extends CustomEvent {
	
	private GameStage lastStage;
	private GameStage newStage;

	public GameStageChangeEvent(GameStage lastStage, GameStage newStage) {
		this.lastStage = lastStage;
		this.newStage = newStage;
	}

	public GameStage getNewStage() {
		return newStage;
	}

	public GameStage getLastStage() {
		return lastStage;
	}

}
