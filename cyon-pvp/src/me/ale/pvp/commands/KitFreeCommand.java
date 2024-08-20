package me.ale.pvp.commands;

import org.bukkit.entity.Player;

import me.ale.commons.CyonAPI;
import me.ale.commons.core.account.Rank;
import me.ale.commons.core.command.Command;
import me.ale.commons.core.command.CommandArgs;
import me.ale.commons.core.command.CommandLoader.CommandClass;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitManager;
import me.ale.pvp.manager.kit.KitUtils;

public class KitFreeCommand implements CommandClass {
	
	@Command(name = "kitfree", aliases = "freekit", rankToUse = Rank.ADMIN)
	public void kitfree(CommandArgs cmdArgs) {
		Player player = cmdArgs.getPlayer();
		String[] args = cmdArgs.getArgs();
		
		if(args.length <= 0) {
			player.sendMessage(CyonAPI.WARNING_PREFIX + "Use: /" + cmdArgs.getLabel() + " <add/remove/clear> <kitName>");
			return;
		}
		
		if(args.length == 1) {
			if(args[0].equalsIgnoreCase("clear")) {
				KitUtils.clearFreeKit();
				player.sendMessage("§aVocê removeu todos os kits free na config.");
			}
		}
		
		if(args.length == 2) {
			if(args[0].equalsIgnoreCase("add")) {
				String kitName = args[1];
				Kit kit = KitManager.getKit(kitName.toUpperCase());
				
				if(kit == null) {
					player.sendMessage("§cEste kit não existe.");
					return;
				}
				
				if(KitUtils.isKitFree(kit.getName())) {
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Este kit já está como kit free.");
					return;
				}
				
				KitUtils.addKitFree(kit.getName());
				player.sendMessage("§aVocê adicionou o kit " + kit.getName() + " como kit free.");
			}
			
			if(args[0].equalsIgnoreCase("remove")) {
				String kitName = args[1];
				Kit kit = KitManager.getKit(kitName.toUpperCase());
				
				if(kit == null) {
					player.sendMessage("§cEste kit não existe.");
					return;
				}
				
				if(!KitUtils.isKitFree(kit.getName())) {
					player.sendMessage(CyonAPI.WARNING_PREFIX + "Este kit não está como kit free. Se quiser adiciona-lo basta digitar: /" + cmdArgs.getLabel() + " add " + kit.getName());
					return;
				}
				
				KitUtils.removeKitFree(kit.getName());
				player.sendMessage("§aVocê removeu o kit " + kit.getName() + " como kit free.");
			}
		}
	}

}
