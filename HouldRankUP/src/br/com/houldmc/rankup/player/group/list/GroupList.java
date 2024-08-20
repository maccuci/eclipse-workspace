package br.com.houldmc.rankup.player.group.list;

import lombok.Getter;

@Getter
public enum GroupList {
	
	OWNER("Dono", "§4[Dono] "),
	DIRETOR("Diretor", "§4[Diretor] "),
	MANAGER("Manager", "§c[Gerente] "),
	ADMINISTRATOR("Administrator", "§c[Admin] "),
	MODERATOR("Moderator", "§5[Mod] "),
	AJUDANTE("Ajudante", "§9[Ajudante] "),
	BUILDER("Builder", "§e[Builder] "),
	DESIGNER("Desiger", "§2[Desiger] "),
	YOUTUBERPLUS("YoutuberPlus", "§3[YT+] "),
	YOUTUBER("Youtuber", "§b[YT] "),
	BETA("Beta", "§1[Beta] "),
	HOULD("Hould", "§c[Hould] "),
	PRO("Pro", "§6[Pro] "),
	MVP("MvP", "§9[MvP] "),
	NORMAL("Normal", "§7");
	
	private String name, tag;
	
	private GroupList(String name, String tag) {
		this.name = name;
		this.tag = tag;
	}
}
