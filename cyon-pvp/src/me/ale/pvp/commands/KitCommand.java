package me.ale.pvp.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ale.commons.CyonAPI;
import me.ale.commons.core.command.Command;
import me.ale.commons.core.command.CommandArgs;
import me.ale.commons.core.command.Completer;
import me.ale.commons.core.command.CommandLoader.CommandClass;
import me.ale.pvp.event.PlayerSelectKit;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitManager;

public class KitCommand implements CommandClass {
	
	@Command(name = "kit")
	public void kit(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<String> kitsName = new ArrayList<>();
		
		if(args.length != 1) {
			player.sendMessage(CyonAPI.SERVER_PREFIX  + "Use: /kit <name>");
			return;
		}
		if(args.length == 1) {
			Kit kit = null;
			String kitName = args[0];
			try {
				kit = KitManager.getKit(kitName);
			} catch (Exception e) {
				player.sendMessage(CyonAPI.SERVER_PREFIX  + "Este kit não existe.");
				return;
			}
			
			for(int i = 0; i < kits.size(); i++) {
				Kit k = kits.get(i);
				kitsName.add(k.getName().toUpperCase());
			}
			
			if(!kitsName.contains(kitName.toUpperCase())) {
				player.sendMessage(CyonAPI.SERVER_PREFIX  + "Este kit não existe.");
				return;
			}
			
			PlayerSelectKit event = new PlayerSelectKit(player, kit);
			Bukkit.getPluginManager().callEvent(event);
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
}
