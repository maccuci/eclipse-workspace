package me.feenks.core.bukkit.injector.packet;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import me.feenks.core.DohanAPI;

public class PacketManagement {

	private static final Map<PacketHandler, String> packets = new HashMap<>();

	public boolean readPacket(PacketHandler packetHandler) {
		long start = System.currentTimeMillis();
		
		if(!hasPacket(packetHandler))
			return true;
		DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] The packet '" + packetHandler.getPacketName() + "' was read.");
		return packetHandler.process();
	}

	public void addPacket(PacketHandler packetHandler) {
		long start = System.currentTimeMillis();
		
		if(hasPacket(packetHandler)) {
			DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] This packet already exist. Try changing the name of this new package.");
			return;
		}

		packets.put(packetHandler, packetHandler.getPacketName());

		DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] New packet was added. Name: '"
				+ packetHandler.getPacketName() + "'.");
	}

	public void removePacket(PacketHandler packetHandler) {
		long start = System.currentTimeMillis();
		
		if(!hasPacket(packetHandler)) {
			DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] This package was not found in the list.");
			return;
		}
		
		packets.remove(packetHandler, packetHandler.getPacketName());
		DohanAPI.log(Level.INFO, "[" + (System.currentTimeMillis() - start) + "ms] This package was removed.");
	}

	public boolean hasPacket(PacketHandler packetHandler) {
		return packets.containsKey(packetHandler);
	}
}
