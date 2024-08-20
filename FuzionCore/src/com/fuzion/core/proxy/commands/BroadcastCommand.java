package com.fuzion.core.proxy.commands;

import com.fuzion.core.master.account.Group;
import com.fuzion.core.proxy.ProxyMain;
import com.fuzion.core.proxy.manager.account.GroupManager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class BroadcastCommand extends Command {
	
	public BroadcastCommand() {
		super("broadcast", "", "bc", "alert");
	}
	
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer)sender;
		if(!new GroupManager().hasGroupPermission(proxiedPlayer, Group.ADMIN)) {
			proxiedPlayer.sendMessage(new TextComponent("§cVocê não possui permissão."));
			return;
		}
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < args.length; i++) {
			str.append(args[i]).append(" ");
		}
		for (ProxiedPlayer online : ProxyMain.getPlugin().getProxy().getPlayers()) {
			online.sendMessage(new TextComponent(" "));
			online.sendMessage(new TextComponent("§4§lAVISO"));
			online.sendMessage(new TextComponent(ChatColor.translateAlternateColorCodes('&', str.toString())));
			online.sendMessage(new TextComponent(" "));
		}
	}

}
