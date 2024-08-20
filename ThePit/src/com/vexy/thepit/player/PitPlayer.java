package com.vexy.thepit.player;

import java.util.UUID;

import com.vexy.thepit.player.scoreboard.PlayerScoreboard;

import lombok.Data;

@Data
public class PitPlayer {
	
	private UUID uuid;
	
	private int id;
	
	//stats
	private int xp, level, prestige;
	private double gold;
	
	private PlayerScoreboard plScoreboard;

}
