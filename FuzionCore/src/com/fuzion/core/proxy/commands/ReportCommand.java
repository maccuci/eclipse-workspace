package com.fuzion.core.proxy.commands;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.proxy.manager.ReportManager;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ReportCommand extends Command {

	public ReportCommand() {
		super("report");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer player = (ProxiedPlayer)sender;
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /report <playerName> [reason]");
			return;
		}
		
		if(args.length >= 2) {
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[0]);
			if(target == null) {
				player.sendMessage("§cAlvo não encontrado.");
				return;
			}
			
			if(CooldownAPI.isInCooldown(player.getUniqueId(), "report")) {
				player.sendMessage("§cVocê ainda não pode reportar. Espere durante " + DateUtils.formatDifference(CooldownAPI.getTimeLeft(player.getUniqueId(), "report")));
				return;
			}
			
			StringBuilder str = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				str.append(args[i]).append(" ");
			}
			
			player.sendMessage("§aVocê reportou o jogador " + target.getName() + ".");
			ReportManager.addReport(player, target, str.toString());
			CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), "report", 10);
			cooldown.start();
		}
	}

}
