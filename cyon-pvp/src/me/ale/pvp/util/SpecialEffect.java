package me.ale.pvp.util;

import org.bukkit.Effect;
import org.bukkit.entity.Player;

public class SpecialEffect {

	public static void priestSuperThrow(Player player) {
		for (int i = 0; i <= 1; i++) {
			player.getWorld().playEffect(player.getLocation(), Effect.MOBSPAWNER_FLAMES, i);
			player.getWorld().playEffect(player.getEyeLocation(), Effect.MOBSPAWNER_FLAMES, i);
		}
		for (int i = 0; i <= 10; i++) {
			player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, i);
			player.getWorld().playEffect(player.getEyeLocation(), Effect.SMOKE, i);
		}
	}

}
