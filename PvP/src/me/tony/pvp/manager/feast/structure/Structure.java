package me.tony.pvp.manager.feast.structure;

import org.bukkit.Location;

public interface Structure {
	
	public Location findPlace();

	public void place(Location loc);

}
