package com.fuzion.hg.kits;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;

public class KayaKit extends Kit {
	
	public KayaKit() {
		super("Kaya", "Trap", new ItemStack(Material.GRASS), Group.BETA, "§7Coloque blocos falsos de grama que quando alguém pisa no mesmo, desaparecem.");
	}

	private transient HashMap<Block, Player> Blocos = new HashMap<Block, Player>();

	@EventHandler
	public void onBreak(BlockBreakEvent e) {
		if (!e.isCancelled())
			Blocos.remove(e.getBlock());
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void asdads(PrepareItemCraftEvent event) {
		if (event.getRecipe().getResult() != null && event.getRecipe().getResult().getType() == Material.GRASS) {
			for (HumanEntity entity : event.getViewers())
				if (hasKit((Player) entity))
					return;
			event.getInventory().setItem(0, new ItemStack(0, 0));
		}
	}

	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		for (Block b : e.blockList())
			Blocos.remove(b);
	}

	@EventHandler
	public void onPiston(BlockPistonExtendEvent e) {
		for (Block b : e.getBlocks())
			Blocos.remove(b);
	}

	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Block b = e.getBlock();
		if (!e.isCancelled())
			if (e.getBlock().getType() == Material.GRASS && hasKit(p)) {
				Blocos.put(b, p);
			}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		if (!AdminAPI.admin.contains(p.getUniqueId()) && !GameManager.isSpector(p)) {
			Location loc = p.getLocation();
			for (int z = -1; z <= 1; z++) {
				for (int x = -1; x <= 1; x++) {
					for (int y = -1; y <= 2; y++) {
						Block b = loc.clone().add(x, y, z).getBlock();
						if (Blocos.containsKey(b) && b.getType() == Material.GRASS) {
							if (Blocos.get(b) != p) {
								b.setType(Material.AIR);
								Blocos.remove(b);
							}
						}
					}
				}
			}
		}
	}

}
