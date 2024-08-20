package com.fuzion.hg.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.game.GameManager;
import com.fuzion.hg.manager.kit.Kit;

public class FishermanKit extends Kit {
	
	public FishermanKit() {
		super("Fisherman", "Trap", new ItemStack(Material.FISHING_ROD), Group.BETA, "§7Puxe jogadores até você com a sua vara de pescar.");
		addItem(new ItemStack(Material.FISHING_ROD, 1));
	}
	
	@EventHandler
	public void onFisherman(PlayerFishEvent event) {
		Player player = event.getPlayer();
		if (!hasKit(player)) {
			return;
		}
		event.getPlayer().getItemInHand().setDurability((short) 0);
		if (GameManager.isInvincibility()) {
			player.sendMessage("§cVocê não pode usar na invencibilidade.");
			return;
		}
		if (event.getState() == PlayerFishEvent.State.CAUGHT_ENTITY) {
			Entity c = event.getCaught();
			World w = player.getLocation().getWorld();
			double x = player.getLocation().getBlockX() + 0.5D;
			double y = player.getLocation().getBlockY();
			double z = player.getLocation().getBlockZ() + 0.5D;
			float yaw = c.getLocation().getYaw();
			float pitch = c.getLocation().getPitch();
			Location loc = new Location(w, x, y, z, yaw, pitch);
			c.teleport(loc);
		}
	}
}
