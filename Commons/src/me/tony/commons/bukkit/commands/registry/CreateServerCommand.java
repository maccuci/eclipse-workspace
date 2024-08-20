package me.tony.commons.bukkit.commands.registry;

import org.bukkit.entity.Player;

import me.tony.commons.bukkit.api.TonyMCAPI;
import me.tony.commons.bukkit.commands.Command;
import me.tony.commons.bukkit.commands.CommandArgs;
import me.tony.commons.bukkit.commands.CommandLoader.CommandClass;
import me.tony.commons.core.data.objects.Server;
import me.tony.commons.core.data.server.ServerData;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CreateServerCommand implements CommandClass {
	
	@Command(name = "createserver", aliases = "servercreate")
	public void createserver(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		ServerData serverData = new ServerData();
		
		if(args.length <= 1) {
			player.sendMessage("§cUse: /" + commandArgs.getLabel() + " <id> <name> [motd] <players> <online>");
			return;
		}
		
		if(args.length == 4) {
			int id = 0;
			try {
				id = Integer.parseInt(args[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			String name = args[1];
			int players = 0;
			try {
				players = Integer.parseInt(args[2]);
			} catch (Exception e) {
			}
			int online = 0;
			try {
				online = Integer.parseInt(args[2]);
			} catch (Exception e) {
			}
			
			if(name == null)
				return;
			if(id == 0)
				return;
			
			serverData.createServer(id, name, players, TonyMCAPI.getBoolean(online));
			TextComponent textComponent = new TextComponent("Servidor criado! Passe o mouse para mais informações.");
			textComponent.addExtra(buildServerInfo(serverData.getServer(id)));
		}
	}
	
	private BaseComponent buildServerInfo(Server server) {
		BaseComponent baseComponent = new TextComponent(server.getName());
		baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new BaseComponent[] { new TextComponent("§fNome: §a" + server.getName()),
				new TextComponent("§fID: §a" + server.getId()), new TextComponent("§fJogadores: §a" + server.getPlayers()),
				new TextComponent("\n"), new TextComponent("§fOnline: §a" + server.isOnline())}));
		return baseComponent;
	}
}
