package com.fuzion.core.proxy.commands;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ConnectCommand extends Command {
	
	public ConnectCommand() {
		super("connect");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer)sender;
		
		if(args.length == 0) {
			proxiedPlayer.sendMessage(new TextComponent("§cUse: /connect <serverName>"));
			return;
		}
		
		if(args.length == 1) {
			String serverName = args[0];
			ServerInfo info = ProxyServer.getInstance().getServerInfo(serverName);
			
			if(info == null) {
				proxiedPlayer.sendMessage(new TextComponent("§cEste servidor não foi encontrado."));
				return;
			}
			proxiedPlayer.sendMessage(new TextComponent("§aConectando ao servidor §f" + info.getName()));
			proxiedPlayer.connect(info);
		}
	}

}
