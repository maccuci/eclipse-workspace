package me.ale.commons.bukkit.command;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ale.commons.CyonAPI;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.bukkit.event.faction.PlayerJoinFactionEvent;
import me.ale.commons.bukkit.menu.FactionMenu;
import me.ale.commons.core.account.manager.FactionManager;
import me.ale.commons.core.command.Command;
import me.ale.commons.core.command.CommandArgs;
import me.ale.commons.core.command.CommandLoader.CommandClass;

public class FactionCommand implements CommandClass {

	private Map<Player, String> invites = new HashMap<>();
	
	@Command(name = "faction")
	public void faction(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		FactionManager faction = BukkitMain.getPlugin().getFactionManager();
		
		if(args.length == 0) {
			player.sendMessage(CyonAPI.WARNING_PREFIX + "Abaixo listamos para você os comandos.");
			player.sendMessage(" ");
			player.sendMessage("§c/faction create <name>");
			player.sendMessage("§c/faction invite <name>");
			player.sendMessage("§c/faction remove <name>");
			player.sendMessage("§c/faction accept");
			player.sendMessage("§c/faction status");
			player.sendMessage("§c/faction members");
			player.sendMessage("§c/faction chat");
			player.sendMessage("§c/faction settings");
			player.sendMessage("§c/faction delete");
			return;
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("chat")) {
				
				if(faction.hasPlayerFaction(player)) {
					if(faction.hasChat(player)) {
						player.sendMessage(CyonAPI.WARNING_PREFIX + "Você saiu do chat da sua faction.");
						faction.put(player, false);
					} else {
						player.sendMessage(CyonAPI.WARNING_PREFIX + "§aVocê entrou do chat da sua faction.");
						faction.put(player, true);
					}
				}
			}
			if(args[0].equalsIgnoreCase("members")) {
				
				if(!faction.hasPlayerFaction(player)) {
					return;
				}
				player.sendMessage("§cMembros da faction §7" + faction.getFactionName(player));
				for(String members : faction.getFaction(player).getMembers()) {
					String stats = "";
					if(Bukkit.getPlayer(members) == null) {
						stats = "§cOffline";
					} else {
						stats = "§aOnline";
					}
					player.sendMessage("§e• §7" + members + " §eestá " + stats + ".");
				}
			}
			if(args[0].equalsIgnoreCase("delete")) {
				
			}
			if(args[0].equalsIgnoreCase("status")) {
				FactionMenu.status(player);
			}
			
			if(args[0].equalsIgnoreCase("settings")) {
				FactionMenu.settings(player);
			}
			
			if(args[0].equalsIgnoreCase("accept")) {
				if(!invites.containsKey(player)) {
					player.sendMessage("§cVocê não possui nenhum convite.");
					return;
				}
				
				if(faction.hasPlayerFaction(player)) {
					player.sendMessage("§cVocê já está em um faction.");
					return;
				}
				
				player.sendMessage("§aVocê entrou na faction " + invites.get(player));
				faction.addMember(invites.get(player), player.getName());
				PlayerJoinFactionEvent event = new PlayerJoinFactionEvent(player, invites.get(player), faction.getFaction(player).getMessage());
				Bukkit.getPluginManager().callEvent(event);
			}
		}
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("invite")) {
				if(!faction.hasOwnerPlayer(player)) {
					player.sendMessage("§cVocê não possui uma faction.");
					return;
				}
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage("§cEste jogador não está online.");
					return;
				}
				
				if(invites.containsKey(target)) {
					player.sendMessage("§cVocê já convidou esse jogador. Espere ele aceitar ou recusar seu convite.");
					return;
				}
				invites.put(target, faction.getFactionName(player));
				player.sendMessage("§aVocê enviou o convite para o jogador " + target.getName() + " para a sua faction.");
				target.sendMessage("§a" + player.getName() + " te convidou para a faction dele. Aceite ou recuse. O convite expira em 1 minuto.");
				Bukkit.getScheduler().scheduleSyncDelayedTask(BukkitMain.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						invites.remove(target);
						player.sendMessage("§cO convite expirou!");
						target.sendMessage("§cO convite expirou!");
					}
				}, 3000);
			}
			if(args[0].equalsIgnoreCase("remove")) {
				if(!faction.hasOwnerPlayer(player)) {
					player.sendMessage("§cVocê não possui uma faction.");
					return;
				}
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage("§cEste jogador não está online.");
					return;
				}
				
				if(!faction.getFaction(player).getMembers().contains(target.getName())) {
					player.sendMessage("§cEste jogador não está na sua faction.");
					return;
				} else {
					player.sendMessage("§cVocê removeu o jogador " + target.getName() + " da sua faction.");
					target.sendMessage("§cVocê foi expulso da faction " + faction.getFactionName(target));
					faction.removeMember(faction.getFactionName(player), target.getName());
				}
			}
			if(args[0].equalsIgnoreCase("create")) {
				String name = args[1];
				
				if(name.length() > 6) {
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Você não pode colocar mais do que 6 letras.");
					return;
				}
				if(faction.hasPlayerFaction(player)) {
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Você já está em uma faction.");
					return;
				}
				
				if(!faction.hasFaction(name)) {
					boolean create = faction.createFaction(name, player.getName(), "None", player.getName());
					
					if(create) {
						player.sendMessage(CyonAPI.WARNING_PREFIX + "Você criou a sua faction com o nome " + faction.getFactionName(player) + " e com a mensagem de boas vindas como " + faction.getFaction(player).getMessage());
					} else {
						player.sendMessage(CyonAPI.WARNING_PREFIX + "Algo deu errado durante a criação da sua faction.");
					}
				}
			}
		}
	}

}
