package me.tony.bedwars.manager.team.list;

import org.bukkit.Color;

import lombok.Getter;

@Getter
public enum TeamList {
	
	RED("Vermelho", "§c§lV", Color.RED),
	BLUE("Azul", "§9§lB", Color.BLUE),
	YELLOW("Amarelo", "§e§lA", Color.YELLOW),
	GREEN("Verde", "§a§lV", Color.GREEN);
	
	private String name, tag;
	private Color color;
	
	private TeamList(String name, String tag, Color color) {
		this.name = name;
		this.tag = tag;
		this.color = color;
	}
}
