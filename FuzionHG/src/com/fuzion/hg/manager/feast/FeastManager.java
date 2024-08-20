package com.fuzion.hg.manager.feast;

import org.bukkit.Location;

import com.fuzion.hg.structure.FeastChestStructure;
import com.fuzion.hg.structure.FeastStructure;

public class FeastManager {
	
	private FeastStructure feastStructure;
	private FeastChestStructure chestStructure;
	private Location feastLocation;
	private boolean spawned;
	private boolean chestSpawned;
	private int counter = 300;

	public FeastManager() {
		feastStructure = new FeastStructure();
		chestStructure = new FeastChestStructure();
	}

	public void spawnFeast() {
		if (spawned)
			return;
		feastLocation = feastStructure.findPlace();
		feastStructure.place(feastLocation);
		spawned = true;
	}

	public void spawnChests() {
		if (chestSpawned)
			return;
		chestSpawned = true;
		chestStructure.place(feastLocation);
	}

	public void spawnBonusFeast() {
		FeastStructure feastStructure = new FeastStructure(20, 500);
		Location loc = feastStructure.findPlace();
		feastStructure.place(loc);
		chestStructure.place(loc);
	}

	public int getCounter() {
		return counter;
	}

	public boolean count() {
		return --counter <= 0;
	}

	public boolean isChestSpawned() {
		return chestSpawned;
	}

	public boolean isSpawned() {
		return spawned;
	}

	public Location getFeastLocation() {
		return feastLocation;
	}

}
