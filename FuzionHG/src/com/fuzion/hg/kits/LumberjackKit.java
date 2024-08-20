package com.fuzion.hg.kits;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class LumberjackKit extends Kit {
	
	public LumberjackKit() {
		super("Lumberjack", "Ambientação", new ItemStack(Material.WOOD_AXE), Group.BETA, "§7Quebre apenas uma madeira, e quebre o tronco inteiro.");
		addItem(new ItemStack(Material.WOOD_AXE));
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block b = event.getBlock();
		if (!hasKit(player)) {
			return;
		}
		if ((b.getType() == Material.LOG) || (b.getType() == Material.LOG_2)) {
			World w = (World) Bukkit.getServer().getWorlds().get(0);
			Double d = Double.valueOf(b.getLocation().getY() + 1.0D);
			Location l = new Location(w, b.getLocation().getX(), d.doubleValue(), b.getLocation().getZ());
			while ((l.getBlock().getType() == Material.LOG) || (l.getBlock().getType() == Material.LOG_2)) {
				l.getBlock().breakNaturally();
				d = Double.valueOf(d.doubleValue() + 1.0D);
				l.setY(d.doubleValue());
			}
		}
	}
}
