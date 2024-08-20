package me.ale.commons.core.account.manager;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.Tag;

public class TagManager {
	
	public static HashMap<Player, Tag> playerTag = new HashMap<>();
	private Player player;
	
	public TagManager(Player player) {
		this.player = player;
	}
	
	public void setTag(Tag tag) {
		playerTag.put(this.player, tag);
	}
	
	public Tag getTag() {
		return playerTag.get(this.player);
	}
	
	public boolean hasTag(Tag tag) {
		return getTag().equals(tag);
	}
	
	public boolean hasTagPermission(Tag tag) {
		RankManager manager = BukkitMain.getPlugin().getRankManager();
		return manager.hasGroupPermission(player.getUniqueId(), tag.getRank());
	}
	
	public void findTag() {
		RankManager manager = BukkitMain.getPlugin().getRankManager();
		setTag(Tag.valueOf(manager.getRank(player.getUniqueId()).getTag().name()));
	}
	
	public void updateAll() {
		Bukkit.getOnlinePlayers().forEach(players -> {
			update(players);
		});
	}
	
	public void update(Player player) {
		RankManager manager = BukkitMain.getPlugin().getRankManager();
		player.setPlayerListName(manager.getRank(player.getUniqueId()).getTag().getColor() + player.getName());
		player.setCustomName(manager.getRank(player.getUniqueId()).getTag().getColor() + player.getName());
	}

}
