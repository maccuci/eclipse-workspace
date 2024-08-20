package me.ale.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class StormKit extends Kit {
	
	private ArrayList<Player> storm = new ArrayList<>();
	
	public StormKit() {
		super("Storm", Material.CAULDRON_ITEM, new ItemStack(Material.CAULDRON_ITEM), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Tenha o poder de um deus de criar", "§7um tornado no mapa.");
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if (!hasKit(player))
			return;
		Action a = event.getAction();
		ItemStack item = player.getItemInHand();
		if (!a.name().contains("RIGHT") && !a.name().contains("LEFT"))
			return;
		if (item == null)
			return;
		ItemStack ITEM = getItem();
		if (!ItemBuilder.isEquals(item, ITEM))
			return;
		if (a.name().contains("RIGHT")) {
			event.setCancelled(true);
		}
		item.setDurability(ITEM.getDurability());
		player.updateInventory();
		if (CooldownAPI.hasCooldown(player, getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
			return;
		}
		
		event.setCancelled(true);
		storm.add(player);
		storm(player.getLocation(), 0F, 0.005F);
		
		 Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable()
	      {
	        public void run()
	        {
	        	storm.remove(player);
	        }
	      }, 100L);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(storm.contains(player)) {
			for(Entity entity : player.getNearbyEntities(5, 3, 5)) {
				if((entity instanceof Player)) {
					Player other = (Player)entity;
					player.getWorld().playEffect(player.getLocation(), Effect.SMOKE, 100, 1);
					player.getWorld().playEffect(player.getLocation(), Effect.PARTICLE_SMOKE, 100, 1);
					player.getWorld().playEffect(player.getLocation(), Effect.EXPLOSION, 100, 1);
					other.setVelocity(new Vector(2.0D, 0.3D, 2.0D));
				}
			}
		}
	}
	
	private void storm(Location location, float radius, float increase) {
		float y = (float) location.getY();
		for(double t = 0; t < 50; t += 0.05) {
			float x = radius * (float)Math.sin(t);
			float z = radius * (float)Math.cos(t);
			PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.TOWN_AURA, true, (float)location.getX() + x, y, (float)location.getZ() + z, 0, 0, 0, 0, 1);
			Bukkit.getOnlinePlayers().forEach(players -> {
				((CraftPlayer) players).getHandle().playerConnection.sendPacket(packet);
			});
			y += 0.01F;
			radius += increase;
		}
	}
}
