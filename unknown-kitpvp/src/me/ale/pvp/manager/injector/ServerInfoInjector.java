package me.ale.pvp.manager.injector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import me.ale.pvp.Main;

public class ServerInfoInjector {
	
	public void inject(Main main) {
		ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
		
		protocolManager.addPacketListener(new PacketAdapter(main, ListenerPriority.NORMAL, //
                PacketType.Status.Server.SERVER_INFO) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer container = event.getPacket();
                WrappedServerPing ping = container.getServerPings().read(0);
                ping.setPlayersOnline(ping.getPlayersOnline());
                List<WrappedGameProfile> list = new ArrayList<>();
                list.add(new WrappedGameProfile(UUID.randomUUID(), "§7Estamos com §b" + ping.getPlayersOnline() + " §7jogadores online."));
                ping.setPlayers(list);
                ping.setMotD("§fKitPvP §8» §7Apenas o último fica de pé");
            }
        });
	}

}
