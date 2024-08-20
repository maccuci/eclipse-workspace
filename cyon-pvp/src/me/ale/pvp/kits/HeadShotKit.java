package me.ale.pvp.kits;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftSnowball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;

public class HeadShotKit extends Kit {
	
	private HashMap<Player, Integer> headshotCount = new HashMap<>();
	
	public HeadShotKit() {
		super("HeadShot", new ItemBuilder().type(Material.SKULL_ITEM).durability(3).build().getType(), null, KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Mire na cabeça dos jogadores para", "§7dar mais dano neles.");
	}
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player))
			return;
		
		Player player = (Player)event.getDamager();
		
		if(!hasKit(player))
			return;
		
		Location playerLocation = player.getLocation().clone().add(0, 1.35, 0);
		Vector direction = playerLocation.getDirection().normalize();
		direction.multiply(-1.5);
		playerLocation.subtract(direction);
		
		Entity snowball = player.getWorld().spawnEntity(player.getLocation().clone().add(0, 1, 0), EntityType.SNOWBALL);
		
		Bukkit.getOnlinePlayers().forEach(players -> {
			((CraftPlayer) players).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityDestroy(((CraftSnowball) snowball).getHandle().getId()));
		});
		
		snowball.teleport(playerLocation);
		snowball.setVelocity(player.getLocation().getDirection().multiply(500));
		snowball.setVelocity(new Vector(snowball.getVelocity().getX(), snowball.getVelocity().getY(), snowball.getVelocity().getZ()));
		
		Entity damaged = event.getEntity();

		double y = snowball.getLocation().getY();
		double snowballY = damaged.getLocation().getY();
		boolean headshot = y - snowballY > 1.20d;
		
		if (headshot) {
			if(headshotCount.containsKey(player))
				headshotCount.put(player, headshotCount.get(player) + 1);
			else 
				headshotCount.put(player, 1);
			
			if(headshotCount.get(player) >= 3) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 3 * 20, 2));
				player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 3 * 20, 1));
				damaged.getWorld().playEffect(damaged.getLocation().clone().add(0, 1, 0), Effect.STEP_SOUND, Material.REDSTONE_WIRE);
			}
		} else {
			if(headshotCount.containsKey(player))
				headshotCount.remove(player);
		}
		
		if(event.getDamager().equals(snowball)) {
			event.setCancelled(true);
		}
	}

}
