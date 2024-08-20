package com.fuzion.kitpvp.manager.warp;

import org.bukkit.Location;

public class Coords {
	
	private Location location;
	
	public Coords(Location location) {
		this.location = location;
	}
	
	public double getX() {
		return location.getX();
	}
	
	public double getY() {
		return location.getY();
	}
	
	public double getZ() {
		return location.getZ();
	}
	
	public double getPitch() {
		return location.getPitch();
	}
	
	public double getYaw() {
		return location.getYaw();
	}
}
