package me.ale.commons.core.account;

import lombok.Getter;

@Getter
public enum League {
	
	APPRENTICE("§f", 0),
	SENSEI("§4", 3000);
	
	private String color;
	private int exp;

	private League(String color, int exp) {
		this.color = color;
		this.exp = exp;
	}

}
