package com.hother.core.bukkit.player;

import java.util.UUID;

import lombok.Getter;

@Getter
public class Gamer {
	
	private UUID uuid;
	
	private int kills, mostkills, streak;
	
	public Gamer(UUID uuid) {
		this.uuid = uuid;
	}
	
	public void addKill() {
		if(kills > 1) {
			kills = 1;
			return;
		}
		int kills = this.kills + 1;
		setKills(kills);
	}
	
	public void addMostKill() {
		if(kills > 1) {
			kills = 1;
			return;
		}
		int kills = this.kills + 1;
		setKills(kills);
	}
	
	public void addStreak() {
		this.streak = streak++;
	}
	
	public void setKills(int kills) {
		this.kills = kills;
	}
}
