package com.fuzion.core.bukkit.command;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.core.bukkit.BukkitMain;
import com.fuzion.core.bukkit.manager.BukkitUUIDFetcher;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.AccountManager;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;
import com.fuzion.core.master.data.language.Language;
import com.fuzion.core.master.data.language.LanguageManager;
import com.fuzion.core.util.mojang.FakePlayerUtils;

public class YoutubeCommand implements CommandClass {
	
	@Command(name = "language", aliases = {"lang", "translate", "l"})
	public void language(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length <= 0) {
			player.sendMessage("§cUse: /" + cmdArgs.getLabel() + " <language>");
			return;
		}
		if(args.length == 1) {
			Language language = Language.PORTUGUESE;
			try {
				language = Language.valueOf(args[0].toUpperCase());
			} catch (Exception e) {
				try {
					language = Language.getLanguage(Integer.parseInt(args[0]));
				} catch (Exception e2) {
					player.sendMessage("§cEste idioma não foi encontrada.");
					return;
				}
			}
			AccountManager.getAccount(player).setLanguage(language);
			if(language == Language.PORTUGUESE) {
				player.sendMessage("§aSeu idioma foi alterado para " + language.getName() + " com sucesso.");
			} else {
				player.sendMessage(new LanguageManager().getMessage("%language-translated%", player).replace("%language%", language.getName()));
				player.sendMessage("§e§l[Debug]: §fThis language has not been translated completely.");
			}
		}
	}
	
	@Command(name = "fake", groupToUse = Group.YOUTUBER)
	public void fake(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if (args.length != 1) {
			player.sendMessage("§cUse /fake <nick> para trocar de nick.");
			player.sendMessage("§cUse /fake para voltar ao seu nick original.");
		}
		
		if(args.length == 0) {
			if (FakePlayerUtils.isFake(player)) {
				new BukkitRunnable() {
					@Override
					public void run() {
						FakePlayerUtils.removePlayerSkin(player, false);
						FakePlayerUtils.changePlayerName(player, FakePlayerUtils.getOriginalName(player), true);
						FakePlayerUtils.removePlayer(player);
						player.sendMessage("§eVocê voltou ao seu nick original.");
					}
				}.runTask(BukkitMain.getPlugin());
			}
		}
		
		if(args.length == 1) {
			String playerName = args[0];
			
			if(playerName == new GroupManager().get(player.getUniqueId(), "nickname")) {
			    FakePlayerUtils.changePlayerSkin(player, new GroupManager().get(player.getUniqueId(), "nickname"), UUID.fromString(new GroupManager().get(player.getUniqueId(), "uuid")), false);
			    FakePlayerUtils.changePlayerName(player, new GroupManager().get(player.getUniqueId(), "nickname"), true);
			    player.sendMessage("§eVocê resetou o seu fake!");
			    return;
			}
			
			if (args[0].equalsIgnoreCase("check")) {
				if (FakePlayerUtils.fakenames.isEmpty()) {
					player.sendMessage("§cNão há nenhum fake no momento!");
					return;
				}
				player.sendMessage("§cLista de todos os fakes: ");
				for (UUID u : FakePlayerUtils.fakenames.keySet()) {
					String nome = FakePlayerUtils.originalnames.get(u);
					String fake = FakePlayerUtils.fakenames.get(u);
					player.sendMessage("§7Nick: §b" + nome + " §7Fake: §c" + fake);
				}
			}
			
			if (!FakePlayerUtils.validateName(playerName)) {
				player.sendMessage("§cUse: /fake <fakeName>");
				return;
			}
			
			if (!FakePlayerUtils.validateName(playerName)) {
				player.sendMessage("§cNick invalido.");
				return;
			}
			if (!FakePlayerUtils.isFake(playerName)) {
				player.sendMessage("§cNick invalido.");
				return;
			}
			
			if(new BukkitUUIDFetcher().getUUID(playerName) != null) {
				player.sendMessage("§cEste nick já existe.");
				return;
			}
			
			new BukkitRunnable() {
				
				@Override
				public void run() {
					 FakePlayerUtils.removePlayerSkin(player, false);
				     FakePlayerUtils.changePlayerName(player, playerName, true);
				     player.sendMessage("§aVocê alterou o seu fake!");
				}
			}.runTask(BukkitMain.getPlugin());
		}
	}
}
