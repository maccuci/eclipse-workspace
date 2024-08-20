package me.feenks.wdr.checks.manager;

import java.lang.reflect.Field;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import me.feenks.core.bukkit.injector.PacketListener;
import me.feenks.core.bukkit.injector.PacketListenerAPI;
import net.minecraft.server.v1_7_R4.EnumEntityUseAction;
import net.minecraft.server.v1_7_R4.Packet;
import net.minecraft.server.v1_7_R4.PacketPlayInUseEntity;

public class ReachcheckInjector {

	private PacketListener protocol;

	public ReachcheckInjector(final Player player, final ReachChecker check, Plugin plugin)
	  {
	    PacketListenerAPI.addListener(this.protocol = new PacketListener()
	    {
	      public void onPacketSend(PacketListener.PacketObject pacote) {}
	      
	      public void onPacketReceive(PacketListener.PacketObject pacote)
	      {
	        Player sender = pacote.getPlayer();
	        Packet packet = pacote.getPacket();
	        if (sender != player) {
	          return;
	        }
	        if ((packet instanceof PacketPlayInUseEntity))
	        {
	          PacketPlayInUseEntity packetplayinuseentity = (PacketPlayInUseEntity)packet;
	          if (packetplayinuseentity.c() == EnumEntityUseAction.ATTACK) {
	            try
	            {
	              Field f = packetplayinuseentity.getClass().getDeclaredField("a");
	              f.setAccessible(true);
	              int id = ((Integer)f.get(packetplayinuseentity)).intValue();
	              check.markAsKilled(Integer.valueOf(id));
	            }
	            catch (NoSuchFieldException|SecurityException|IllegalArgumentException|IllegalAccessException e)
	            {
	              e.printStackTrace();
	            }
	          }
	        }
	      }
	    });
	  }

	public void close() {
		PacketListenerAPI.removeListener(this.protocol);
	}

}
