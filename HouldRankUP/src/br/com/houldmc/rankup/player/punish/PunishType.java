package br.com.houldmc.rankup.player.punish;

import lombok.Getter;

public enum PunishType {
	
	BAN(1),
	MUTE(2),
	TEMPBAN(3),
	TEMPMUTE(4);
	
	@Getter
	private final int id;
	
	private PunishType(int id) {
		this.id = id;
	}
	
	public static PunishType getType(int id) {
		PunishType type = null;
		for(PunishType types : values())
			if(types.getId() == id)
				type = types;
		return type;
	}
	
	public static PunishType getType(String name) {
		PunishType type = null;
		for(PunishType types : values())
			if(types.name().toLowerCase().equalsIgnoreCase(name))
				type = types;
		return type;
	}
}
