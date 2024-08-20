package br.com.houldmc.rankup.api.stack;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

public class MobStackListener implements Listener {
	
	@EventHandler(priority = EventPriority.LOW)
	void onEntityDeath(EntityDeathEvent event) {
		if (!(event.getEntity() instanceof LivingEntity)) {
			return; // Not a living entity.
		}
		LivingEntity entity = (LivingEntity) event.getEntity();

		if (entity.getType() != EntityType.PLAYER) {
			MobStackAPI.attemptUnstackOne(entity);
		}
	}
}
