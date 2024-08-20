package me.ale.commons.core.server;

import java.util.HashMap;

public class ServerController {
	
	private static final HashMap<String, ServerType> serversType = new HashMap<>();
	
	public static ServerType getServerType() {
		for(ServerType serverType : ServerType.values()) {
			String name = serverType.name();
			
			if(serverType == serversType.get(name)) {
				return serverType;
			}
		}
		return null;
	}
	
	public static void setServerType(ServerType type) {
		switch (type) {
		case PVP:
			serversType.put("pvp", ServerType.PVP);
			break;
			
		case HG:
			serversType.put("hg", ServerType.HG);
			break;

		default:
			break;
		}
	}
}
