package br.com.houldmc.rankup.commands.player;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.manager.command.Command;
import br.com.houldmc.rankup.manager.command.CommandArgs;
import br.com.houldmc.rankup.manager.command.CommandLoader.CommandClass;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;
import br.com.houldmc.rankup.player.account.crud.RankUPPlayerCrud;
import br.com.houldmc.rankup.player.clan.ClanManager;
import br.com.houldmc.rankup.player.clan.utilitaries.Clan;

public class ClanCommand implements CommandClass {
	
	private static final Map<Player, String> invites = new HashMap<Player, String>();
	
	@Command(name = "clan")
	public void clan(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		ClanManager clanManager = new ClanManager();
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		
		if(args.length == 0) {
			player.sendMessage("§6§lClan §7» §fConfira abaixo os comandos.");
			player.sendMessage(" ");
			player.sendMessage("§e/clan criar <nome> <tag> - §fCrie o seu clan! Você irá precisar de 50.000.00 moedas.");
			player.sendMessage("§e/clan convidar <nome> - §fConvide alguém ao seu clan!");
			player.sendMessage("§e/clan remover <nome> - §fRemova alguém do seu clan!");
			player.sendMessage("§e/clan aceitar <clan> - §fAceite um pedido de um clan!");
			player.sendMessage("§e/clan info <tag> - §fVeja informações sobre um clan!");
			player.sendMessage("§e/clan top - §fVeja os top clans money do servidor!");
			player.sendMessage("§e/clan depositar <quantia> - §fDeposite uma quantia moedas no clan!");
			player.sendMessage("§e/clan retirar <quantia> - §fRetire uma quantia moedas do clan!");
		}
		
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("info")) {
				Clan clan = clanManager.getClanByTagg(args[0]);
				
				if(clan == null) {
					player.sendMessage("§6§lClan §7» §cEste clan não foi encontrado.");
					return;
				}
				
				clanManager.info(player, clan, 1);
			}
			if(args[0].equalsIgnoreCase("depositar")) {
				int amount = 0;
				try {
					amount = NumberUtils.toInt(args[1]);
				} catch (Exception e) {
					player.sendMessage("§6§lClan §7» §cVocê pode colocar apenas números.");
					return;
				}
				
				if(amount > rankUPPlayer.getMoney()) {
					player.sendMessage("§6§lClan §7» §cVocê não possui esta quantia de moedas.");
					return;
				}
				
				clanManager.addMoney(player, clanManager.getClanName(player), amount);
				rankUPPlayer.setMoney(rankUPPlayer.getMoney() - amount);
				new RankUPPlayerCrud().update(rankUPPlayer);
				player.sendMessage("§6§lClan §7» §cVocê depositou " + amount + " moedas no clan.");
			}
			
			if(args[0].equalsIgnoreCase("retirar")) {
				int amount = 0;
				try {
					amount = Integer.parseInt(args[0]);
				} catch (Exception e) {
					player.sendMessage("§6§lClan §7» §cVocê pode colocar apenas números.");
					return;
				}
				
				if(clanManager.getClan(player).getMoney() < amount) {
					player.sendMessage("§6§lClan §7» §cO clan não possui esta quantia moedas.");
					return;
				}
				
				clanManager.addMoney(player, clanManager.getClanName(player), amount);
				rankUPPlayer.setMoney(rankUPPlayer.getMoney() + amount);
				new RankUPPlayerCrud().update(rankUPPlayer);
				player.sendMessage("§6§lClan §7» §cVocê retirou " + amount + " moedas do clan.");
			}
			
			if(args[0].equalsIgnoreCase("convidar")) {
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage("§6§lClan §7» §cAlvo não foi encontrado.");
					return;
				}
				
				if(!clanManager.hasPlayerClanOwner(player.getUniqueId())) {
					player.sendMessage("§6§lClan §7» §cVocê não é dono da sua clan.");
					return;
				}
				
				if(!clanManager.hasPlayerClan(player.getUniqueId())) {
					player.sendMessage("§6§lClan §7» §cVocê não possui um clan.");
					return;
				}
				
				if(invites.containsKey(target)) {
					player.sendMessage("§6§lClan §7» §cVocê já convidou esse jogador para o seu clan.");
					return;
				}
				
				invites.put(target, clanManager.getClanName(player));
				player.sendMessage("§6§lClan §7» §cVocê convidou o jogador " + target.getName() + " para o seu clan.");
				player.sendMessage("§6§lClan §7» §cVocê recebeu um convite para entrar para o clan " + clanManager.getClanName(player));
				
				Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
					
					@Override
					public void run() {
						invites.remove(target);
						
						if(player != null || target != null) {
							player.sendMessage("§6§lClan §7» §cO convite expirou!");
							target.sendMessage("§6§lClan §7» §cO convite expirou!");
						}
					}
				}, 3000);
			}
			
			if(args[0].equalsIgnoreCase("remover")) {
				Player target = Bukkit.getPlayer(args[1]);
				
				if(target == null) {
					player.sendMessage("§6§lClan §7» §cAlvo não foi encontrado.");
					return;
				}
				
				if(!clanManager.hasPlayerClanOwner(player.getUniqueId())) {
					player.sendMessage("§6§lClan §7» §cVocê não é dono da sua clan.");
					return;
				}
				
				if(!clanManager.hasPlayerClan(player.getUniqueId())) {
					player.sendMessage("§6§lClan §7» §cVocê não possui um clan.");
					return;
				}
				
				if(!clanManager.getClan(player).getMembers().contains(target.getName())) {
					player.sendMessage("§6§lClan §7» §cEste jogador não é do seu clan.");
					return;
				}
				
				player.sendMessage("§6§lClan §7» §aVocê removeu o jogador " + target.getName() + " do clan!");
				target.sendMessage("§6§lClan §7» §cVocê foi removidor da clan " + clanManager.getClanName(target));
				clanManager.removeMemberClan(clanManager.getClanName(player), target.getName());
			}
			
			if(args[0].equalsIgnoreCase("aceitar")) {
				if(!invites.containsKey(player)) {
					player.sendMessage("§6§lClan §7» §cVocê não possui nenhum convite.");
					return;
				}
				
				if(!(invites.get(player) == null)) {
					player.sendMessage("§6§lClan §7» §cVocê não possui nenhum convite deste clan.");
					return;
				}
				
				if(clanManager.hasPlayerClan(player.getUniqueId())) {
					player.sendMessage("§6§lClan §7» §cVocê já está em um clan.");
					return;
				}
				
				player.sendMessage("§6§lClan §7» §cVocê acaba de entrar para o clan " + invites.get(player) + ".");
				clanManager.broadcast("§aO jogador " + player.getName() + " acaba de entrar para o clan.");
			}
		}
		
		if(args.length == 3) {
			if(args[0].equalsIgnoreCase("criar")) {
				String name = args[1];
				String tag = args[2];
				
				if(rankUPPlayer.getMoney() < 50000) {
					player.sendMessage("§6§lClan §7» §cVocê precisa de 50.000.00 moedas para criar o seu clan.");
					return;
				}
				
				if(tag.length() > 4) {
					player.sendMessage("§6§lClan §7» §cA tag do clan não pode ser maior do que 4 características.");
					return;
				}
				
				if(name.length() > 16) {
					player.sendMessage("§6§lClan §7» §cO nome do clan não pode ser maior do que 16 características.");
					return;
				}
				
				if(clanManager.hasPlayerClan(player.getUniqueId())) {
					player.sendMessage("§6§lClan §7» §cVocê já está em um clan.");
					return;
				}
				
				if(clanManager.hasClan(name)) {
					player.sendMessage("§6§lClan §7» §cJá existe um clan com este nome.");
					return;
				}
				
				if(clanManager.hasClanTag(tag)) {
					player.sendMessage("§6§lClan §7» §cJá existe uma tag de clan com este nome.");
					return;
				}
				
				clanManager.createClan(player.getUniqueId(), name, tag, player.getName());
				clanManager.setup();
				rankUPPlayer.setMoney(rankUPPlayer.getMoney() - 50000);
				rankUPPlayer.update();
				
				player.sendMessage("§6§lClan §7» §fVocê criou o clan §6" + name + "§7(" + tag +") §fcom sucesso.");
				Bukkit.broadcastMessage("§6§lClan §7» §fO jogador §e" + player.getName() + " §fcriou o clan §6" + name + "§7(" + tag +")");
			}
		}
	}
}
