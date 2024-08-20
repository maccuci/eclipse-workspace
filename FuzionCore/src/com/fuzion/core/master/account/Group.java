package com.fuzion.core.master.account;

import lombok.Getter;

@Getter
public enum Group {
	
	DONO(0, "§4§l"),
	DIRETOR(1, "§4§l"),
	DEVELOPER(2, "§3§l"),
	ADMIN(3, "§c§l"),
	GERENTE(4, "§c§l"),
	MODPLUS(5, "§5§l"),
	MODGC(6, "§5§l"),
	MOD(7, "§5§l"),
	TRIAL(8, "§5§l"),
	BUILDER(9, "§e§l"),
	HELPER(10, "§9§l"),
	YOUTUBERPLUS(11, "§3§l"),
	YOUTUBER(12, "§b§l"),
	BETA(13, "§1§l"),
	ULTRA(14, "§d§l"),
	PREMIUM(15, "§6§l"),
	ALPHA(16, "§a§l"),
	NORMAL(17, "§7");
	
	private int id;
	private String color;
	private String[] permissions;
	
	private Group(int id, String color) {
		this.id = id;
		this.color = color;
		permissions = new String[] {"*"};
	}
}
