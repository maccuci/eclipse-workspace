package me.tony.commons.bukkit;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.tony.commons.core.CommonsPlatform;

public class BukkitPlatform implements CommonsPlatform {

	@Override
	public UUID getUUID(String name) {
		Player player = getPlayerExact(name, Player.class);
		return player != null ? player.getUniqueId() : null;
	}

	@Override
	public <T> T getPlayerExact(String name, Class<T> clazz) {
		Player found = null;
		
		if(name != null && !name.isEmpty()) {
			found = Bukkit.getPlayerExact(name);
			if(found == null) {
				for(Player player : Bukkit.getOnlinePlayers()) {
					if(player.getName().equalsIgnoreCase(name)) {
						found = player;
						break;
					}
				}
			}
		}
		return found != null ? clazz.cast(found) : null;
	}
	
	@Override
	public void runAsync(Runnable runnable) {
		Bukkit.getScheduler().runTaskAsynchronously(BukkitMain.getPlugin(), runnable);
	}
	
	@Override
	public <T> T setClassInicializate(Class<T> plugin) {
		BukkitMain bukkitMain = null;
		if(plugin.equals(BukkitMain.class)) {
			try {
				bukkitMain = (BukkitMain) plugin.newInstance();
			} catch (Exception e) {
				System.out.println("This class not equals BukkitMain.");
				return null;
			}
		}
		return bukkitMain != null ? plugin.cast(bukkitMain) : null;
	}
}
