package me.tony.he4rt.cosmetics.list.type;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import lombok.Getter;
import me.tony.he4rt.cosmetics.list.Category;
import me.tony.he4rt.cosmetics.list.Cosmetic;

public class GadgetType extends Cosmetic implements Listener {

	@Getter private String type;
	@Getter private Material icon;
	private boolean equiped, affectPlayers;
	
	public GadgetType(Player owner, String type, Material icon, String... description) {
		super(owner, type, icon, Category.GADGETS, description);
		this.type = type;
	}
	
	public boolean isEquiped() {
		return equiped == true;
	}
	
	public boolean isAffectPlayers() {
		return affectPlayers == true;
	}
}
