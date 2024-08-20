package com.fuzion.core.proxy;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import com.fuzion.core.FuzionAPI;
import com.fuzion.core.proxy.commands.BroadcastCommand;
import com.fuzion.core.proxy.commands.ConnectCommand;
import com.fuzion.core.proxy.commands.LobbyCommand;
import com.fuzion.core.proxy.commands.ReportCommand;
import com.fuzion.core.proxy.listener.ServerListener;
import com.fuzion.core.proxy.manager.mysql.MysqlBackend;
import com.fuzion.core.proxy.manager.socket.ServerInfo;

import lombok.Getter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class ProxyMain extends Plugin {
	
	@Getter
	private static ProxyMain plugin;
	public static ArrayList<ServerInfo> infos;
	
	@Getter
	private MysqlBackend mysqlBackend;
	
	@Override
	public void onEnable() {
		mysqlBackend = new MysqlBackend();
		PluginManager manager = ProxyServer.getInstance().getPluginManager();
		manager.registerCommand(this, new ConnectCommand());
		manager.registerCommand(this, new ReportCommand());
		manager.registerCommand(this, new BroadcastCommand());
		manager.registerCommand(this, new LobbyCommand());
		manager.registerListener(this, new ServerListener());
		getProxy().registerChannel(FuzionAPI.getBungeeChannel());
		
		infos = new ArrayList<ServerInfo>();
		for (Map.Entry<String, net.md_5.bungee.api.config.ServerInfo> entrys : getProxy().getServers().entrySet()) {
			String serverName = (String) entrys.getKey();
			net.md_5.bungee.api.config.ServerInfo info = (net.md_5.bungee.api.config.ServerInfo) entrys.getValue();
			infos.add(new ServerInfo(serverName, info.getAddress()));
		}
	}
	
	public ServerInfo getLobby() {
		ArrayList<ServerInfo> lobbys = new ArrayList<>();
		for (ServerInfo info : infos) {
			if (info.isOnline()) {
				if (info.getName().startsWith("l")) {
					if (!(info.getPlayersOnline() >= info.getMaxPlayers())) {
						lobbys.add(info);
					}
				}
			}
		}
		if (lobbys.size() == 0) {
			return null;
		}
		return lobbys.get(new Random().nextInt(lobbys.size()));
	}
	
	public void addBungee(final String serverHostName, String ipAddress, int port) {
		net.md_5.bungee.api.config.ServerInfo localServerInfo = getProxy().constructServerInfo(serverHostName,
				new InetSocketAddress(ipAddress, port), "Restarting", false);
		if (!serverExists(serverHostName)) {
			System.out.println("Server " + serverHostName + " adicionado ao Bungee.");
			getProxy().getServers().put(serverHostName, localServerInfo);
		} else {
			System.out.println("Servidor \"" + serverHostName + "\" já existe!");
		}
	}
	
	public boolean serverExists(String paramString) {
		return ProxyServer.getInstance().getServers().containsKey(paramString);
	}
}
