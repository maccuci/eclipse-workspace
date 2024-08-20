package me.feenks.hg.game.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import me.feenks.hg.constructor.Kit;

public class KangarooKit extends Kit {

	public KangarooKit() {
		super("Kangaroo", "§7Utilize o seu foguete para ganhar boost.", new ItemStack(Material.FIREWORK), new ArrayList<>());
		addItem(new ItemStack(Material.FIREWORK));
	}

}
