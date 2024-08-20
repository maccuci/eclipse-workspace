package com.fuzion.core.master.account;

import lombok.Getter;

@Getter
public enum Tag {
	
	DONO("§4§lDONO§7 ", Group.DONO, "atag"),
	DIRETOR("§4§lDIRETOR§7 ", Group.DIRETOR, "btag"),
	DEVELOPER("§3§lDEVELOPER§7 ", Group.DEVELOPER, "ctag"),
	ADMIN("§c§lADMIN§7 ", Group.ADMIN, "dtag"),
	GERENTE("§c§lGERENTE§7 ", Group.GERENTE, "etag"),
	MODPLUS("§5§lMOD+§7 ", Group.MODPLUS, "ftag"),
	MODGC("§5§lMODGC§7 ", Group.MODGC, "gtag"),
	MOD("§5§lMOD§7 ", Group.MOD, "htag"),
	TRIAL("§5§lTRIAL§7 ", Group.TRIAL, "itag"),
	BUILDER("§e§lBUILDER§7 ", Group.BUILDER, "jtag"),
	HELPER("§9§lHELPER§7 ", Group.HELPER, "ktag"),
	YOUTUBERPLUS("§3§lYT+§7 ", Group.YOUTUBERPLUS, "ltag"),
	YOUTUBER("§b§lYT§7 ", Group.YOUTUBER, "mtag"),
	BETA("§1§lBETA§7 ", Group.BETA, "ntag"),
	//WINNER("§2§lWINNER§2 ", Group.NORMAL, "otag"),
	ULTRA("§d§lULTRA§7 ", Group.ULTRA, "ptag"),
	PREMIUM("§6§lPREMIUM§7 ", Group.PREMIUM, "qtag"),
	ALPHA("§a§lALPHA§7 ", Group.ALPHA, "rtag"),
	NORMAL("§7", Group.NORMAL, "stag");
	
	private String name, teamName;
	private Group group;
	
	private Tag(String name, Group group, String teamName) {
		this.name = name;
		this.group = group;
		this.teamName = teamName;
	}

}
