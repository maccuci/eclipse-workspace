package me.tony.commons.bukkit.listener;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import me.tony.commons.bukkit.event.SchedulerEvent;
import me.tony.commons.bukkit.event.SchedulerEvent.SchedulerType;

public class AntiAfkListener implements Listener {

	private Map<Player, Location> locations = new HashMap<>();
	private Map<Player, Long> afkMap = new HashMap<>();

	public long getTime(Player player) {
		return System.currentTimeMillis() - afkMap.getOrDefault(player, System.currentTimeMillis());
	}

	public void resetTimer(Player player) {
		afkMap.put(player, System.currentTimeMillis());
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onUpdate(SchedulerEvent event) {
		if (event.getType() != SchedulerType.SECOND)
			return;
		for (Player players : Bukkit.getOnlinePlayers()) {
/*			CommonsPlayer player = Commons.getAccount(players.getUniqueId());
			if (player == null)
				return;*/
			Location loc = locations.get(players);
			if (loc == null) {
				locations.put(players, players.getLocation().clone());
				continue;
			}
			Location l = players.getLocation();
			if (loc.getX() != l.getX() || loc.getY() != l.getY() || loc.getZ() != l.getZ()) {
				resetTimer(players);
			}

			long time = getTime(players);

			if (time >= 1000 * 60 * 1) {
				players.kickPlayer("§cVocê foi kickado do servidor pois\n§cestava AFK no mesmo!");
			}

			locations.put(players, players.getLocation().clone());
		}
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onLeave(PlayerQuitEvent event) {
		locations.remove(event.getPlayer());
		afkMap.remove(event.getPlayer());
	}
}
