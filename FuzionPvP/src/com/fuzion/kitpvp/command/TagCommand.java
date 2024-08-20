package com.fuzion.kitpvp.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.core.master.account.Tag;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;
import com.fuzion.kitpvp.manager.ScoreboardManager;
import com.fuzion.kitpvp.manager.position.PositionInformation;
import com.fuzion.kitpvp.manager.position.PositionManager;


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
	
	@Command(name = "ranking")
	public void ranking(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length >= 0) {
			player.sendMessage("§aListando os TOP 10 jogadores da rede abaixo.");
			for(PositionInformation information : PositionManager.POSITION) {
					player.sendMessage("§6" + information.getPosition() + "° §7- §f" + information.getName());
					return;
			}
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
				new ScoreboardManager().createScoreboard(player);
				TagManager tagManager = new TagManager(player);
				tagManager.setTag(tagManager.getTag());
				tagManager.update();
				for(Player online : Bukkit.getOnlinePlayers()) {
					new TagManager(online).update();
				}
			}
		}
	}
}
