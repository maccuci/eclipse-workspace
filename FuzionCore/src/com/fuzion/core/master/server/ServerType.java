package com.fuzion.core.master.server;

public enum ServerType {
	
	PVP_SIMULATOR,
	HUNGERGAMER,
	POT_PVP,
	LOBBY;
	
	public boolean isLobby() {
		return this == LOBBY;
	}

}
