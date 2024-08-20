package me.feenks.wdr.bungee.commands;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class WDRBungeeCommand extends Command {

	public WDRBungeeCommand() {
		super("wdrbungee", "", new String[] {"watchdogreportbungee"});
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer proxiedPlayer = (ProxiedPlayer)sender;
		
		if(args.length <= 1) {
			proxiedPlayer.sendMessage(new TextComponent("§c/wdrbungee <player> <hacks>"));
			return;
		}
		if(args.length >= 2) {
			ProxiedPlayer hacker = ProxyServer.getInstance().getPlayer(args[0]);
			
			if(hacker == null) {
				proxiedPlayer.sendMessage(new TextComponent("§f[§c§lWDR§f] §cEste jogador não foi encontrado no servidor."));
				return;
			}
			String hacks = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ", ");
			
			proxiedPlayer.sendMessage(new TextComponent("§f[§c§lWDR§f] §eVocê reportou o jogador " + hacker.getName() + "! Obrigado pelo seu report."));
			
			TextComponent component = new TextComponent("§bClique para teleportar.");
			for(ProxiedPlayer online : ProxyServer.getInstance().getPlayers()) {
				if(online.hasPermission("wdr.seereport") && online.getGroups().contains("dono")) {
					online.sendMessage(new TextComponent("§aUm novo report chegou!"));
					online.sendMessage(new TextComponent(" "));
					online.sendMessage(new TextComponent("Jogador: §c" + hacker.getName()));
					online.sendMessage(new TextComponent("Hacks: §e" + hacks));
					online.sendMessage(new TextComponent(" "));
					if(online.getServer().getInfo().getName() == hacker.getServer().getInfo().getName()) {
						component.setClickEvent(
								new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tp " + hacker.getName()));
					} else {
						component.setClickEvent(
								new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/server " + hacker.getServer().getInfo().getName()));
					}
					component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
							new ComponentBuilder("§aClique para ir até o servidor").create()));
				}
			}
		}
	}
	
	public Iterable<String> onTabComplete(CommandSender cs, String[] args) {
		if ((args.length > 2) || (args.length == 0)) {
			return ImmutableSet.of();
		}
		Set<String> match = new HashSet<>();
		if (args.length == 1) {
			String search = args[0].toLowerCase();
			for (ProxiedPlayer player : ProxyServer.getInstance().getPlayers()) {
				if (player.getName().toLowerCase().startsWith(search)) {
					match.add(player.getName());
				}
			}
		}
		return match;
	}
}
