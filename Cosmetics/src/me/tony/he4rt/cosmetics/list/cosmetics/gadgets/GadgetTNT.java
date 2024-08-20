package me.tony.he4rt.cosmetics.list.cosmetics.gadgets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.vehicle.VehicleDestroyEvent;
import org.bukkit.util.Vector;

import me.tony.he4rt.cosmetics.api.TonyMCAPI;
import me.tony.he4rt.cosmetics.list.type.GadgetType;
import me.tony.he4rt.cosmetics.util.MathUtils;

public class GadgetTNT extends GadgetType {

	List<Entity> entities = new ArrayList<>();

	public GadgetTNT(Player owner) {
		super(owner, "TnT", Material.TNT, "§7Joque TnT's pelo mapa.");
	}

	public void onRightClick() {
		TNTPrimed tnt = getOwner().getWorld().spawn(getOwner().getLocation().add(0, 2, 0), TNTPrimed.class);
		// Vector stuff
		tnt.setFuseTicks(20);
		tnt.setVelocity(getOwner().getLocation().getDirection().multiply(0.854321));
		entities.add(tnt);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(TonyMCAPI.checkItem(player.getItemInHand(), "§aTNT §7(Clique)")) {
			onRightClick();
			player.sendMessage("§aVocê usou o gadget §fTNT!");
		}
	}

	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (entities.contains(event.getDamager())) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onItemFrameBreak(HangingBreakEvent event) {
		for (Entity ent : entities) {
			if (ent.getWorld() != event.getEntity().getWorld())
				continue;
			if (ent.getLocation().distance(event.getEntity().getLocation()) < 15)
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onVehicleDestroy(VehicleDestroyEvent event) {
		for (Entity tnt : entities) {
			if (tnt.getWorld() != event.getVehicle().getWorld())
				continue;
			if (tnt.getLocation().distance(event.getVehicle().getLocation()) < 10) {
				event.setCancelled(true);
			}
		}
	}

	@EventHandler
	public void onExplode(ExplosionPrimeEvent event) {
		if (isAffectPlayers()) {
			event.setCancelled(true);
			return;
		}
	}

	@EventHandler
	public void onEntityExplode(EntityExplodeEvent event) {
		if (entities.contains(event.getEntity())) {
			event.setCancelled(true);
			event.getEntity().getLocation().getWorld().createExplosion(event.getEntity().getLocation(), 3F, true);
			getOwner().playSound(getOwner().getLocation(), Sound.EXPLODE, 1.4f, 1.5f);

			for (Entity ent : event.getEntity().getNearbyEntities(3, 3, 3)) {
				if (ent instanceof Creature || ent instanceof Player) {
					double dX = event.getEntity().getLocation().getX() - ent.getLocation().getX();
					double dY = event.getEntity().getLocation().getY() - ent.getLocation().getY();
					double dZ = event.getEntity().getLocation().getZ() - ent.getLocation().getZ();
					double yaw = Math.atan2(dZ, dX);
					double pitch = Math.atan2(Math.sqrt(dZ * dZ + dX * dX), dY) + Math.PI;
					double X = Math.sin(pitch) * Math.cos(yaw);
					double Y = Math.sin(pitch) * Math.sin(yaw);
					double Z = Math.cos(pitch);

					Vector vector = new Vector(X, Z, Y);
					MathUtils.applyVelocity(ent, vector.multiply(1.3D).add(new Vector(0, 1.4D, 0)));
				}
			}
			entities.remove(event.getEntity());
		}
	}
}
