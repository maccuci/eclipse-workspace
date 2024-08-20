package me.ale.hg.manager.game;

import lombok.Getter;

@Getter
public enum GameType {
	
	WAITING(300), //
	INVINCIBILITY(120), //
	GAMETIME(INVINCIBILITY.getTimer()); //
	
	private int timer;
	
	private GameType(int timer) {
		this.timer = timer;
	}

}
