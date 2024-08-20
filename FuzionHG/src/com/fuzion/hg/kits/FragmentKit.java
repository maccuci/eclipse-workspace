package com.fuzion.hg.kits;

import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class FragmentKit extends Kit {
	
	public FragmentKit() {
		super("Fragment", "Estratégico", new ItemStack(Material.GOLD_NUGGET), Group.BETA, "§7Ao quebrar a grama, tenha chance de dropar um gold nugget.");
	}
	
	@EventHandler
	public void onDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		if (hasKit(player)) {
			if (event.getBlock().getType() == Material.LONG_GRASS && event.getBlock().getType() == Material.GRASS && new Random().nextInt(4) == 0) {
				Location loc = event.getBlock().getLocation().clone();
				loc.getWorld().dropItemNaturally(loc.add(0.5, 0, 0.5), new ItemStack(Material.GOLD_NUGGET, new Random().nextInt(4)));
			}
		}
	}
}
