package com.fuzion.hg.manager.game;

import com.fuzion.hg.Main;

import lombok.Getter;

@Getter
public enum GameStage {
	
	WAITING(Main.PREGAME_TIMER),
	INVINCIBILITY(Main.INVINCIBILITY_TIMER),
	GAME(Main.GAME_TIMER);
	
	private int defaultTimer;
	
	private GameStage(int defaultTimer) {
		this.defaultTimer = defaultTimer;
	}

}
