package com.fuzion.party.manager.minegame.constructor;

import lombok.Getter;

@Getter
public abstract class MiniGame {
	
	private String name;
	private String description;
	private int[] pointsWin;
	
	public MiniGame(String name, String description, int... pointsWin) {
		this.name = name;
		this.description = description;
		this.pointsWin = pointsWin;
	}

}
