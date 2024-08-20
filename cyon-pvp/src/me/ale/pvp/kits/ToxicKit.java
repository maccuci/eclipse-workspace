package me.ale.pvp.kits;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class ToxicKit extends Kit {
	
	private ArrayList<Player> toxics = new ArrayList<>();
	
	public ToxicKit() {
		super("Toxic", Material.SLIME_BALL, new ItemStack(Material.SLIME_BALL), KitRarity.RARE, Rank.SIMPLE, "§7Todos ao seu redor irão ficar entoxicicados.");	
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
		player.getWorld().playSound(player.getLocation(), Sound.BAT_HURT, 5.0F, -5.0F);
		CooldownAPI.addCooldown(player, new Cooldown(getName(), 25L));
		Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable()
	      {
	        public void run()
	        {
	        	toxics.add(player);
	        }
	      }, 0L);
	      Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable()
	      {
	        public void run()
	        {
	        	toxics.remove(player);
	        }
	      }, 100L);
	}
	
	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(toxics.contains(player)) {
			for(Entity entity : player.getNearbyEntities(5.0D, 5.0D, 5.0D)) {
				if((entity instanceof Player)) {
					player.getWorld().playEffect(player.getLocation(), Effect.POTION_BREAK, 100, 1);
					Player other = (Player)entity;
					Location loc = other.getLocation();
			        loc.setY(loc.getY() + 3.0D);
			          
			        Entity bats = Bukkit.getServer().getWorld(other.getLocation().getWorld().getName()).spawnEntity(loc, EntityType.BAT);
			        
			        ((LivingEntity)entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 150, 1));
			          Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
			            public void run() {
			              bats.remove();
			            }
			          }, 10L);
				}
			}
		}
	}
}
