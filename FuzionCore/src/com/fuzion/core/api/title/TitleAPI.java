package com.fuzion.core.api.title;

import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import org.spigotmc.*;
import net.minecraft.server.v1_7_R4.*;
import org.bukkit.*;

public class TitleAPI {
	
	private String title;
    private ChatColor titleColor;
    private String subtitle;
    private ChatColor subtitleColor;
    private int fadeInTime;
    private int stayTime;
    private int fadeOutTime;
    private boolean ticks;
    
    public TitleAPI(final String title) {
        this.title = "";
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.title = title;
    }
    
    public TitleAPI(final String title, final String subtitle) {
        this.title = "";
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.title = title;
        this.subtitle = subtitle;
    }
    
    public TitleAPI(final TitleAPI title) {
        this.title = "";
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.title = title.title;
        this.subtitle = title.subtitle;
        this.titleColor = title.titleColor;
        this.subtitleColor = title.subtitleColor;
        this.fadeInTime = title.fadeInTime;
        this.fadeOutTime = title.fadeOutTime;
        this.stayTime = title.stayTime;
        this.ticks = title.ticks;
    }
    
    public TitleAPI(final String title, final String subtitle, final int fadeInTime, final int stayTime, final int fadeOutTime) {
        this.title = "";
        this.titleColor = ChatColor.WHITE;
        this.subtitle = "";
        this.subtitleColor = ChatColor.WHITE;
        this.fadeInTime = -1;
        this.stayTime = -1;
        this.fadeOutTime = -1;
        this.ticks = false;
        this.title = title;
        this.subtitle = subtitle;
        this.fadeInTime = fadeInTime;
        this.stayTime = stayTime;
        this.fadeOutTime = fadeOutTime;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public void setSubtitle(final String subtitle) {
        this.subtitle = subtitle;
    }
    
    public String getSubtitle() {
        return this.subtitle;
    }
    
    public void setTitleColor(final ChatColor color) {
        this.titleColor = color;
    }
    
    public void setSubtitleColor(final ChatColor color) {
        this.subtitleColor = color;
    }
    
    public void setFadeInTime(final int time) {
        this.fadeInTime = time;
    }
    
    public void setFadeOutTime(final int time) {
        this.fadeOutTime = time;
    }
    
    public void setStayTime(final int time) {
        this.stayTime = time;
    }
    
    public void setTimingsToTicks() {
        this.ticks = true;
    }
    
    public void setTimingsToSeconds() {
        this.ticks = false;
    }
    
    public void send(final Player player) {
        if (!this.isNewVersion(player)) {
            return;
        }
        this.resetTitle(player);
        final EntityPlayer handle = ((CraftPlayer)player).getHandle();
        final PlayerConnection connection = handle.playerConnection;
        ProtocolInjector.PacketTitle packetTitle = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TIMES, this.fadeInTime * (this.ticks ? 1 : 20), this.stayTime * (this.ticks ? 1 : 20), this.fadeOutTime * (this.ticks ? 1 : 20));
        if (this.fadeInTime != -1 && this.fadeOutTime != -1 && this.stayTime != -1) {
            connection.sendPacket((Packet)packetTitle);
        }
        IChatBaseComponent serializer = ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', this.title) + "\",color:" + this.titleColor.name().toLowerCase() + "}");
        packetTitle = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.TITLE, serializer);
        connection.sendPacket((Packet)packetTitle);
        if (this.subtitle != "") {
            serializer = ChatSerializer.a("{text:\"" + ChatColor.translateAlternateColorCodes('&', this.subtitle) + "\",color:" + this.subtitleColor.name().toLowerCase() + "}");
            packetTitle = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.SUBTITLE, serializer);
            connection.sendPacket((Packet)packetTitle);
        }
    }
    
    @SuppressWarnings("deprecation")
	public void broadcast() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            this.send(player);
        }
    }
    
    public void clearTitle(final Player player) {
        if (this.isNewVersion(player)) {
            return;
        }
        final EntityPlayer handle = ((CraftPlayer)player).getHandle();
        final PlayerConnection connection = handle.playerConnection;
        final ProtocolInjector.PacketTitle packetTitle = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.CLEAR);
        connection.sendPacket((Packet)packetTitle);
    }
    
    public void resetTitle(final Player player) {
        if (this.isNewVersion(player)) {
            return;
        }
        final EntityPlayer handle = ((CraftPlayer)player).getHandle();
        final PlayerConnection connection = handle.playerConnection;
        final ProtocolInjector.PacketTitle packetTitle = new ProtocolInjector.PacketTitle(ProtocolInjector.PacketTitle.Action.RESET);
        connection.sendPacket((Packet)packetTitle);
    }
    
    public boolean isNewVersion(final Player player) {
        return ((CraftPlayer)player).getHandle().playerConnection.networkManager.getVersion() >= 47;
    }

}
