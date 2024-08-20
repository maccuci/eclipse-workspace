package me.ale.commons.api.admin;

import org.bukkit.GameMode;

import lombok.Getter;

@Getter
public enum AdminMode {
	
	PLAYER(GameMode.SURVIVAL),
	ADMIN(GameMode.CREATIVE);
	
	private GameMode gameMode;
	
	private AdminMode(GameMode gameMode) {
		this.gameMode = gameMode;
	}

}
