package me.feenks.core.master.data.permissions.group;

import lombok.Getter;

@Getter
public enum GroupType {
	
	DONO(2, "Dono"),
	ADMIN(3, "Admin"),
	MOD(4, "Mod"),
	AJUDANTEPLUS(5, "AjudantePlus"),
	AJUDANTE(6, "Ajudante"),
	YOUTUBER(7, "Youtuber"),
	MVPPLUS(8, "MvpPlus"),
	MVP(9, "Mvp"),
	VIP(10, "Vip"),
	MEMBRO(11, "Membro");
	
	private String name;
	private int id;
	
	private GroupType(int id, String name) {
		this.name = name;
		this.id = id;
	}

}
