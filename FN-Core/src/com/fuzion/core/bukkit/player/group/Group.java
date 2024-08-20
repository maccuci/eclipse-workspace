package com.fuzion.core.bukkit.player.group;

import com.fuzion.core.bukkit.player.tag.Tag;

import lombok.Getter;

@Getter
public enum Group {
	
	DONO("§4§lDONO §7", "§4§l", Tag.DONO),
	DIRETOR("§4§lDIRETOR §7", "§4§l", Tag.DIRETOR),
	DEVELOPER("§3§lDEVELOPER §7", "§3§l", Tag.DEVELOPER),
	ADMIN("§4c§lADMIN §7", "§c§l", Tag.ADMIN),
	MODPLUS("§5§lMOD+ §7", "§5§l", Tag.MODPLUS),
	MODGC("§4§lMODGC §7", "§5§l", Tag.MODGC),
	MOD("§4§lMOD §7", "§5§l", Tag.MOD),
	TRIAL("§4§lTRIAL §7", "§5§l", Tag.TRIAL),
	YOUTUBERPLUS("§3§lYT+ §7", "§3§l", Tag.YOUTUBERPLUS),
	AJUDANTE("§9§lHELPER §7", "§9§l", Tag.AJUDANTE),
	BUILDER("§e§lBUILDER §7", "§e§l", Tag.BUILDER),
	YOUTUBER("§b§lYT §7", "§b§l", Tag.YOUTUBER),
	BETA("§1§lBETA §7", "§1§l", Tag.BETA),
	ULTRA("§d§lULTRA §7", "§d§l", Tag.ULTRA),
	PREMIUM("§6§lPREMIUM §7", "§6§l", Tag.PREMIUM),
	ALPHA("§a§lALPHA §7", "§a§l", Tag.ALPHA),
	MEMBRO("§7", "§7§l", Tag.MEMBRO);
	
	private String prefix, color;
	private Tag tag;
	
	private Group(String prefix, String color, Tag tag) {
		this.prefix = prefix;
		this.color = color;
		this.tag = tag;
	}
}
