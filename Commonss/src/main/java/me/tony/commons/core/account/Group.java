package me.tony.commons.core.account;

import lombok.Getter;

@Getter
public enum Group {
	
	DEVELOPER(1, "Developer", "§4[DEVELOPER] "),
	ADMIN(2, "Admin", "§x[ADMIN] "),
	MODPLUS(3, "ModPlus", "§2[MOD+] "),
	MOD(4, "Mod", "§2[MOD] "),
	HELPER(5, "Helper", "§9[HELPER] "),
	MVPPLUSPLUS(6, "MvpPlusPlus", "§b[MVP§6++§b] "),
	MVPPLUS(7, "MvpPlus", "§4[MVP+] "),
	MVP(8, "Mvp", "§4[MVP] "),
	VIPPLUS(9, "VipPlus", "§a[VIP§6+]§a "),
	VIP(10, "Vip", "§a[VIP] "),
	MEMBER(11, "Member", "§7");
	
	private String name, tag;
	private int id;
	
	private Group(int id, String name, String tag) {
		this.id = id;
		this.name = name;
		this.tag = tag;
	}
}
