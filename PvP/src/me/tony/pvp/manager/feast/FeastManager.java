package me.tony.pvp.manager.feast;

import org.bukkit.Location;

import me.tony.pvp.manager.feast.structure.FeastChestStructure;

public class FeastManager {
	
	private FeastChestStructure chestStructure;
	private Location feastLocation;
	private boolean chestSpawned;
	private int counter = 300;

	
	public FeastManager() {
		chestStructure = new FeastChestStructure();
		feastLocation = feastLocation.clone().add(10, 0, 10);
	}

	public void spawnChests() {
		if (chestSpawned)
			return;
		chestSpawned = true;
		chestStructure.place(feastLocation);
	}
	
	public int getCounter() {
		return counter;
	}

	public boolean count() {
		return --counter <= 0;
	}
	
	public int restartCounter() {
		return counter = 300;
	}

	public boolean isChestSpawned() {
		return chestSpawned;
	}

	public Location getFeastLocation() {
		return feastLocation;
	}
}
