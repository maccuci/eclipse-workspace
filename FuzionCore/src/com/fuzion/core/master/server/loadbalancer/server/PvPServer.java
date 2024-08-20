package com.fuzion.core.master.server.loadbalancer.server;

import java.util.Set;
import java.util.UUID;

import com.fuzion.core.master.server.ServerType;

public class PvPServer extends ServerGame {
	
	public PvPServer(String serverId, ServerType type, Set<UUID> onlinePlayers, boolean joinEnabled) {
        super(serverId, type, onlinePlayers, 100, joinEnabled);
    }

    @Override
    public boolean isInProgress() {
        return getState() == ServerState.NONE;
    }
}
