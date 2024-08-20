package com.fuzion.core.master.server.loadbalancer.server;

import java.util.Set;
import java.util.UUID;

import com.fuzion.core.master.server.ServerType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ServerGame extends FuzionServer {

	private int time;
    private String map;
    private ServerState state;

    public ServerGame(String serverId, ServerType type, Set<UUID> onlinePlayers, int maxPlayers, boolean joinEnabled) {
        super(serverId, type, onlinePlayers, 100, joinEnabled);
        this.state = ServerState.WAITING;
    }

    @Override
    public int getActualNumber() {
        return super.getActualNumber();
    }

    public abstract boolean isInProgress();

}
