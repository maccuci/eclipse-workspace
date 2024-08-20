package me.ale.pvp.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;

public class ZeusKit extends Kit {
	
	 private static List<Player> zeus = new ArrayList<>();
	
	public ZeusKit() {
		super("Zeus", Material.BOW, new ItemStack(Material.BOW), KitRarity.MYSTIC, Rank.SIMPLE, "§7Acerte uma flecha em um lugar", "§7e invoque um raio dos deuses.");	
	}
	
	@EventHandler
	public void onBow(ProjectileLaunchEvent event) {
		if (((event.getEntity() instanceof Arrow)) && ((event.getEntity().getShooter() instanceof Player))) {
			Player player = (Player)event.getEntity().getShooter();
			if(!zeus.contains(player) || hasKit(player)) {
				zeus.add(player);
			}
		}
	}
	
	@EventHandler
	public void onProjectile(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if (((event.getEntity() instanceof Arrow)) && ((event.getEntity().getShooter() instanceof Player))) {
			Arrow arrow = (Arrow)projectile;
			Player player = (Player)event.getEntity().getShooter();
			
			if(zeus.contains(player)) {
				zeus.remove(player);
				arrow.getWorld().strikeLightning(arrow.getLocation());
				arrow.getWorld().strikeLightning(arrow.getLocation());
				arrow.getWorld().strikeLightning(arrow.getLocation());
				arrow.getWorld().strikeLightning(arrow.getLocation());
			}
		}
	}
}
