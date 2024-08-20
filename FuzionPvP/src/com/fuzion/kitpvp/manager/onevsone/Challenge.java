package com.fuzion.kitpvp.manager.onevsone;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.fuzion.kitpvp.manager.onevsone.Warp1v1.ChallengeType;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Challenge {
	
	@Getter
	@NonNull
	private Player challenger, challenged;
	@Getter
	@NonNull
	private ItemStack sword, helmet, chestplate, leggins, boots, soup, mushroom;
	@Getter
	private boolean recraft, refil, strenght;
	
	@Getter
	private ChallengeType type;
	
	public Challenge(Player challenger, Player challenged) {
		this.challenger = challenger;
		this.challenged = challenged;
		this.type = ChallengeType.DEFAULT;
		this.sword = new ItemStack(Material.DIAMOND_SWORD);
		this.helmet = new ItemStack(Material.IRON_HELMET);
		this.chestplate = new ItemStack(Material.IRON_CHESTPLATE);
		this.leggins = new ItemStack(Material.IRON_LEGGINGS);
		this.boots = new ItemStack(Material.IRON_BOOTS);
	}
	
	public Challenge(Player challenger, Player challenged, ChallengeType type) {
		this.challenger = challenger;
		this.challenged = challenged;
		this.type = type;
	}
	
	public Challenge(ItemStack sword, ItemStack helmet, ItemStack chestplate, ItemStack leggins, ItemStack boots, ItemStack soup, ItemStack mushroom, boolean refil, boolean recraft, boolean strenght) {
		this.sword = sword;
		this.helmet = helmet;
		this.chestplate = chestplate;
		this.leggins = leggins;
		this.boots = boots;
		this.soup = soup;
		this.mushroom = mushroom;
		this.recraft = false;
		this.refil = false;
		this.strenght = false;
	}
	
	public boolean hasRefil() {
		return refil;
	}
	
	public boolean hasRecraft() {
		return recraft;
	}
	
	public boolean hasStrenght() {
		return strenght;
	}

}
