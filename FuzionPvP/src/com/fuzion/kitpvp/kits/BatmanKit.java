package com.fuzion.kitpvp.kits;

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

import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.manager.kit.Kit;

import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.master.account.Group;

public class BatmanKit extends Kit {
	
	public List<Location> locs = new ArrayList<>();
	
	public BatmanKit() {
		super("Batman", new ItemStack(Material.COAL), Group.BETA, "§7Invoque morcegos venenosos.");
		addItem(new ItemStack(Material.COAL));
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
		if (CooldownAPI.isInCooldown(player.getUniqueId(), getName())) {
			player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
			player.sendMessage("§cVocê está em cooldown durante " + DateUtils.formatDifference((long) CooldownAPI.getTimeLeft(player.getUniqueId(), getName())));
			return;
		}
		
		Random r = new Random();
	      
	      final List<Bat> bats = new ArrayList<Bat>();
	      
	      Location sp = null;
	      
	      boolean air = true;
	      if (air) {
	    	  HashSet<Byte> NullHashSet = null;
	        Location loc = player.getTargetBlock(NullHashSet, 3).getLocation();
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
	      CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), getName(), 15);
	      cooldown.start();
	}
}
