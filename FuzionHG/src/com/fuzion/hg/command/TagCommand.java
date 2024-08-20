package com.fuzion.hg.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.json.JSONChatClickEventType;
import com.fuzion.core.api.json.JSONChatExtra;
import com.fuzion.core.api.json.JSONChatHoverEventType;
import com.fuzion.core.api.json.JSONChatMessage;
import com.fuzion.core.master.account.Tag;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;
import com.fuzion.hg.event.PlayerSelectKitEvent;
import com.fuzion.hg.manager.ScoreboardManager;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.game.GameStage;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.timer.GameTimer;
import com.fuzion.hg.manager.timer.InvincibilityTimer;
import com.fuzion.hg.manager.timer.PreGameTimer;

public class TagCommand implements CommandClass {
	
	public static List<Player> scoreboard = new ArrayList<>();
	
	@SuppressWarnings("deprecation")
	@Command(name = "tag")
	public void tag(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		TagManager tagManager = new TagManager(player);
		
		if(args.length != 1) {
			tagManager.sendTags();
			return;
		}
		
		if(args.length == 1) {
			Tag tag = Tag.NORMAL;
			try {
				tag = Tag.valueOf(args[0].toUpperCase());
			} catch (Exception e) {
				player.sendMessage("§cEsta tag não existe.");
				return;
			}
			
/*			if(tag == Tag.WINNER) {
				if(Main.getPlugin().getWinnerManager().hasWinner(player)) {
					tagManager.setTag(tag);
					tagManager.update();
					for(Player online : Bukkit.getOnlinePlayers()) {
						new TagManager(online).update();
					}
					player.sendMessage("§aSua tag foi alterada para " + tag.getName());
					return;
				} else {
					player.sendMessage("§cVocê não foi o winner da partida passada!");
					return;
				}
			}*/
			
			if(tagManager.hasTag(tag)) {
				player.sendMessage("§cVocê já está usando esta tag.");
				return;
			}
			
			if(!tagManager.hasTagPermission(tag)) {
				player.sendMessage("§cVocê não tem a tag " + tag.getName());
				return;
			}
			
			tagManager.setTag(tag);
			tagManager.update();
			for(Player online : Bukkit.getOnlinePlayers()) {
				new TagManager(online).update();
			}
			player.sendMessage("§aSua tag foi alterada para " + tag.getName());
		}
	}
	
	@Command(name = "kit")
	public void kit(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<String> kitsName = new ArrayList<>();
		
		if(args.length == 0) {
			List<Kit> yourKits = new ArrayList<>();
			JSONChatMessage chatMessage = new JSONChatMessage("§eSeus kits:", null, null);
			for(int i = 0; i < kits.size(); i++) {
				Kit kit = kits.get(i);
				JSONChatExtra chatExtra = new JSONChatExtra(kit.getName().isEmpty() ? "" + kit.getName() : ", " + kit.getName());
				chatExtra.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, "§6Clique para selecionar!");
				chatExtra.setClickEvent(JSONChatClickEventType.RUN_COMMAND, "/kit " + kit.getName());
				if(kit.canUseKit(player)) {
					yourKits.add(kit);
					chatMessage.addExtra(chatExtra);
				}
			}
			if(yourKits.size() == 0) {
				player.sendMessage("§cVocê não possui nenhum kit.");
				return;
			}
			JSONChatExtra finalPoint = new JSONChatExtra("§f.");
			finalPoint.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, "");
			chatMessage.addExtra(finalPoint);
			chatMessage.sendToPlayer(player);
		}
		if(args.length == 1) {
			Kit kit = null;
			String kitName = args[0];
			
			try {
				kit = KitManager.getKit(kitName);
			} catch (Exception e) {
				player.sendMessage("§cEste kit não existe.");
				return;
			}
			for(int i = 0; i < kits.size(); i++) {
				Kit k = kits.get(i);
				kitsName.add(k.getName().toUpperCase());
			}
			
			if(!kitsName.contains(kitName.toUpperCase())) {
				player.sendMessage("§cEste kit não existe.");
				return;
			}
			
			if(!kit.canUseKit(player)) {
				player.sendMessage("§cVocê não possui este kit.");
				return;
			}
			
			PlayerSelectKitEvent event = new PlayerSelectKitEvent(player, kit);
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	@SuppressWarnings("deprecation")
	@Command(name = "score")
	public void score(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			if(!scoreboard.contains(player)) {
				scoreboard.add(player);
				player.sendMessage("§cVocê desativou a scoreboard.");
				player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
				TagManager tagManager = new TagManager(player);
				tagManager.setTag(tagManager.getTag());
				tagManager.update();
				for(Player online : Bukkit.getOnlinePlayers()) {
					new TagManager(online).update();
				}
			} else {
				scoreboard.remove(player);
				player.sendMessage("§aVocê ativou a scoreboard.");
				if(GameManager.getStage() == GameStage.WAITING) {
					new ScoreboardManager().waiting(player);
				} else if(GameManager.getStage() == GameStage.INVINCIBILITY) {
					new ScoreboardManager().invincibility(player);
				} else if(GameManager.getStage() == GameStage.GAME) {
					new ScoreboardManager().game(player);
				}
				TagManager tagManager = new TagManager(player);
				tagManager.setTag(tagManager.getTag());
				tagManager.update();
				for(Player online : Bukkit.getOnlinePlayers()) {
					new TagManager(online).update();
				}
			}
		}
	}
	
	@Command(name = "game", aliases = {"info", "gameinfo", "jogo", "partida"})
	public void game(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length >= 0) {
			player.sendMessage("§7--==[ §6§lFuzionHG Informações §7]==--");
			player.sendMessage(" ");
			player.sendMessage("§7Estágio: §6" + ItemBuilder.captalise(GameManager.translateStageName(GameManager.getStage())));
			player.sendMessage("§7Tempo: §6" + ItemBuilder.format(GameManager.getStage() == GameStage.GAME ? GameTimer.timer : GameManager.getStage() == GameStage.INVINCIBILITY ? InvincibilityTimer.timer : GameManager.getStage() == GameStage.WAITING ? PreGameTimer.timer : 0));
			player.sendMessage(" ");
			player.sendMessage("§7Kit: §6" + new KitManager().getKitName(player));
			player.sendMessage("§7Jogadores: §6" + GameManager.getPlayerSize());
			player.sendMessage("§7Kills: §6" + GameManager.getKills(player));
		}
	}
}
