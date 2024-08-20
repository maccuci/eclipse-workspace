package com.fuzion.kitpvp.kits;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.kitpvp.manager.kit.Kit;

import com.fuzion.core.master.account.Group;

public class FishermanKit extends Kit {
	
	public FishermanKit() {
		super("Fisherman", new ItemStack(Material.FISHING_ROD), Group.BETA, "§7Puxe os inimigos até você com a sua vara de pesca.");
		addItem(new ItemStack(Material.FISHING_ROD));
	}
	
	@EventHandler
	public void onFish(PlayerFishEvent event) {
		Player player = (Player)event.getCaught();
		
		if(!hasKit(player))
			return;
		
		if (event.getCaught() != null) {
			Entity entity = event.getCaught();
			if ((entity instanceof Player)) {
				 Player caught = ((Player)entity).getPlayer();
				 caught.teleport(player);
			}
		}
	}
}
