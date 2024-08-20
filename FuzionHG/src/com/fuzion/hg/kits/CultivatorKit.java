package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class CultivatorKit extends Kit {
	
	public CultivatorKit() {
		super("Cultivator", "Ambientação", new ItemStack(Material.SEEDS), Group.BETA, "§7Cultive mudas mais rapidamente.");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onCultiavator(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		if (!hasKit(player)) {
			return;
		}
		if (event.getBlock().getType() == Material.SAPLING) {
			event.getBlock().setType(Material.AIR);

			boolean arvore = event.getBlock().getWorld().generateTree(event.getBlock().getLocation(), TreeType.TREE);
			if (!arvore) {
				event.getBlock().setTypeIdAndData(Material.SAPLING.getId(), event.getBlock().getData(), false);
			}
		} else if (event.getBlock().getType() == Material.CROPS) {
			event.getBlock().setData((byte) 7);
		}
	}
}
