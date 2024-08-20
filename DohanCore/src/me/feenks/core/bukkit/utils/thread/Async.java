package me.feenks.core.bukkit.utils.thread;

import java.util.logging.Level;

import org.bukkit.Bukkit;

import de.inventivegames.holograms.HologramListeners;
import me.feenks.core.DohanAPI;
import me.feenks.core.bukkit.BukkitMain;
import me.feenks.core.bukkit.event.UpdateScheduler;
import me.feenks.core.bukkit.injector.packet.PacketManagement;
import me.feenks.core.bukkit.injector.packet.in.PacketSystemLoader;
import me.feenks.core.bukkit.injector.packet.out.PacketOutGroupUpdate;
import me.feenks.core.bukkit.injector.packet.out.PacketOutPermissionRemove;
import me.feenks.core.bukkit.injector.packet.out.PacketOutPermissionSet;
import me.feenks.core.bukkit.injector.packet.out.PacketOutUpdateRank;
import me.feenks.core.master.data.DataController;

public class Async extends Thread {
	
	@Override
	public void run() {
		new DataController().mysqlOpenConnection();
		
		new UpdateScheduler().run();
		DohanAPI.log(Level.INFO, "Core Plugin has been initialized.");
		
		Bukkit.getPluginManager().registerEvents(new HologramListeners(), BukkitMain.getPlugin());
		
		PacketManagement packetManagement = new PacketManagement();
		
		packetManagement.readPacket(new PacketSystemLoader());
		packetManagement.readPacket(new PacketOutGroupUpdate());
		packetManagement.readPacket(new PacketOutPermissionRemove());
		packetManagement.readPacket(new PacketOutPermissionSet());
		packetManagement.readPacket(new PacketOutUpdateRank());
		
		//new PermissionManager().loadGroups();
	}

}
