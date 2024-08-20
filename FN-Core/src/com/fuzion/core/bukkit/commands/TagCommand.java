package com.fuzion.core.bukkit.commands;

import java.util.ArrayList;
import java.util.Comparator;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fuzion.core.bukkit.player.tag.Tag;
import com.fuzion.core.bukkit.player.tag.TagManager;
import com.fuzion.core.master.base.HandlerCommand;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TagCommand extends HandlerCommand {
	
	public TagCommand() {
		super("tag", "Player alterate your prefix in the server.");
	}
	
	public ArrayList<Tag> getPlayerTags(Player player) {
		ArrayList<Tag> tags = new ArrayList<>();
		for (Tag tag : Tag.values()) {
			if (new TagManager().hasTagPermission(player, tag)) {
				tags.add(tag);
			}
		}

		tags.sort(Comparator.comparing(Tag::ordinal).reversed());
		return tags;
	}

	@Override
	public boolean execute(CommandSender commandSender, String label, String[] args) {
		if (!isPlayer(commandSender)) {
			return false;
		}

		Player player = (Player) commandSender;
		
		if (args.length == 0) {
			TextComponent tagsMessage = new TextComponent("§6Suas tags: ");
			for (int i = 0; i < getPlayerTags(player).size(); i++) {
				Tag tag = getPlayerTags(player).get(i);
				tagsMessage.addExtra(i == 0 ? "" : "§f, ");
				tagsMessage.addExtra(buildGroupComponent(tag, player.getName()));
			}
			player.spigot().sendMessage(tagsMessage);
		} else {
			String selectedGroup = args[0];
			for (Tag tag : Tag.values()) {
				if (tag.name().equalsIgnoreCase(selectedGroup)) {
					if (new TagManager().hasTagPermission(player, tag)) {
						player.sendMessage("§cVocê não possui a tag " + tag.getColor() + "§l"
								+ selectedGroup.toUpperCase());
						return true;
					}

					player.sendMessage("§aVocê agora está utilizando a tag " + tag.getColor() + "§l"
							+ tag.name().toUpperCase());
						new TagManager().update(player);
					return true;
				}
			}
			player.sendMessage("§9§lTAGS §fTag não existente!");
		}

		return true;
	}

	private BaseComponent buildGroupComponent(Tag tag, String playerName) {
		BaseComponent baseComponent = new TextComponent(tag.getColor() + tag.name());
		baseComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
				new BaseComponent[] { new TextComponent("§fExemplo: " + (tag.getPrefix() + playerName)),
						new TextComponent("\n"), new TextComponent("§aClique para selecionar!") }));
		baseComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tag.name()));
		return baseComponent;
	}

}
