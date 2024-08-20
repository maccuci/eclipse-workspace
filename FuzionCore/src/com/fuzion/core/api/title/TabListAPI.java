package com.fuzion.core.api.title;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.craftbukkit.v1_7_R4.entity.*;
import org.spigotmc.*;
import net.minecraft.server.v1_7_R4.*;

public class TabListAPI {
	
    public static void broadcastHeader(final String header) {
        broadcastHeaderAndFooter(header, null);
    }
    
    public static void broadcastFooter(final String footer) {
        broadcastHeaderAndFooter(null, footer);
    }
    
    @SuppressWarnings("deprecation")
	public static void broadcastHeaderAndFooter(final String header, final String footer) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            setHeaderAndFooter(player, header, footer);
        }
    }
    
    public static void setHeader(final Player p, final String header) {
        setHeaderAndFooter(p, header, null);
    }
    
    public static void setFooter(final Player p, final String footer) {
        setHeaderAndFooter(p, null, footer);
    }
    
    public static void setHeaderAndFooter(final Player p, final String rawHeader, final String rawFooter) {
        if (((CraftPlayer)p).getHandle().playerConnection.networkManager.getVersion() < 47) {
            return;
        }
        final CraftPlayer player = (CraftPlayer)p;
        final IChatBaseComponent header = ChatSerializer.a(TextConverter.convert(rawHeader));
        final IChatBaseComponent footer = ChatSerializer.a(TextConverter.convert(rawFooter));
        final ProtocolInjector.PacketTabHeader packet = new ProtocolInjector.PacketTabHeader(header, footer);
        player.getHandle().playerConnection.sendPacket((Packet)packet);
    }
    
    private static class TextConverter
    {
        public static String convert(final String text) {
            if (text == null || text.length() == 0) {
                return "\"\"";
            }
            final int len = text.length();
            final StringBuilder sb = new StringBuilder(len + 4);
            sb.append('\"');
            for (int i = 0; i < len; ++i) {
                final char c = text.charAt(i);
                switch (c) {
                    case '\"':
                    case '\\': {
                        sb.append('\\');
                        sb.append(c);
                        break;
                    }
                    case '/': {
                        sb.append('\\');
                        sb.append(c);
                        break;
                    }
                    case '\b': {
                        sb.append("\\b");
                        break;
                    }
                    case '\t': {
                        sb.append("\\t");
                        break;
                    }
                    case '\n': {
                        sb.append("\\n");
                        break;
                    }
                    case '\f': {
                        sb.append("\\f");
                        break;
                    }
                    case '\r': {
                        sb.append("\\r");
                        break;
                    }
                    default: {
                        if (c < ' ') {
                            final String t = "000" + Integer.toHexString(c);
                            sb.append("\\u").append(t.substring(t.length() - 4));
                            break;
                        }
                        sb.append(c);
                        break;
                    }
                }
            }
            sb.append('\"');
            return sb.toString();
        }
    }

}
