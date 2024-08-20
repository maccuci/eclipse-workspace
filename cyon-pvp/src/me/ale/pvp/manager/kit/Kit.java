package me.ale.pvp.manager.kit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.ale.commons.bukkit.BukkitMain;
import me.ale.commons.core.account.Rank;
import me.ale.commons.core.account.manager.RankManager;

@Getter
public abstract class Kit implements Listener {
	
	private String name;
	private Material icon;
	private ItemStack item;
	private KitRarity rarity;
	private Rank rank;
	private String[] lore;
	
	public Kit(String name, Material icon, ItemStack item, KitRarity rarity, Rank rank, String... lore) {
		this.name = name;
		this.icon = icon;
		this.item = item;
		this.rarity = rarity;
		this.rank = rank;
		this.lore = lore;
	}
	
	public Kit(String name, ItemStack icon, ItemStack item, KitRarity rarity, Rank rank, String... lore) {
		this.name = name;
		this.icon = item.getType();
		this.item = item;
		this.rarity = rarity;
		this.rank = rank;
		this.lore = lore;
	}
	
	public boolean canUseKit(Player player) {
		RankManager manager = BukkitMain.getPlugin().getRankManager();
		return manager.hasGroupPermission(player.getUniqueId(), rank) || player.hasPermission("kit." + name);
	}
	
	public boolean hasKit(Player player) {
		return new KitManager().getKitName(player.getUniqueId()) == getName();
	}
}
