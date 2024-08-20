package com.fuzion.kitpvp.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.json.JSONChatClickEventType;
import com.fuzion.core.api.json.JSONChatExtra;
import com.fuzion.core.api.json.JSONChatHoverEventType;
import com.fuzion.core.api.json.JSONChatMessage;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.TagManager;
import com.fuzion.core.master.account.management.PunishManager.Durations;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;
import com.fuzion.core.master.commands.Completer;
import com.fuzion.kitpvp.event.PlayerSelectKitEvent;
import com.fuzion.kitpvp.manager.ScoreboardManager;
import com.fuzion.kitpvp.manager.kit.Kit;
import com.fuzion.kitpvp.manager.kit.KitManager;
import com.fuzion.kitpvp.manager.warp.WarpManager;
import com.fuzion.kitpvp.manager.warp.Warps;

import net.md_5.bungee.api.ChatColor;

public class KitCommand implements CommandClass {
	
	@SuppressWarnings("deprecation")
	@Command(name = "spawn")
	public void spawn(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length >= 0) {
			player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
			player.setFoodLevel(20);
			WarpManager.teleport(player, Warps.SPAWN);
			new ScoreboardManager().createScoreboard(player);
			TagManager tagManager = new TagManager(player);
			tagManager.findTag();
			tagManager.update();
			for(Player online : Bukkit.getOnlinePlayers()) {
				new TagManager(online).update();
			}
		}
	}
	
	@Command(name = "kit")
	public void kit(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		KitManager kitManager = new KitManager();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<String> kitsName = new ArrayList<>();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /kit <kitName>");
			return;
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
			
			if(kitManager.hasKit(player)) {
				player.sendMessage("§cVocê já está com um kit selecionado.");
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
			
			PlayerSelectKitEvent event = new PlayerSelectKitEvent(player, kit);
			Bukkit.getPluginManager().callEvent(event);
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "givekit", groupToUse = Group.NORMAL)
	public void givekit(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<String> kitsName = new ArrayList<>();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /givekit <playerName> <kit> [time]");
			return;
		}
		if(args.length == 2) {
			Player target = Bukkit.getPlayer(args[0]);
			GroupManager groupManager = new GroupManager();
			Kit kit = null;
			String kitName = args[1];
			
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!groupManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cJogador inexistente no banco de dados.");
					return;
				}
				if(new KitManager().hasSQLKit(offlinePlayer.getUniqueId(), kitName) || KitManager.kitsSQL.contains(kitName)) {
					player.sendMessage("§cEste jogador já possui este kit.");
					return;
				}
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
				new KitManager().giveSQLKit(offlinePlayer.getUniqueId(), offlinePlayer, kitName, Durations.PERMANENT, false, 0);
				player.sendMessage("§eVocê deu o kit §c" + kit.getName() + " §epara o jogador §f" + offlinePlayer.getName());
				return;
			}
			if(!groupManager.exists(target.getUniqueId())) {
				player.sendMessage("§cJogador inexistente no banco de dados.");
				return;
			}
			if(new KitManager().hasSQLKit(target.getUniqueId(), kitName) || KitManager.kitsSQL.contains(kitName)) {
				player.sendMessage("§cEste jogador já possui este kit.");
				return;
			}
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
			new KitManager().giveSQLKit(target.getUniqueId(), target, kitName, Durations.PERMANENT, false, 0);
			player.sendMessage("§eVocê deu o kit §c" + kit.getName() + " §epara o jogador §f" + target.getName());
		}
		if(args.length == 3) {
			Player target = Bukkit.getPlayer(args[0]);
			GroupManager groupManager = new GroupManager();
			Kit kit = null;
			String kitName = args[1];
			
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!groupManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cJogador inexistente no banco de dados.");
					return;
				}
				if(new KitManager().hasSQLKit(offlinePlayer.getUniqueId(), kitName) || KitManager.kitsSQL.contains(kitName)) {
					player.sendMessage("§cEste jogador já possui este kit.");
					return;
				}
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
				String timeString = args[2];
				long time = 0L;
				try {
					time = ItemBuilder.fromString(timeString);
				} catch(Exception exception) {
					player.sendMessage("§cFormato de tempo incorreto. Utilize: 1d,1h,1m,1s.");
					time = 0L;
					return;
				}
				new KitManager().giveSQLKit(offlinePlayer.getUniqueId(), offlinePlayer, kitName, Durations.TEMPORARY, true, time);
				player.sendMessage("§eVocê deu o kit §c" + kit.getName() + " §epara o jogador §f" + offlinePlayer.getName() + " §edurante §c" + ItemBuilder.getMessage(time));
				return;
			}
			if(new KitManager().hasSQLKit(target.getUniqueId(), kitName) || KitManager.kitsSQL.contains(kitName)) {
				player.sendMessage("§cEste jogador já possui este kit.");
				return;
			}
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
			String timeString = args[2];
			long time = 0L;
			try {
				time = ItemBuilder.fromString(timeString);
			} catch(Exception exception) {
				player.sendMessage("§cFormato de tempo incorreto. Utilize: 1d,1h,1m,1s.");
				time = 0L;
				return;
			}
			new KitManager().giveSQLKit(target.getUniqueId(), target, kitName, Durations.TEMPORARY, true, time);
			player.sendMessage("§eVocê deu o kit §c" + kit.getName() + " §epara o jogador §f" + target.getName() + " §edurante §c" + ItemBuilder.getMessage(time));
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "giveallkit", groupToUse = Group.MOD)
	public void giveallkit(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		
		if(args.length <= 0) {
			player.sendMessage("§cUse: /giveallkit <playerName>");
			return;
		}
		if(args.length == 1) {
			Player target = Bukkit.getPlayer(args[0]);
			GroupManager groupManager = new GroupManager();
			
			if(target == null) {
				OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
				if(!groupManager.exists(offlinePlayer.getUniqueId())) {
					player.sendMessage("§cJogador inexistente no banco de dados.");
					return;
				}
				if(groupManager.hasGroup(offlinePlayer.getUniqueId(), Group.ULTRA)) {
					player.sendMessage("§cEste jogador já possui todos os kits.");
					return;
				}
				for(int i = 0; i < kits.size(); i++) {
					Kit kit = kits.get(i);
					
					new KitManager().giveSQLKit(offlinePlayer.getUniqueId(), offlinePlayer, kit.getName(), Durations.PERMANENT, false, 0);
				}
				player.sendMessage("§eVocê deu §cTODOS OS KITS §epara o jogador §f" + offlinePlayer.getName());
				return;
			}
			if(!groupManager.exists(target.getUniqueId())) {
				player.sendMessage("§cJogador inexistente no banco de dados.");
				return;
			}
			if(groupManager.hasGroupPermission(target.getUniqueId(), Group.ULTRA)) {
				player.sendMessage("§cEste jogador já possui todos os kits.");
				return;
			}
			for(int i = 0; i < kits.size(); i++) {
				Kit kit = kits.get(i);
				
				new KitManager().giveSQLKit(target.getUniqueId(), target, kit.getName(), Durations.PERMANENT, false, 0);
			}
			player.sendMessage("§eVocê deu §cTODOS OS KITS §epara o jogador §f" + target.getName());
			return;
		}
	}
	
	@Completer(name = "kit")
	public List<String> kitcompleter(CommandArgs cmdArgs) {
		List<String> list = new ArrayList<>();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length != 1) {
			return null;
		}
		
		if(args.length == 1) {
			String search = args[0].toLowerCase();
			for(int i = 0; i < kits.size(); i++) {
				Kit kit = kits.get(i);
				
				if(kit.canUseKit(player)) {
					if(kit.getName().toLowerCase().startsWith(search)) {
						list.add(kit.getName().toLowerCase());
					}
				}
			}
		}
		
		return list;
	}
	
	public void sendKits(Player player) {
		ArrayList<Kit> list = new ArrayList<>(KitManager.getKits().values());
		JSONChatMessage chatMessage = new JSONChatMessage(ChatColor.GREEN + "Kits que vocę possui: ", null, null);
		for(int i = 0; i < list.size(); i++) {
			Kit kit = list.get(i);
			JSONChatExtra chatExtra = new JSONChatExtra(String.valueOf(kit.getName() == "PvP" ? "" : ", ") + "§e" + kit.getName().toLowerCase());
			chatExtra.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, kit.getExplanation() + "\n§aClique para selecionar.");
			chatExtra.setClickEvent(JSONChatClickEventType.RUN_COMMAND, "/kit " + kit.getName().toLowerCase());
			if(kit.canUseKit(player)) {
				list.add(kit);
				chatMessage.addExtra(chatExtra);
			}
		}
		if(list.size() == 0) {
			player.sendMessage("Você não possui nenhum kit.");
			return;
		}
		JSONChatExtra finalPoint = new JSONChatExtra(ChatColor.RESET + ".");
		finalPoint.setHoverEvent(JSONChatHoverEventType.SHOW_TEXT, "");
		chatMessage.addExtra(finalPoint);
		chatMessage.sendToPlayer(player);
		return;
	}

}
