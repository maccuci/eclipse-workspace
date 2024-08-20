package com.fuzion.hg.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.kit.Kit;

public class AnchorKit extends Kit {
	
	public AnchorKit() {
		super("Anchor", "Contra times", new ItemStack(Material.ANVIL), Group.BETA, "§7Tenha um peso de uma bigorna.");
	}
	
	@EventHandler
	public void onDamager(EntityDamageByEntityEvent event) {
		if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player))) {
			Player player = (Player) event.getEntity();
			Player d = (Player) event.getDamager();
			
			if(!hasKit(d) || !hasKit(player))
				return;
			player.setVelocity(new Vector());
			d.playSound(d.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
				public void run() {
					player.setVelocity(new Vector());
				}
			});
		}
	}
}
