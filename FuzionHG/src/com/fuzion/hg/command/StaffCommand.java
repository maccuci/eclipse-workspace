package com.fuzion.hg.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.core.master.account.management.GroupManager;
import com.fuzion.core.master.account.management.PunishManager.Durations;
import com.fuzion.core.master.commands.Command;
import com.fuzion.core.master.commands.CommandArgs;
import com.fuzion.core.master.commands.CommandLoader.CommandClass;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.game.GameStage;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;
import com.fuzion.hg.manager.kit.SimpleKit;
import com.fuzion.hg.manager.timer.GameTimer;
import com.fuzion.hg.manager.timer.InvincibilityTimer;
import com.fuzion.hg.manager.timer.PreGameTimer;

public class StaffCommand implements CommandClass {
	
	@SuppressWarnings("deprecation")
	@Command(name = "givekit", groupToUse = Group.MOD)
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
				if(new KitManager().hasSQLKit(offlinePlayer.getUniqueId(), kitName)) {
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
			if(new KitManager().hasSQLKit(target.getUniqueId(), kitName)) {
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
				if(new KitManager().hasSQLKit(offlinePlayer.getUniqueId(), kitName)) {
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
				new KitManager().giveSQLKit(offlinePlayer.getUniqueId(), offlinePlayer, kitName, Durations.PERMANENT, true, time);
				player.sendMessage("§eVocê deu o kit §c" + kit.getName() + " §epara o jogador §f" + offlinePlayer.getName());
				return;
			}
			if(new KitManager().hasSQLKit(target.getUniqueId(), kitName)) {
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
			new KitManager().giveSQLKit(target.getUniqueId(), target, kitName, Durations.PERMANENT, true, time);
			player.sendMessage("§eVocê deu o kit §c" + kit.getName() + " §epara o jogador §f" + target.getName());
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
	
	@Command(name = "start", aliases = "iniciar", groupToUse = Group.YOUTUBERPLUS)
	public void start(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length >= 0) {
			player.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
			GameManager.startGame();
			return;
		}
	}
	
	@Command(name = "dano", groupToUse = Group.MOD)
	public void dano(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /dano <on/off>");
			return;
		}
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("on")) {
				if(GameManager.DAMAGE) {
					player.sendMessage("§cO dano já está ativado.");
					return;
				} else {
					GameManager.DAMAGE = true;
					Bukkit.broadcastMessage("§aDANO do servidor foi ativado.");
				}
			} else if(args[0].equalsIgnoreCase("off")) {
				if(!GameManager.DAMAGE) {
					player.sendMessage("§cO dano já está desativado.");
					return;
				} else {
					GameManager.DAMAGE = false;
					Bukkit.broadcastMessage("§cDANO do servidor foi desativado.");
				}
			}
		}
	}
	
	@Command(name = "pvp", groupToUse = Group.MOD)
	public void pvp(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /pvp <on/off>");
			return;
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("on")) {
				if(Bukkit.getWorlds().get(0).getPVP() == true) {
					player.sendMessage("§cO PvP já se encontra ativado.");
					return;
				}
				Bukkit.getWorlds().get(0).setPVP(true);
				Bukkit.broadcastMessage("§aPVP do servidor foi ativado.");
			} else if(args[0].equalsIgnoreCase("off")) {
				if(Bukkit.getWorlds().get(0).getPVP() == false) {
					player.sendMessage("§cO PvP já se encontra desativado.");
					return;
				}
				Bukkit.getWorlds().get(0).setPVP(false);
				Bukkit.broadcastMessage("§cPVP do servidor foi desativado.");
			}
		}
	}
	
	@Command(name = "tempo", groupToUse = Group.MOD)
	public void time(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /tempo <value>");
			return;
		}
		if(args.length == 1) {
			Integer value;
			try {
				value = Integer.valueOf(args[0]);
			} catch (Exception e) {
				player.sendMessage("§cInsira um valor numérico.");
				return;
			}
			if(GameManager.getStage() == GameStage.WAITING) {
				PreGameTimer.timer = value;
				Bukkit.broadcastMessage("§eO tempo da partida foi alterado para " + DateUtils.formatDifference(value) + " no estágio §9§l" + GameManager.translateStageName(GameManager.getStage()));
			} else if(GameManager.getStage() == GameStage.INVINCIBILITY) {
				InvincibilityTimer.timer = value;
				Bukkit.broadcastMessage("§eO tempo da partida foi alterado para " + DateUtils.formatDifference(value) + " no estágio §9§l" + GameManager.translateStageName(GameManager.getStage()));
			} else if(GameManager.getStage() == GameStage.GAME) {
				GameTimer.timer = value;
				Bukkit.broadcastMessage("§eO tempo da partida foi alterado para " + DateUtils.formatDifference(value) + " no estágio §9§l" + GameManager.translateStageName(GameManager.getStage()));
			}
		}
	}
	
	@Command(name = "kitinfo", aliases = "infokit")
	public void kitinfo(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<String> kitsName = new ArrayList<>();
		
		if(args.length == 0) {
			player.sendMessage("§cUse: /kitinfo <kitName>");
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
			for(int i = 0; i < kits.size(); i++) {
				Kit k = kits.get(i);
				kitsName.add(k.getName().toUpperCase());
			}
			
			if(!kitsName.contains(kitName.toUpperCase())) {
				player.sendMessage("§cEste kit não existe.");
				return;
			}
			
			player.sendMessage("§aInformações sobre o kit §f" + kit.getName());
			player.sendMessage(" ");
			player.sendMessage("§aTipo: §f" + kit.getType() + ".");
			player.sendMessage("§aDescrição: §f" + kit.getExplanation());
		}
	}
	
	@SuppressWarnings("deprecation")
	@Command(name = "skit", groupToUse = Group.MOD)
	public void skit(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /skit <create/apply/remove> <skitName> [player/all]");
			return;
		}
		if(args.length == 2) {
			String kit = args[1];
			if (args[0].equalsIgnoreCase("create")) {
				SimpleKit.addKit(player, kit, new SimpleKit(player));
				return;
			}
			if (args[0].equalsIgnoreCase("apply")) {
				SimpleKit.applyKit(player, kit, null);
				return;
			}
			if (args[0].equalsIgnoreCase("remove")) {
				SimpleKit.removeKit(player, kit);
				return;
			}
		}
		if (args.length == 3) {
			Player target = Bukkit.getPlayer(args[2]);
			if (target == null) {
				player.sendMessage("Player offline.");
				return;
			}
			if (args[0].equalsIgnoreCase("apply")) {
				String kit = args[1];
				SimpleKit.applyKit(player, kit, target);
				return;
			}
			if (args[3].equalsIgnoreCase("all")) {
				for(Player online : Bukkit.getOnlinePlayers()) {
					if(AdminAPI.admin.contains(online.getUniqueId()))
						break;
					String kit = args[1];
					SimpleKit.applyKit(player, kit, online);
				}
			}
		}
	}
}
