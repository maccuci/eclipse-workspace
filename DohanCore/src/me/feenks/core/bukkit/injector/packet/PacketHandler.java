package me.feenks.core.bukkit.injector.packet;

import lombok.Getter;

public abstract class PacketHandler {
	
	@Getter
	private String packetName;
	
	@Getter private PacketManagement packetManagement = new PacketManagement();
	
	public PacketHandler(String packetName) {
		this.packetName = packetName;
		getPacketManagement().addPacket(this);
	}
	
	public abstract boolean process();
	
	

}
