package me.feenks.core.bukkit.injector.packet.out;

import me.feenks.core.bukkit.injector.packet.PacketHandler;

public class PacketOutPermissionRemove extends PacketHandler {
	
	public PacketOutPermissionRemove() {
		super("PermissionRemove");
	}

	@Override
	public boolean process() {
		
		return false;
	}

}
