package me.feenks.core.bukkit.injector.packet.out;

import me.feenks.core.bukkit.injector.packet.PacketHandler;

public class PacketOutGroupUpdate extends PacketHandler {
	
	public PacketOutGroupUpdate() {
		super("GroupUpdate");
	}

	@Override
	public boolean process() {
		
		return false;
	}

}
