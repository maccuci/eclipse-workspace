package com.fuzion.core.bukkit.command;

import org.bukkit.entity.Player;

import com.fuzion.core.master.clan.ClanManager;
import com.fuzion.core.master.clan.ClanRank;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;

public class ClanCommand implements CommandClass {
	
	@Command(name = "clan")
	public void clan(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		ClanManager clanManager = new ClanManager();
		
		if(args.length == 0) {
			player.sendMessage("§a/clan criar <tag> <nome>");
			player.sendMessage("§a/clan deletar");
			player.sendMessage("§a/clan convidar <jogador>");
			player.sendMessage("§a/clan remover <jogador>");
			player.sendMessage("§a/clan status");
			player.sendMessage("§a/clan ranks");
			return;
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("ranks")) {
				for(ClanRank ranks : ClanRank.values()) {
					player.sendMessage(ranks.getColor() + ranks.getSymbol() + " " + ranks.name() + " §7- §fXP: §b" + ranks.getXp());
				}
				if(clanManager.hasPlayerClan(player)) {
					player.sendMessage("Seu clan está no rank: " + clanManager.getRank(clanManager.getClanName(player)).getColor() + clanManager.getRank(clanManager.getClanName(player)).name());
					player.sendMessage("XP do seu clan: " + clanManager.getRank(clanManager.getClanName(player)).getXp());
				}
			}
			if(args[0].equalsIgnoreCase("status")) {
				if(!clanManager.hasPlayerClan(player)) {
					player.sendMessage("§cVocê não possui um clan para ver status!");
					return;
				}
				int kills = clanManager.getClan(player).getKills();
				int deaths = clanManager.getClan(player).getDeaths();
				int xp = clanManager.getClan(player).getXp();
				int money = clanManager.getClan(player).getMoney();
				ClanRank rank = clanManager.getClanRank(player);
				
				player.sendMessage("§e§m------------------------");
				player.sendMessage("Kills: §b" + kills);
				player.sendMessage("Deaths: §b" + deaths);
				player.sendMessage("Coins: §b" + money);
				player.sendMessage(" ");
				player.sendMessage("Rank: §b" + rank.getColor() + rank.getSymbol() + " " + rank.name());
				player.sendMessage("XPs: §b" + xp);
				player.sendMessage("§e§m------------------------");
			}
		}
		
		if(args.length == 3) {
			if(args[0].equalsIgnoreCase("criar")) {
				String tag = args[1];
				String name = args[2];
				
				if(tag.length() > 3 || name.length() > 6) {
					player.sendMessage("§eSeu clan não pode ter a tag e nem o nome maior do que 6 caractéres.");
					return;
				}
				if(clanManager.hasPlayerClan(player)) {
					player.sendMessage("§eVocê já se encontra em um clan!");
					return;
				}

				if(!clanManager.hasClan(name)) {
				boolean create = clanManager.createClan(name, tag, player.getName(), player.getName());
				
					if (create) {
						player.sendMessage("§bVocê criou o seu clan com sucesso!");
						clanManager.setup();
					} else {
						player.sendMessage("§cAlgo deu errado durante a criação do seu clan!");
					}
				} else {
					player.sendMessage("§cEste nome já existe.");
				}
			}
		}
	}
}
