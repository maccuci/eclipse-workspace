package com.fuzion.lobby.manager.pets;

import org.bukkit.entity.EntityType;

import lombok.Getter;

@Getter
public class Pet {
	
	private String name;
	private EntityType type;
	
	public Pet(String name, EntityType type) {
		this.name = name;
		this.type = type;
	}

}
