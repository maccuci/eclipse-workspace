package com.vexy.thepit.player;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

public class PitPlayerManager {
	
	private Map<UUID, PitPlayer> pojo;
	
	public PitPlayerManager() {
		pojo = Maps.newHashMap();
	}
	
	public void add(UUID uuid, PitPlayer tPlayer) {
        pojo.put(uuid, tPlayer);
    }

    public void remove(UUID uuid) {
        if (pojo.containsKey(uuid))
            pojo.remove(uuid);
    }

    public boolean contains(UUID uuid) {
        return pojo.containsKey(uuid);
    }

    public PitPlayer retriever(UUID uuid) {
        return pojo.get(uuid);
    }

    public Map<UUID, PitPlayer> retrieverCache() {
        return pojo;
    }
}
