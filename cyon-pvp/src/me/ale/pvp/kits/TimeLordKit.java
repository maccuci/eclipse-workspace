package me.ale.pvp.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class TimeLordKit extends Kit {
	
	private List<Player> freeze = new ArrayList<>();
	
	public TimeLordKit() {
		super("TimeLord", Material.WATCH, new ItemStack(Material.WATCH), KitRarity.MYSTIC, Rank.SIMPLE, "§7Pare o tempo ao seu reodr de 5 blocos.");
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
		drawCircle(player.getLocation(), 5, EnumParticle.REDSTONE, 3);
		for(Entity entity : player.getNearbyEntities(5, 5, 5)) {
			if (entity instanceof LivingEntity) {
				if(entity.getType() == EntityType.PLAYER) {
					Player other = (Player)entity;
					
					freeze.add(other);
					other.sendMessage("§cVocê foi congelado por um TimeLord!");
					Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
						
						@Override
						public void run() {
							freeze.remove(other);
							other.sendMessage("§aVocê agora não está mais congelado.");
						}
					}, 100L);
				}
			}
		}
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 25L));
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(freeze.contains(player)) {
			event.setTo(player.getLocation());
		}
	}
	
	public void particleEffect(Player player) {
		new BukkitRunnable() {
			double t = Math.PI / 4;
			Location loc = player.getLocation();

			public void run() {
				t = t + 0.1 * Math.PI;
				for (double theta = 0; theta <= 2 * Math.PI; theta = theta + Math.PI / 32) {
					double x = t * Math.cos(theta);
					double y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
					double z = t * Math.sin(theta);
					loc.add(x, y, z);
					PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(EnumParticle.FIREWORKS_SPARK, true, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), 0, 0, 0, 0, 1);
					//ParticleEffect.FIREWORKS_SPARK.display(loc, 0, 0, 0, 0, 1);
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
					loc.subtract(x, y, z);

					theta = theta + Math.PI / 64;

					x = t * Math.cos(theta);
					y = 2 * Math.exp(-0.1 * t) * Math.sin(t) + 1.5;
					z = t * Math.sin(theta);
					loc.add(x, y, z);
					PacketPlayOutWorldParticles packett = new PacketPlayOutWorldParticles(EnumParticle.SPELL_WITCH, true, (float)loc.getX(), (float)loc.getY(), (float)loc.getZ(), 0, 0, 0, 0, 1);
					((CraftPlayer) player).getHandle().playerConnection.sendPacket(packett);
					loc.subtract(x, y, z);
				}
				if (t > 20) {
					this.cancel();
				}
			}

		}.runTaskTimer(Main.getPlugin(), 0, 1);
	}
	
    public void drawCircle(Location loc, float radius, EnumParticle particle, int data){
        for(double t = 0; t<50; t+=0.5){
            float x = radius*(float)Math.sin(t);
            float z = radius*(float)Math.cos(t);
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true,(float) loc.getX() + x, (float) loc.getY(),(float) loc.getZ() + z, 0, 0, 0, data, 1);
            for(Player online : Bukkit.getOnlinePlayers()){
                ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
}
