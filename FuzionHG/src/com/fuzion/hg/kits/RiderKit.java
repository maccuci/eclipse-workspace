package com.fuzion.hg.kits;

import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class RiderKit extends Kit {
	
	private HashMap<Player, Horse> horses = new HashMap<>();
	
	public RiderKit() {
		super("Rider", "Invocação", new ItemStack(Material.SADDLE), Group.BETA, "§7Convoque um cavalo para lhe ajudar na partida.");
	}
	
	@EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.SADDLE) {
            if (event.getWhoClicked().getVehicle() != null) {
                if (horses.containsValue(event.getWhoClicked().getVehicle())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (horses.containsValue(event.getEntity())) {
            event.setDroppedExp(0);
            event.getDrops().clear();
            Iterator<Player> itel = horses.keySet().iterator();
            while (itel.hasNext()) {
                if (horses.get(itel.next()) == event.getEntity()) {
                    itel.remove();
                    break;
                }
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
    	if ((event.getAction().name().contains("RIGHT")) && (item != null) && (item.getType() == Material.SADDLE) && (hasKit(p))) {
    		if (horses.containsKey(p)) {
    			Horse horse = (Horse)horses.remove(p);
    			if (!horse.isDead()) {
    				horse.eject();
    				horse.leaveVehicle();
    				horse.remove();
    			}
    		} else {
    			Horse horse = (Horse)p.getWorld().spawnEntity(p.getLocation(), EntityType.HORSE);
    			horses.put(p, horse);
    			horse.setCustomName("Cavalo de " + p.getName());
    			horse.setCustomNameVisible(true);
    			horse.setBreed(false);
    			horse.setTamed(true);
    			horse.setDomestication(horse.getMaxDomestication());
    			horse.getInventory().setSaddle(new ItemStack(Material.SADDLE));
    			horse.setOwner(p);
    			horse.setJumpStrength(1.0D);
    			horse.setMaxHealth(52.0D);
    			horse.setHealth(52.0D);
    			horse.setColor(Horse.Color.WHITE);
    			horse.setPassenger(p);
    		}
    	}
    }

    @EventHandler
    public void onKilled(PlayerDeathEvent event) {
        if (horses.containsKey(event.getEntity().getPlayer())) {
            Horse horse = horses.remove(event.getEntity().getPlayer());
            if (!horse.isDead()) {
                horse.eject();
                horse.leaveVehicle();
                horse.remove();
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event) {
    	if (horses.containsValue(event.getRightClicked())) {
    		for (Player p : horses.keySet()) {
    			if (horses.get(p) == event.getRightClicked()) {
    				if (event.getPlayer() != p) {
    					event.setCancelled(true);
    					break;
    				}
    			}
    		}
    	} 
    }
}
