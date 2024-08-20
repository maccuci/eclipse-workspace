package me.ale.pvp.kits;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.api.item.ItemBuilder;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.DateUtils;

public class BatmanKit extends Kit {
	
	public List<Location> locs = new ArrayList<>();
	
	public BatmanKit() {
		super("Batman", Material.COAL, new ItemStack(Material.COAL), KitRarity.RARE, Rank.SIMPLE, "§7Invoque morcegos explosivos e venenosos.");
	}
	
	@SuppressWarnings("deprecation")
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
		
		Random r = new Random();
	      
	      final List<Bat> bats = new ArrayList<Bat>();
	      
	      Location sp = null;
	      
	      boolean air = true;
	      if (air) {
	    	  HashSet<Byte> NullHashSet = null;
	        Location loc = player.getTargetBlock(NullHashSet, 3).getLocation();
	       // player.sendBlockChange(loc, Material.GLASS, (byte)0);
	        sp = loc;
	      }
	      double min = -1.5D;
	      double max = 1.5D;
	      while (sp.clone().add(0.0D, -1.5D, 0.0D).getBlock().getType() != Material.AIR) {
	        sp.add(0.0D, 1.5D, 0.0D);
	      }
	      for (int i = 0; i < 30; i++)
	      {
	        double x = r.nextInt((int)(max - min)) + min;
	        double y = r.nextInt((int)(max - min)) + min;
	        double z = r.nextInt((int)(max - min)) + min;
	        
	        Location spawn = sp.clone().add(x, y, z);
	        
	        locs.add(spawn);
	        Bat b = (Bat)spawn.getWorld().spawnEntity(spawn, 
	          EntityType.BAT);
	        b.setMetadata("NoDamage", new FixedMetadataValue(Main.getPlugin(), 
	          "NoDamage"));
	        locs.remove(spawn);
	        bats.add(b);
	      }
	      sp.getBlock().getState().update();
	      
	      Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), 
	        new Runnable()
	        {
	          public void run()
	          {
	            for (Bat b : bats) {
	              b.remove();
	            }
	            bats.clear();
	          }
	        }, 60L);
	      
	      new BukkitRunnable()
	      {
	        public void run()
	        {
	          if (bats.size() != 0)
	          {
	            Iterator<?> localIterator2;
	            for (Iterator<Bat> localIterator1 = bats.iterator(); localIterator1.hasNext(); localIterator2.hasNext())
	            {
	              Bat b = (Bat)localIterator1.next();
	              localIterator2 = b.getNearbyEntities(0.8D, 0.8D, 0.8D).iterator();
	              while (localIterator2.hasNext()) {
	              Entity ent = (Entity)localIterator2.next();
	              if ((ent instanceof Player))
	              {
	                Player target = (Player)ent;
	                
	                  if (target != player)
	                  {
	                    target.setVelocity(new Vector(0.0D, 0.3D, 0.0D));
	                    
	                    target.damage(1.5D);
	                    if (!target.hasPotionEffect(PotionEffectType.POISON)) {
	                      target.addPotionEffect(new PotionEffect(
	                        PotionEffectType.POISON, 160, 0));
	                    }
	                  }
	              }
	            }
	          }
	          }
	        }
	      }.runTaskTimer(Main.getPlugin(), 0L, 5L);
	      CooldownAPI.addCooldown(player, new Cooldown(getName(), 30L));
	}
}
