package me.feenks.core.bukkit.injector.packet.out;

import me.feenks.core.bukkit.injector.packet.PacketHandler;

public class PacketOutPermissionSet extends PacketHandler {
	
	public PacketOutPermissionSet() {
		super("PermissionSet");
	}

	@Override
	public boolean process() {
		
		return false;
	}

}
