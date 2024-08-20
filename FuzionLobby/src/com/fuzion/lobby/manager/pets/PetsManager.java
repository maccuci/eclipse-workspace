package com.fuzion.lobby.manager.pets;

import java.util.HashMap;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

public class PetsManager {
	
	public HashMap<Player, Pet> mapPet = new HashMap<>();
	
	public void addPet(Player player, String name, EntityType type) {
		Pet pet = new Pet(name, type);
		mapPet.put(player, pet);
		if(type == EntityType.WOLF) {
			Wolf wolf = (Wolf)player.getWorld().spawnEntity(player.getLocation(), type);
			wolf.setBaby();
			wolf.setCustomName(name);
			wolf.setCustomNameVisible(true);
			wolf.setOwner(player);
		}
	}
	
	public void removePet(Player player, EntityType type) {
		if(!mapPet.containsKey(player))
			return;
		if(type == EntityType.WOLF) {
			Wolf wolf = (Wolf)getPet(player);
			wolf.remove();
		}
	}
	
	public Pet getPet(Player player) {
		return mapPet.get(player);
	}
}
