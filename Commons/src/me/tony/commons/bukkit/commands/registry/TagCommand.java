package me.tony.commons.bukkit.commands.registry;

import java.util.ArrayList;
import java.util.Comparator;

import org.bukkit.entity.Player;

import me.tony.commons.bukkit.commands.Command;
import me.tony.commons.bukkit.commands.CommandArgs;
import me.tony.commons.bukkit.commands.CommandLoader.CommandClass;
import me.tony.commons.core.account.Tag;
import me.tony.commons.core.managements.TagCommon;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TagCommand implements CommandClass {
	
	public ArrayList<Tag> getPlayerTags(Player player) {
		ArrayList<Tag> tags = new ArrayList<>();
		for (Tag tag : Tag.values()) {
				tags.add(tag);
		}

		tags.sort(Comparator.comparing(Tag::getOrder));
		return tags;
	}
	
	@Command(name = "tag")
	public void tag(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		
		if(args.length == 0) {
			TextComponent tagsMessage = new TextComponent("§aSuas Tags: ");
			
			for(int i = 0; i < getPlayerTags(player).size(); i++) {
				Tag tag = getPlayerTags(player).get(i);
				tagsMessage.addExtra(i == 0 ? "" : "§f, ");
				tagsMessage.addExtra(buildGroupComponent(tag, player.getName()));
			}
			player.spigot().sendMessage(tagsMessage);
		} else {
			String selectedGroup = args[0];
			for(Tag tag : Tag.values()) {
				if (tag.getGroup().name().equalsIgnoreCase(selectedGroup)) {
					new TagCommon().updateTagCommand(player, tag);
				}
			}
		}
	}
	
	private BaseComponent buildGroupComponent(Tag tag, String playerName) {
		BaseComponent baseComponent = new TextComponent(tag.getTag());
		baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new BaseComponent[] { new TextComponent("§fExemplo: " + (tag.getTag() + playerName)),
						new TextComponent("\n"), new TextComponent("§aClique para selecionar!") }));
		baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tag.name()));
		return baseComponent;
	}
}
