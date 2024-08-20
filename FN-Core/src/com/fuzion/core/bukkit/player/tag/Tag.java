package com.fuzion.core.bukkit.player.tag;

import com.fuzion.core.bukkit.player.group.Group;

import lombok.Getter;

@Getter
public enum Tag {
	
	DONO("§4§lDONO §7", "§4§l", Group.DONO),
	DIRETOR("§4§lDIRETOR §7", "§4§l", Group.DIRETOR),
	DEVELOPER("§3§lDEVELOPER §7", "§3§l", Group.DEVELOPER),
	ADMIN("§4c§lADMIN §7", "§c§l", Group.ADMIN),
	MODPLUS("§5§lMOD+ §7", "§5§l", Group.MODPLUS),
	MODGC("§4§lMODGC §7", "§5§l", Group.MODGC),
	MOD("§4§lMOD §7", "§5§l", Group.MOD),
	TRIAL("§4§lTRIAL §7", "§5§l", Group.TRIAL),
	YOUTUBERPLUS("§3§lYT+ §7", "§3§l", Group.YOUTUBERPLUS),
	AJUDANTE("§9§lHELPER §7", "§9§l", Group.AJUDANTE),
	BUILDER("§e§lBUILDER §7", "§e§l", Group.BUILDER),
	YOUTUBER("§b§lYT §7", "§b§l", Group.YOUTUBER),
	BETA("§1§lBETA §7", "§1§l", Group.BETA),
	ULTRA("§d§lULTRA §7", "§d§l", Group.ULTRA),
	PREMIUM("§6§lPREMIUM §7", "§6§l", Group.PREMIUM),
	ALPHA("§a§lALPHA §7", "§a§l", Group.ALPHA),
	MEMBRO("§7", "§7§l", Group.MEMBRO);
	
	private String prefix, color;
	private Group group;
	
	private Tag(String prefix, String color, Group group) {
		this.prefix = prefix;
		this.color = color;
		this.group = group;
	}

}
