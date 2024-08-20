package com.fuzion.hg.kits;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class TurtleKit extends Kit {
	
	public TurtleKit() {
		super("Turtle", "Sem dano", new ItemStack(Material.DIAMOND_CHESTPLATE), Group.BETA, "§7Aperte shift e fique imune à qualquer tipo de damage.");
	}
	
	public static ArrayList<String> damage = new ArrayList<String>();

	@EventHandler
	public void viper(EntityDamageByEntityEvent e) {
		if (e.getDamager() instanceof Player) {
			Player d = (Player) e.getDamager();
			if (hasKit(d)) {
				if (damage.contains(d.getName())) {
					e.setCancelled(true);
					return;
				}
			}
		}
	}

	@EventHandler
	public void De4(PlayerToggleSneakEvent event) {
		Player p = event.getPlayer();
		if (hasKit(p)) {
			if ((!p.isSneaking())) {
				if (!damage.contains(p.getName())) {
					damage.add(p.getName());
				}
			} else {
				if (damage.contains(p.getName())) {
					damage.remove(p.getName());
				}
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntityDamage(EntityDamageEvent e) {
		if (!(e.getEntity() instanceof Player)) {
			return;
		}
		Player p = (Player) e.getEntity();
		if (hasKit(p) && (p.isSneaking())
				&& ((e.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) || (e.getCause() == EntityDamageEvent.DamageCause.CONTACT) || (e.getCause() == EntityDamageEvent.DamageCause.CUSTOM)
						|| (e.getCause() == EntityDamageEvent.DamageCause.DROWNING) || (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) || (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
						|| (e.getCause() == EntityDamageEvent.DamageCause.FALL) || (e.getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK) || (e.getCause() == EntityDamageEvent.DamageCause.FIRE)
						|| (e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) || (e.getCause() == EntityDamageEvent.DamageCause.LAVA) || (e.getCause() == EntityDamageEvent.DamageCause.LIGHTNING)
						|| (e.getCause() == EntityDamageEvent.DamageCause.MAGIC) || (e.getCause() == EntityDamageEvent.DamageCause.MELTING) || (e.getCause() == EntityDamageEvent.DamageCause.POISON)
						|| (e.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) || (e.getCause() == EntityDamageEvent.DamageCause.STARVATION) || (e.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION)
						|| (e.getCause() == EntityDamageEvent.DamageCause.THORNS) || (e.getCause() == EntityDamageEvent.DamageCause.VOID) || (e.getCause() == EntityDamageEvent.DamageCause.WITHER))) {
			e.setDamage(1);
			return;
		}
	}
}
