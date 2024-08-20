package me.tony.commons.core.managements;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.entity.Player;

import lombok.Getter;
import me.tony.commons.core.Commons;
import me.tony.commons.core.account.Group;
import me.tony.commons.core.account.Tag;
import me.tony.commons.core.account.consts.TagConst;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TagCommon {
	
	@Getter private static Map<String, TagConst> tags = new HashMap<>();
	private static final HashMap<String, Tag> tagsMaked = new HashMap<>();
	
	private static final HashMap<UUID, Tag> lastTag = new HashMap<>();
	
	public boolean loadTags() {
		try {
			PreparedStatement preparedStatement = Commons.getMySQL().getConnection().prepareStatement("SELECT * FROM `global_tags`;");
			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt(1);
				String name = resultSet.getString(2);
				Group group = Group.valueOf(resultSet.getString(3).toUpperCase());
				
				if(!tags.containsKey(name)) {
					tags.put(name, new TagConst(name, group, id));
				}
			}
			resultSet.close();
			preparedStatement.close();
		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.SEVERE, "Error when the plugin tried to load the tags.", e);
			return false;
		}
		for(Tag tag : Tag.values()) {
			tags.put(tag.getGroup().getName(), new TagConst(tag.getGroup().getName(), tag.getGroup(), tag.getGroup().getId()));
			if (!tagsMaked.containsKey(tag.getGroup().getName())) {
				createTag(tag.getGroup().getName(), tag.getGroup(), tag.getGroup().getId());
			}
		}
		return true;
	}
	
	public boolean createTag(String tagName, Group group, int id) {
		try {
			PreparedStatement insertStatment = Commons.getMySQL().getConnection().prepareStatement("INSERT INTO `global_tags`(`id`, `name`, `group`) VALUES (?, ?, ?);");
			insertStatment.setInt(1, id);
			insertStatment.setString(2, tagName);
			insertStatment.setString(3, group.getName());
			insertStatment.execute();
			insertStatment.close();
			
			if (!tags.containsKey(tagName)) {
				tags.put(tagName, new TagConst(tagName, group, id));
			}
			
			Commons.getCommonLogger().log(Level.INFO, "The tag " + tagName + " was create.");
			return true;
		} catch (Exception e) {
			Commons.getCommonLogger().log(Level.SEVERE, "Error when the plugin tried to create the tag " + tagName + ".", e);
			return false;
		}
	}
	
	public Tag tagCommand(Player account) {
		Tag toReturn = null;
		
		List<Tag> ta = new ArrayList<>();
		for(Tag tag : Tag.values())
				ta.add(tag);
		
		ta.sort(Comparator.comparing(Tag::getOrder));
		
		toReturn = ta.iterator().next();
		return toReturn;
	}
	
	public void updateTag(Player player, String team, String prefix, String suffix) {
		TagUtils tagUtils = new TagUtils();
		
		tagUtils.setNameTag(player.getPlayer().getName(), team, prefix, " " + suffix);
	}
	
	public void updateTag(Player player) {
		Tag tag = lastTag.containsKey(player.getUniqueId()) ? lastTag.get(player.getUniqueId()) : tagCommand(player);
		if (tag.getGroup().getName() == null || player == null) {
			player.getPlayer().kickPlayer("§cAlgo ocorreu e sua conta não foi carregada.");
		}
		
		player.setDisplayName(tag.getTag() + player.getName());
		player.setPlayerListName(tag.getTag() + player.getName());
		//updateTag(player, "A" + tag.getGroup().getId(), tag.getGroup().getName(), " ");
	}
	
	public void updateTagCommand(Player player, Tag tag) {
		try {
			if(tag.getGroup().getName() == null || player == null) {
				player.getPlayer().kickPlayer("§cAlgo ocorreu e sua conta não foi carregada.");
			}
			
			updateTag(player);
		} catch (Exception e) {
		}
	}
	
	public TagConst getTag(int id) {
		for (TagConst tag : tags.values())
			if (tag.getId() == id)
				return tag;
		return null;
	}
	
	public ArrayList<Tag> getPlayerTags(Player player) {
		ArrayList<Tag> tags = new ArrayList<>();
		for (Tag tag : Tag.values()) {
				tags.add(tag);
		}

		tags.sort(Comparator.comparing(Tag::getOrder));
		return tags;
	}
	
	public void listTags(Player player) {
		TextComponent tagsMessage = new TextComponent("§aSuas Tags: ");
		
		for(int i = 0; i < getPlayerTags(player).size(); i++) {
			Tag tag = getPlayerTags(player).get(i);
			tagsMessage.addExtra(i == 0 ? "" : "§f, ");
			tagsMessage.addExtra(buildGroupComponent(tag, player.getName()));
		}
		player.spigot().sendMessage(tagsMessage);
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
