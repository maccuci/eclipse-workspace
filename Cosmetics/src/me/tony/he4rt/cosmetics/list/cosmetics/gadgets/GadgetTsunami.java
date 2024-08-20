package me.tony.he4rt.cosmetics.list.cosmetics.gadgets;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import me.tony.he4rt.cosmetics.Main;
import me.tony.he4rt.cosmetics.api.TonyMCAPI;
import me.tony.he4rt.cosmetics.list.type.GadgetType;
import me.tony.he4rt.cosmetics.util.MathUtils;
import me.tony.he4rt.cosmetics.util.ParticleEffect;
import me.tony.he4rt.cosmetics.util.ParticleEffect.OrdinaryColor;

public class GadgetTsunami extends GadgetType {

	List<Entity> cooldownJump = new ArrayList<>();

	public GadgetTsunami(Player owner) {
		super(owner, "Tsunami", Material.WATER_BUCKET, "§7Crie uma tsunami de partículas.");
	}

	public void onRightClick() {
		final Vector v = getOwner().getLocation().getDirection().normalize().multiply(0.3);
		v.setY(0);
		final Location loc = getOwner().getLocation().subtract(0, 1, 0).add(v);
		final int i = Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getPlugin(), () -> {
			if (loc.getBlock().getType() != Material.AIR && loc.getBlock().getType().isSolid())
				loc.add(0, 1, 0);
			if (loc.clone().subtract(0, 1, 0).getBlock().getType() == Material.AIR)
				loc.add(0, -1, 0);
			Location loc1 = loc.clone().add(MathUtils.randomDouble(-1.5, 1.5), MathUtils.randomDouble(0, .5) - 0.75,
					MathUtils.randomDouble(-1.5, 1.5));
			Location loc2 = loc.clone().add(MathUtils.randomDouble(-1.5, 1.5), MathUtils.randomDouble(1.3, 1.8) - 0.75,
					MathUtils.randomDouble(-1.5, 1.5));
			for (int i1 = 0; i1 < 5; i1++) {
				ParticleEffect.EXPLOSION_NORMAL.display(getOwner(), loc1, 0.2f, 0.2f, 0.2f, 1, 1);
				ParticleEffect.DRIP_WATER.display(getOwner(), loc2, 0.4f, 0.4f, 0.4f, 1, 2);
			}
			for (int a = 0; a < 100; a++)
				ParticleEffect.REDSTONE.display(new OrdinaryColor(0, 0, 255),
						loc.clone().add(MathUtils.randomDouble(-1.5, 1.5), MathUtils.randomDouble(1, 1.6) - 0.75,
								MathUtils.randomDouble(-1.5, 1.5)),
						2);
			if (isAffectPlayers())
				for (final Entity ent : getOwner().getWorld().getNearbyEntities(loc, 0.6, 0.6, 0.6)) {
					if (!cooldownJump.contains(ent) && ent != getOwner() && !(ent instanceof ArmorStand)) {
						MathUtils.applyVelocity(ent, new Vector(0, 1, 0).add(v.clone().multiply(2)));
						cooldownJump.add(ent);
						Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> cooldownJump.remove(ent), 20);
					}
				}
			loc.add(v);
		}, 0, 1).getTaskId();
		Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> Bukkit.getScheduler().cancelTask(i), 40);
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(TonyMCAPI.checkItem(player.getItemInHand(), "§aTsunami §7(Clique)")) {
			event.setCancelled(true);
			onRightClick();
			player.sendMessage("§aVocê usou o gadget §fTsunami!");
		}
	}
}
