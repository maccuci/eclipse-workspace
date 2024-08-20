package me.tony.bedwars.manager.team;

import org.bukkit.Color;

import lombok.Getter;

@Getter
public class Team {
	
	private String name, tag;
	private Color color;
	
	public Team(String name, String tag, Color color) {
		this.name = name;
		this.tag = tag;
		this.color = color;
	}
}
