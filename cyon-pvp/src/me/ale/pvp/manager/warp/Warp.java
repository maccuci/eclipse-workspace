package me.ale.pvp.manager.warp;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import lombok.Getter;

@Getter
public enum Warp {
	
	//edit locations TODO
	
	FFA(new Location(Bukkit.getWorld("world"), 10, 100, 10)),
	CHALLENGES(new Location(Bukkit.getWorld("world"), 20, 100, 20)),
	CHALLENGES_SOLO(new Location(Bukkit.getWorld("world"), 30, 100, 30)),
	MARKET(Bukkit.getWorld("world").getSpawnLocation()),
	CLASSIC(new Location(Bukkit.getWorld("world"), 50, 100, 50));
	
	private Location location;
	
	private Warp(Location location) {
		this.location = location;
	}
}
