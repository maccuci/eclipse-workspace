package me.ale.hg.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import me.ale.hg.event.player.PlayerSelectKitEvent;
import me.ale.hg.manager.kit.Kit;
import me.ale.hg.manager.kit.KitManager;

public class KitCommand implements CommandExecutor, TabExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = (Player)sender;
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		List<String> kitsName = new ArrayList<>();
		
		if(args.length != 1) {
			player.sendMessage("§cUse: /kit <kitName>");
			return true;
		}
		
		if(args.length == 1) {
			String kitName = args[0];
			Kit kit = null;
			try {
				kit = KitManager.getKit(kitName);
			} catch (Exception e) {
				player.sendMessage("§cEste kit não existe.");
				return true;
			}
			for(int i = 0; i < kits.size(); i++) {
				kit = kits.get(i);
				kitsName.add(kit.getName().toUpperCase());
			}
			
			if(!kitsName.contains(kitName.toUpperCase())) {
				player.sendMessage("Este kit não existe.");
				return true;
			}
			
			PlayerSelectKitEvent event = new PlayerSelectKitEvent(player, kit);
			Bukkit.getPluginManager().callEvent(event);
			
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> list = new ArrayList<>();
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		Player player = (Player)sender;
		
		if(args.length != 1) {
			return null;
		}
		
		if(args.length == 1) {
			String search = args[0].toLowerCase();
			for(int i = 0; i < kits.size(); i++) {
				Kit kit = kits.get(i);
				
				if(player.hasPermission("kit." + kit.getName())) {
					if(kit.getName().toLowerCase().startsWith(search)) {
						list.add(kit.getName().toLowerCase());
					}
				}
			}
		}
		
		return list;
	}
}
