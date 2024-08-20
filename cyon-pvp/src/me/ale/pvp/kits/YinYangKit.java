package me.ale.pvp.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import me.ale.commons.CyonAPI;
import me.ale.commons.api.cooldown.CooldownAPI;
import me.ale.commons.api.cooldown.types.Cooldown;
import me.ale.commons.core.account.Rank;
import me.ale.pvp.Main;
import me.ale.pvp.manager.kit.Kit;
import me.ale.pvp.manager.kit.KitRarity;
import me.ale.pvp.util.BlockUtil;
import me.ale.pvp.util.DateUtils;
import net.minecraft.server.v1_8_R3.EntityFireball;

public class YinYangKit extends Kit {
	
	  public static HashMap<UUID, String> fireballs = new HashMap<UUID, String>();
	  public static HashMap<Location, BlockState> used = new HashMap<Location, BlockState>();
	  public static HashMap<UUID, Integer> timers = new HashMap<UUID, Integer>();
	
	public YinYangKit() {
		super("YinYang", Material.WOOL, new ItemStack(Material.WOOL), KitRarity.EXTRAORDINARY, Rank.SIMPLE, "§7Tenha o poder de criar uma chuva de", "§7meteoros ao seu redor.");	
	}
	
	@EventHandler
	public void onBlock(BlockPlaceEvent event) {
		Player player = event.getPlayer();
		
		if(hasKit(player) && event.getBlockPlaced().getType() == Material.WOOL) {
			if(CooldownAPI.hasCooldown(player, getName())) {
				player.playSound(player.getLocation(), Sound.IRONGOLEM_HIT, 0.5F, 1.0F);
				player.sendMessage(CyonAPI.SERVER_PREFIX + "Você está em cooldown durante §c" + DateUtils.formatDifference((long) CooldownAPI.getCooldown(player).getRemaining()));
				return;
			}
			
	        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new Runnable() {
	          public void run()
	          {
	            doEffect(player, event.getBlockPlaced().getLocation());
	          }
	        }, 1L);
	        
	        CooldownAPI.addCooldown(player, new Cooldown(getName(), 40L));
		}
	}
	
	@EventHandler
	  public void explosionPrime(ExplosionPrimeEvent event) {
		if ((event.getEntityType() == EntityType.FIREBALL)
				&& (fireballs.containsKey(event.getEntity().getUniqueId()))) {
			String hitter = (String) fireballs.get(event.getEntity().getUniqueId());
			event.setCancelled(true);
			event.setFire(false);
			event.getEntity().getWorld().createExplosion(event.getEntity().getLocation(), 0.0F);
			for (Entity en : event.getEntity().getNearbyEntities(4.0D, 4.0D, 4.0D)) {
				if (((en instanceof LivingEntity))) {
					if (en.getType() == EntityType.PLAYER) {
							if (Bukkit.getPlayer(hitter) != null) {
								((LivingEntity) en).damage(9.0D, Bukkit.getPlayer(hitter));
							} else {
								((LivingEntity) en).damage(9.0D);
						}
					}
				}
				fireballs.remove(event.getEntity().getUniqueId());
		}
	}
}
	
	@EventHandler
	public void onSnowballHit(EntityDamageByEntityEvent e) {
		if ((e.getEntityType() == EntityType.PLAYER) && (e.getDamager().getType() == EntityType.FIREBALL)
				&& (fireballs.containsKey(e.getDamager().getUniqueId()))) {
			e.setCancelled(true);
			if (((Player) e.getEntity()).getName() == fireballs.get(e.getDamager().getUniqueId())) {
				fireballs.remove(e.getDamager().getUniqueId());
			} else {
				String hitter = (String) fireballs.get(e.getEntity().getUniqueId());
				e.setCancelled(true);
				e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 0.0F);
				for (Entity en : e.getEntity().getNearbyEntities(4.0D, 4.0D, 4.0D)) {
					if (((en instanceof LivingEntity))) {
						if (en.getType() == EntityType.PLAYER) {
								if (Bukkit.getPlayer(hitter) != null) {
									((LivingEntity) en).damage(9.0D, Bukkit.getPlayer(hitter));
								} else {
									((LivingEntity) en).damage(9.0D);
								}
							}
						}
					}
					fireballs.remove(e.getEntity().getUniqueId());
				}
			}
		}
	
	  @SuppressWarnings("deprecation")
	public static void doEffect(final Player p, Location loc)
	  {
	    List<BlockState> oldBlocks = new ArrayList<>();
	    for (int x = loc.getBlockX() - 1; x <= loc.getBlockX() + 1; x++) {
	      for (int z = loc.getBlockZ() - 1; z <= loc.getBlockZ() + 1; z++)
	      {
	        Location l = new Location(loc.getWorld(), x, loc.getY() - 1.0D, z);
	        if (used.containsKey(l))
	        {
	          oldBlocks.add((BlockState)used.get(l));
	        }
	        else
	        {
	          oldBlocks.add(l.getBlock().getState());
	          used.put(l, l.getBlock().getState());
	        }
	      }
	    }
	    oldBlocks.add(loc.getBlock().getState());
	    int r = 13;
	    int cx = loc.getBlockX();
	    int cz = loc.getBlockZ();
	    int rSquared = r * r;
	    for (int x = cx - r; x <= cx + r; x++) {
	      for (int z = cz - r; z <= cz + r; z++) {
	        if ((cx - x) * (cx - x) + (cz - z) * (cz - z) <= rSquared)
	        {
	          final Location l = new Location(loc.getWorld(), x, 113.0D, z);
	          long delay = 0L;
	          int wave = new Random().nextInt(2);
	          switch (wave)
	          {
	          case 0: 
	            delay = 0L;
	            break;
	          case 1: 
	            delay = 20L;
	          }
	          if (new Random().nextInt(100) <= 20) {
	            Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getPlugin(), new BukkitRunnable()
	            {
	              public void run()
	              {
	                try
	                {
	                  final Fireball fb = (Fireball)p.getWorld().spawnEntity(l, EntityType.FIREBALL);
	                  fireballs.put(fb.getUniqueId(), p.getName());
	                  
	                  timers.put(fb.getUniqueId(), Integer.valueOf(Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable()
	                  {
	                    int x = 0;
	                    
	                    public void run()
	                    {
	                      try
	                      {
	                        if (this.x != 600)
	                        {
	                          EntityFireball efb = ((CraftFireball)fb).getHandle();
	                          if (efb.isAlive())
	                          {
	                            if (fireballs.containsKey(efb.getUniqueID()))
	                            {
	                              efb.motX = 1.0E-4D;
	                              efb.motZ = 1.0E-4D;
	                              efb.motY = -1.0D;
	                            }
	                          }
	                          else
	                          {
	                            Bukkit.getScheduler().cancelTask(((Integer)timers.get(fb.getUniqueId())).intValue());
	                            timers.remove(fb.getUniqueId());
	                          }
	                        }
	                        else
	                        {
	                          timers.remove(fb.getUniqueId());
	                          Bukkit.getScheduler().cancelTask(((Integer)timers.get(fb.getUniqueId())).intValue());
	                          return;
	                        }
	                        this.x += 1;
	                      }
	                      catch (Exception e)
	                      {
	                        timers.remove(fb.getUniqueId());
	                        Bukkit.getScheduler().cancelTask(((Integer)timers.get(fb.getUniqueId())).intValue());
	                        return;
	                      }
	                    }
	                  }, 0L, 1L)));
	                }
	                catch (Exception e)
	                {
	                  cancel();
	                }
	              }
	            }, delay);
	          }
	        }
	      }
	    }
	    loc.getWorld().playSound(loc, Sound.ENDERDRAGON_GROWL, 2.0F, 1.0F);
	    loc.getBlock().setType(Material.BEACON);
	    BlockUtil.performWorldRegen(oldBlocks, loc, 3, 20L, 200L);
	  }
}
