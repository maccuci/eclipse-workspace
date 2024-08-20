package com.fuzion.hg.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.listener.DeathListener;
import com.fuzion.hg.manager.kit.Kit;

public class TankKit extends Kit {
	
	public TankKit() {
		super("Tank", "Tanker", new ItemStack(Material.TNT), Group.BETA, "§7Quando chegar a sua hora de morrer, exploda tudo em sua volta.");
	}
	
	@EventHandler
	public void tankDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (!hasKit(p)) {
			return;
		}
		if ((e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION) || (e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)) {
			e.setCancelled(true);
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void tankEvent(final PlayerDeathEvent e) {
		if (!(e.getEntity().getKiller() instanceof Player)) {
			return;
		}
		if (e.getEntity().getKiller() == null) {
			return;
		}
		if (!hasKit(e.getEntity().getKiller())) {
			return;
		}
		if ((hasKit(e.getEntity().getKiller())) && ((e.getEntity() instanceof Player)) && ((e.getEntity().getKiller() instanceof Player))) {
			e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 2.0F);
		}
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			public void run() {
				DeathListener.dropItems(e.getDrops(), e.getEntity().getKiller().getLocation());
			}
		}, 20L);
	}

}
