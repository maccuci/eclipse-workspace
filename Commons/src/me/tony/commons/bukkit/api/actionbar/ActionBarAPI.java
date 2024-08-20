package me.tony.commons.bukkit.api.actionbar;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import me.tony.commons.bukkit.BukkitMain;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

public class ActionBarAPI {

    public static void send(Player player, String text) {
        PacketContainer chatPacket = new PacketContainer(PacketType.Play.Server.CHAT);
        chatPacket.getChatComponents().write(0, WrappedChatComponent.fromJson("{\"text\":\"" + text + " \"}"));
        chatPacket.getBytes().write(0, (byte) 2);
        try {
            BukkitMain.getPlugin().getProtocolManager().sendServerPacket(player, chatPacket);
        } catch (InvocationTargetException e) {
            throw new RuntimeException("Cannot send packet " + chatPacket, e);
        }
    }

    public static void broadcast(String text) {
        Bukkit.getOnlinePlayers().forEach(p -> send(p, text));
    }

}
