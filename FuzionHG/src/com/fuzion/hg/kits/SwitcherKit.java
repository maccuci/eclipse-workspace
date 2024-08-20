package com.fuzion.hg.kits;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.kit.Kit;

public class SwitcherKit extends Kit {
	
	public SwitcherKit() {
		super("Switcher", "Trap", new ItemStack(Material.SNOW_BALL), Group.BETA, "§7Troque de lugar com quem a bola de neve acertar.");
		addItem(new ItemStack(Material.SNOW_BALL, 16));
	}
	

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();

		if (!hasKit(player))
			return;
		Action a = event.getAction();
		ItemStack item = player.getItemInHand();
		if (!a.name().contains("RIGHT") && !a.name().contains("LEFT"))
			return;
		if (item == null)
			return;
		ItemStack ITEM = getItems().iterator().next().clone();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		player.updateInventory();

		Snowball s = player.launchProjectile(Snowball.class);
		Vector v = player.getLocation().getDirection().normalize().multiply(10);
		s.setVelocity(v);
		s.setMetadata("switcher", new FixedMetadataValue(Main.getPlugin(), true));
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Snowball) {
			Snowball s = (Snowball) event.getDamager();
			if (s.getShooter() instanceof Player) {
				if (s.hasMetadata("switcher")) {
					Player damager = (Player)s.getShooter();
					Player damaged = (Player) event.getEntity();
					
		            Location hitLoc = damaged.getLocation();
		            Location shooterLoc = damager.getLocation();
		            
		            damaged.teleport(shooterLoc);
		            damager.teleport(hitLoc);
				}
			}
		}
	}
}
