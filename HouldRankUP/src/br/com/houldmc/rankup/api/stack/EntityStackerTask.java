package br.com.houldmc.rankup.api.stack;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class EntityStackerTask extends BukkitRunnable {
	
	@Override
	public void run() {
		final int radius = 1;

		// Iterate through all worlds
		for (World world : Bukkit.getServer().getWorlds()) {
			// Iterate through all entities in this world
			for (LivingEntity entity : world.getLivingEntities()) {
				// Don't waste precious CPU cycles on entities that don't stack or are invalid
				if (!MobStackAPI.getMobsType().contains(entity.getType()) || !entity.isValid()) {
					continue;
				}

				// Iterate through all entities in range
				for (Entity nearby : entity.getNearbyEntities(radius, radius, radius)) {
					if (!(nearby instanceof LivingEntity) || !nearby.isValid()) {
						continue; // Not a living entity or is invalid.
					}
					if (!MobStackAPI.getMobsType().contains(nearby.getType())) {
						continue; // This entity type is not enabled to stack.
					}
					MobStackAPI.stack(entity, (LivingEntity) nearby);
				}
			}
		}
	}

}
