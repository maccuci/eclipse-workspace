package me.ale.pvp.kits;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class NinjaKit extends Kit {
	
	private HashMap<Player, Player> ninja = new HashMap<>();
	
	public NinjaKit() {
		super("Ninja", Material.NETHER_STAR, null, KitRarity.MYSTIC, Rank.SIMPLE, "§7Teleporte-se até o último jogador hitado.");
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (((event.getEntity() instanceof Player)) && ((event.getDamager() instanceof Player))) {
			Player damager = (Player)event.getDamager();
			Player damaged = (Player)event.getEntity();
			
			if(event.getDamage() < 1.0) {
				return;
			}
			
			if(!hasKit(damager))
				return;
			
			ninja.put(damager, damaged);
		}
	}
	
	@EventHandler
	  public void shift(PlayerToggleSneakEvent event)
	  {
	    Player p = event.getPlayer();
	    if (ninja.containsKey(p)) {
	      Player t = (Player)ninja.get(p);
	      if (t.getLocation().distance(p.getLocation()) < 50.0D) {
	        p.teleport(t.getLocation());
	      }
	    }
	  }
}
