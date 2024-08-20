package me.tony.commons.api.actionbar;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

import net.minecraft.server.v1_7_R4.ChatSerializer;
import net.minecraft.server.v1_7_R4.IChatBaseComponent;
import net.minecraft.server.v1_7_R4.PacketPlayOutChat;

public class ActionBarAPI {
	
	public static void send(Player player, String text) {
		IChatBaseComponent chatBaseComponent = ChatSerializer.a(text);
		 PacketPlayOutChat packet = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
		 ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
	}
	
	@SuppressWarnings("deprecation")
	public static void broadcast(String text) {
		for(Player player : Bukkit.getOnlinePlayers()) {
			send(player, text);
		}
	}
}
