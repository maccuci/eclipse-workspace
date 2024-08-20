package br.com.houldmc.rankup.commands.player;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.manager.command.Command;
import br.com.houldmc.rankup.manager.command.CommandArgs;
import br.com.houldmc.rankup.manager.command.CommandLoader.CommandClass;
import br.com.houldmc.rankup.manager.menu.BankMenu;
import br.com.houldmc.rankup.manager.menu.KitMenu;
import br.com.houldmc.rankup.manager.menu.MarketMenu;
import br.com.houldmc.rankup.manager.menu.RankUPMenu;
import br.com.houldmc.rankup.manager.rank.RankManager;
import br.com.houldmc.rankup.manager.rank.list.RankList;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;
import br.com.houldmc.rankup.player.account.crud.RankUPPlayerCrud;

public class BasicsCommand implements CommandClass {
	
	@Command(name = "kits", aliases = "kit")
	public void kits(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		
		if(args.length >= 0) {
			KitMenu.principal(player);
		}
	}
	
	@Command(name = "loja", aliases = "shop")
	public void shop(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		
		if(args.length >= 0) {
			MarketMenu.principal(player);
		}
	}
	
	@Command(name = "banco", aliases = "bank")
	public void bank(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		
		if(args.length >= 0) {
			BankMenu.open(player);
		}
	}
	
	@Command(name = "rankup")
	public void rankup(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();

		if(args.length >= 0) {
			RankUPMenu.open(player);
		}
	}
	
	@Command(name = "g", aliases = {"global", "globalchat", "gchat"})
	public void gchat(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player.getUniqueId());
		
		if(args.length > 0) {
			String message = ItemBuilder.getArgs(args, 0);
			
			if(message == null || message.isEmpty()) {
				player.sendMessage("§c/" + commandArgs.getLabel() + " <message>");
				return;
			}
			
			Bukkit.getOnlinePlayers().forEach(online -> online.sendMessage("§9[g] §7[" + rankUPPlayer.getRank().getTag() + "§7] " + rankUPPlayer.getGroup().getTag() + player.getName() + " §6§l: §7" + message.replace("%", "%%")));
		}
	}
	
	@Command(name = "tpa")
	public void tpa(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		Map<Player, Player> tpa = new HashMap<>(2);
		
		if(args.length < 1) {
			player.sendMessage("§c/tpa <player> [aceitar/recusar]");
			return;
		}
		
		if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			
			if(target == null) {
				player.sendMessage("§cEste alvo não foi encontrado.");
				return;
			}
			
			if(tpa.containsKey(target)) {
				player.sendMessage("§cVocê já enviou um pedido de tpa para este jogador.");
				return;
			}
			
			tpa.put(player, target);
			player.sendMessage("§6§lTPA §7» §aVocê enviou um pedido de TPA para o jogador " + target.getName());
			target.sendMessage("§6§lTPA §7» §aO jogador " + player.getName() + " te enviou um pedido de tpa!");
		}
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("aceitar")) {
				Player target = tpa.get(player);
				
				if(target == null) {
					player.sendMessage("§cEste alvo não foi encontrado.");
					return;
				}
				
				if(!tpa.containsKey(target)) {
					player.sendMessage("§cVocê não possui um pedido de tpa deste jogador.");
					return;
				}
				
				target.teleport(player);
				target.sendMessage("§6§lTPA §7» §aVocê foi teleportado até o jogador " + target.getName());
				tpa.remove(target, player);
			}
			if(args[0].equalsIgnoreCase("recusar")) {
				Player target = tpa.get(player);
				
				if(target == null) {
					player.sendMessage("§cEste alvo não foi encontrado.");
					return;
				}
				
				if(!tpa.containsKey(target)) {
					player.sendMessage("§cVocê não possui um pedido de tpa deste jogador.");
					return;
				}
				target.sendMessage("§6§lTPA §7» §cSeu pedido de TPA foi recusado.");
				tpa.remove(player, target);
			}
		}
	}
	
	@Command(name = "reparar")
	public void reparar(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		ItemStack item = player.getItemInHand();
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		
		if(args.length >= 0) {
			try {
				if (item.getDurability() == 0) {
					player.sendMessage("§6§lReparar §7» §cEste item não precisa ser reparado.");
					return;
				}

				if (rankUPPlayer.getMoney() < 500) {
					player.sendMessage("§6§lReparar §7» §cVocê não tem dinheiro para reparar.");
					return;
				}

				item.setDurability((short) 0);
				player.sendMessage("§6§lReparar §7» §aVocê reparou o item " + item.getType().name() + "!");
				rankUPPlayer.setMoney(rankUPPlayer.getMoney() - 500);
				new RankUPPlayerCrud().update(rankUPPlayer);
			} catch (Exception e){
				player.sendMessage("§6§lReparar §7» §cVocê não pode reparar este item.");
				return;
			}
		}
	}
	
	@Command(name = "ranks", aliases = "ranklist")
	public void ranks(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		
		if(args.length >= 0) {
			player.sendMessage("§6§lRank §7» §fConfira abaixo todos os nossos ranks do servidor, e a quantia para updar de cada um.");
			for(RankList ranks : RankList.values()) {
				player.sendMessage(ranks.getTag() + " §f- §b" + ItemBuilder.format(ranks.getPrice()));
			}
			player.sendMessage("§6§lRank §7» §fVocê está no rank " + rankUPPlayer.getRank().getTag());
			if(new RankManager().getNextRank(player) == RankList.EMPEROR)
				return;
			player.sendMessage("§6§lRank §7» §fSeu próximo rank " + new RankManager().getNextRank(player).getTag());
		}
	}
}
