package me.feenks.wdr.commands;

import java.util.Arrays;
import java.util.AbstractMap.SimpleEntry;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.feenks.wdr.WDR;
import me.feenks.wdr.checks.manager.ReachChecker;
import me.feenks.wdr.checks.manager.ReachChecker.Callback;
import net.minecraft.util.org.apache.commons.lang3.StringUtils;

public class WDRCommand implements CommandExecutor {
	
	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		String[] hacks = new String[] {"forcefield", "ff", "killaura", "reach", "autosoup", "glide", "autoarmor", "flyspeed", "fly", "speed"};
		
		
		if(!(sender instanceof Player)) {
			sender.sendMessage(WDR.PREFIX + " Este comando só pode ser utilizado por jogadores.");
			return true;
		}
		
		if(args.length <= 1) {
			player.sendMessage("§c/wdr <player> <hacks>");
			return true;
		}
		if(args.length >= 2) {
			Player hacker = Bukkit.getPlayer(args[0]);
			if(hacker == null) {
				player.sendMessage(WDR.PREFIX + "§cEste jogador não foi encontrado na sua partida! Tente utilizar o comando /wdrbungee para reportar qualquer jogador do servidor.");
				return true;
			}
			String hacksArguments = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ", ");
			
			for(String s : hacks) {
				if(!hacksArguments.contains(s)) {
					player.sendMessage(WDR.PREFIX + "§cIsto não é um hack válido! Tente outro.");
					return true;
				}
			}
			
			player.sendMessage(WDR.PREFIX + "§eVocê reportou o jogador " + hacker.getName() + "! Obrigado pelo seu report.");
			
			for(String s : hacks) {
				if(s.contains("force") || s.contains("reach") || s.contains("kill")) {
					ReachChecker checker = new ReachChecker(WDR.getPlugin(), hacker);
					checker.invoke(hacker, new Callback() {
						
						@Override
						public void done(long paramLong1, long paramLong2, SimpleEntry<Integer, Integer> s,
								CommandSender paramCommandSender) {
							player.sendMessage("§cO jogador matou: §f" + s.getValue() + " §cde 5 bots.");
							if(s.getValue() >= 4) {
								player.sendMessage("§cEste jogador será banido automaticamente pelo WDR.");
								
								Bukkit.dispatchCommand(sender, "ban " + hacker.getName() + " Forcefield/KillAura");
								Bukkit.broadcast(WDR.PREFIX + "§cBaniu o jogador " + hacker.getName() + " automaticamente por Forcefield/KillAura.", "wdr.seeban");
							}
						}
					});
				}	
			}
			
			for(Player online : Bukkit.getOnlinePlayers()) {
				if(online.hasPermission("wdr.seereport") && online.isOp()) {
					online.sendMessage(WDR.PREFIX + "§aUm novo report chegou!");
					online.sendMessage(" ");
					online.sendMessage("Jogador: §c" + hacker.getName());
					online.sendMessage("Hacks: §e" + hacksArguments);
					online.sendMessage(" ");
				}
			}
		}
		return false;
	}

}
