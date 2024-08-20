package me.feenks.pvp.abilities;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.feenks.pvp.manager.ability.Ability;

public class KangarooAbility extends Ability {

	public KangarooAbility() {
		super("Kangaroo", Material.FIREWORK, "§7Use seu foguete para obter boost.", new ItemStack(Material.FIREWORK));
	}

}
