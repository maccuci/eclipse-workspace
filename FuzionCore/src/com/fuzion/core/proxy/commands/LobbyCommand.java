package com.fuzion.core.proxy.commands;

import com.fuzion.core.proxy.ProxyMain;
import com.fuzion.core.proxy.manager.socket.ServerInfo;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {
	
	public LobbyCommand() {
		super("lobby", "", "hub");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer)sender;
		ServerInfo lobby = ProxyMain.getPlugin().getLobby();
		
		if (lobby == null) {
			proxiedPlayer.sendMessage(new TextComponent("§cNão há nenhum lobby disponível."));
			return;
		}
		proxiedPlayer.connect(ProxyMain.getPlugin().getProxy().getServerInfo(lobby.getName()));
	}

}
