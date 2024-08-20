package me.ale.pvp.kits;

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
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;

public class UnderKit extends Kit {
	
	public UnderKit() {
		super("Under", Material.SANDSTONE, new ItemStack(Material.SANDSTONE), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Deixe todos em sua volta em um raio de 4 blocos", "§7na total escuridão e envenenados.");	
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
		
		drawCircle(player.getLocation(), 4, EnumParticle.SMOKE_NORMAL);
		for(Entity entity : player.getNearbyEntities(4.0, 4.0, 4.0)) {
			if (entity instanceof LivingEntity) {
				if(entity.getType() == EntityType.PLAYER) {
					((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 10 * 20, 4));
					((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10 * 20, 4));
				}
			}
		}
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 40L));
	}
	
    public void drawCircle(Location loc, float radius, EnumParticle particle){
        for(double t = 0; t<50; t+=0.5){
            float x = radius*(float)Math.sin(t);
            float z = radius*(float)Math.cos(t);
            PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(particle, true,(float) loc.getX() + x, (float) loc.getY(),(float) loc.getZ() + z, 0, 0, 0, 0, 1);
            for(Player online : Bukkit.getOnlinePlayers()){
                ((CraftPlayer)online).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }
}
