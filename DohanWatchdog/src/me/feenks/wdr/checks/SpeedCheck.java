package me.feenks.wdr.checks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import me.feenks.wdr.WDR;
import me.feenks.wdr.manager.Check;
import me.feenks.wdr.util.PlayerUtils;

public class SpeedCheck extends Check {
	
	public SpeedCheck() {
		super("Speed");
	}
	
	@SuppressWarnings("deprecation")
	@EventHandler
	void checkSpeed(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(!player.isSprinting() && player.getWalkSpeed() > 5.0) {
			if(PlayerUtils.getPing(player) > 300)
				return;
			for(Player online : Bukkit.getOnlinePlayers()) {
				online.sendMessage(WDR.PREFIX + "§cO jogador " + player.getName() + " está usando Speed!");
			}
		}
	}
}
