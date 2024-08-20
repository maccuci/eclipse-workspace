package me.feenks.core.bukkit.injector.packet.out;

import me.feenks.core.bukkit.injector.packet.PacketHandler;

public class PacketOutUpdateRank extends PacketHandler {

	public PacketOutUpdateRank() {
		super("RankUpdate");
	}

	@Override
	public boolean process() {
		
		return false;
	}

}
