package me.ale.hg.manager.feast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import me.ale.hg.Main;

public class FeastManager {
	
	private static Block mainBlock = null;
    private static Integer radius = Integer.valueOf(16);
	public static Boolean spawned = Boolean.valueOf(false);
    private static Chest[] chests = new Chest[12];
	private static ArrayList<Location> fblocks = new ArrayList<>();
	public static Boolean FEAST = Boolean.valueOf(true);
	public static Boolean FEAST_CHESTS = Boolean.valueOf(false);
	
	public static void announceFeast() {
		if (mainBlock == null) {
			do {
				int min = -50;
		        int max = 50;
		        Random r = new Random();
		        int x = r.nextInt(max - min + 1) + max;
		        int z = r.nextInt(max - min + 1) + max;
		        Block maxheight = ((World)Bukkit.getServer().getWorlds().get(0)).getHighestBlockAt(x, z);
		        Block loca = ((World)Bukkit.getServer().getWorlds().get(0)).getBlockAt(x, maxheight.getY(), z);
		        
		        mainBlock = loca;
			} while ((mainBlock.getType() == Material.LOG) || (mainBlock.getType() == Material.LEAVES));
		      mainBlock.setType(Material.GRASS);
		      fblocks.add(mainBlock.getLocation());
		      removeAbove(mainBlock);
		      createFeast(Material.GRASS);
		      spawned = Boolean.valueOf(true);
		}
	}
	
	@SuppressWarnings({ "deprecation", "unused" })
	public static void spawn() {
		if (mainBlock == null) {
			announceFeast();
		}
		if (FEAST_CHESTS.booleanValue()) {
			spawnChests();
		}
		List<?> items = Main.getPlugin().getConfig().getStringList("ITEMS");
	    for (Object item : items)
	    {
	      String[] oneitem = ((String)item).split(",");
	      Random r = new Random();
	      String itemid = oneitem[0];
	      Integer minamount = Integer.valueOf(Integer.parseInt(oneitem[1]));
	      Integer maxamount = Integer.valueOf(Integer.parseInt(oneitem[2]));
	      Integer amount = Integer.valueOf(0);
	      Boolean force = Boolean.valueOf(Boolean.parseBoolean(oneitem[3]));
	      Boolean spawn = force;
	      Integer id = null;
	      Short durability = null;
	      if (!force.booleanValue()) {
	        spawn = Boolean.valueOf(r.nextBoolean());
	      }
	      if (spawn.booleanValue())
	      {
	        if (((String)item).contains(":"))
	        {
	          String[] it = itemid.split(":");
	          id = Integer.valueOf(Integer.parseInt(it[0]));
	          durability = Short.valueOf(Short.parseShort(it[1]));
	        }
	        else
	        {
	          id = Integer.valueOf(Integer.parseInt(itemid));
	        }
	        ItemStack i = new ItemStack(id.intValue(), 1);
	        if (durability != null) {
	          i.setDurability(durability.shortValue());
	        }
	        if (oneitem.length == 6) {
	          i.addUnsafeEnchantment(Enchantment.getById(Integer.parseInt(oneitem[4])), Integer.parseInt(oneitem[5]));
	        }
	        Integer ra = radius;
	        if (maxamount == minamount) {
	          amount = maxamount;
	        } else {
	          amount = Integer.valueOf(minamount.intValue() + r.nextInt(maxamount.intValue() - minamount.intValue() + 1));
	        }
	        int maxtry;
	        if (FEAST_CHESTS.booleanValue())
	        {
	          Integer localInteger1;
	          for (; amount.intValue() > 0; localInteger1 = amount = Integer.valueOf(amount.intValue() - 1))
	          {
	            Chest chest = chests[r.nextInt(12)];
	            Integer slot = Integer.valueOf(r.nextInt(27));
	            maxtry = 0;
	            while ((chest.getInventory().getItem(slot.intValue()) != null) && (!chest.getInventory().getItem(slot.intValue()).getType().equals(i.getType())) && (maxtry < 1000)) {
	              slot = Integer.valueOf(r.nextInt(27));
	            }
	            if (chest.getInventory().getItem(slot.intValue()) != null) {
	              i.setAmount(i.getAmount() + 1);
	            }
	            chest.getInventory().setItem(slot.intValue(), i);
	            chest.update();
	            localInteger1 = amount;
	          }
	        }
	        Location c = mainBlock.getLocation();
	        c.add(-(ra.intValue() / 2) + r.nextInt(ra.intValue()), 1.0D, -(ra.intValue() / 2) + r.nextInt(ra.intValue()));
	        Integer slot;
	        for (; amount.intValue() > 0; maxtry = (amount = Integer.valueOf(amount.intValue() - 1)).intValue())
	        {
	          ((World)Bukkit.getServer().getWorlds().get(0)).dropItemNaturally(c, i).setPickupDelay(100);
	          slot = amount;
	        }
	      }
	    }
	}
	
	  public static Boolean isFeastBlock(Block b)
	  {
	    if ((!FEAST.booleanValue()) || (!spawned.booleanValue())) {
	      return Boolean.valueOf(false);
	    }
	    return Boolean.valueOf(fblocks.contains(b.getLocation()));
	  }
	  
	  private static void createFeast(Material m)
	  {
	    Location loc = mainBlock.getLocation();
	    Integer r = radius;
	    for (double x = -r.intValue(); x <= r.intValue(); x += 1.0D) {
	      for (double z = -r.intValue(); z <= r.intValue(); z += 1.0D)
	      {
	        Location l = new Location((World)Bukkit.getServer().getWorlds().get(0), loc.getX() + x, loc.getY(), loc.getZ() + z);
	        if ((l.distance(loc) <= r.intValue()) && (l.getBlock().getType() != Material.NETHERRACK))
	        {
	          removeAbove(l.getBlock());
	          l.getBlock().setType(m);
	          fblocks.add(l);
	        }
	      }
	    }
	  }
	  
	  private static void spawnChests()
	  {
	    Location loc = mainBlock.getLocation();
	    loc.add(-3.0D, 1.0D, -3.0D);
	    Integer curchest = Integer.valueOf(0);
	    //FEAST_PROTECTED = Boolean.valueOf(false);
	    
	    Integer[] co = { Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(3), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(2), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(5), Integer.valueOf(-1) };
	    Integer[] arrayOfInteger1;
	    int j = (arrayOfInteger1 = co).length;
	    for (int i = 0; i < j; i++)
	    {
	       i = arrayOfInteger1[i];
	      
	      Material m = Material.AIR;
	      switch (i)
	      {
	      case 0: 
	        m = Material.AIR;
	        break;
	      case 1: 
	        m = Material.OBSIDIAN;
	        break;
	      case 2: 
	        m = Material.CHEST;
	        break;
	      case 3: 
	        m = Material.ENCHANTMENT_TABLE;
	        break;
	      case 4: 
	        m = Material.FENCE;
	        break;
	      case 5: 
	        break;
	      case 6: 
	        m = Material.WOOD;
	        break;
	      case 7: 
	        m = Material.GLOWSTONE;
	        break;
	      }
	      if (i == -1)
	      {
	        loc.add(0.0D, 0.0D, 1.0D);
	        loc.subtract(7.0D, 0.0D, 0.0D);
	      }
	      else if (i == -2)
	      {
	        loc.add(0.0D, 1.0D, 0.0D);
	        loc.subtract(7.0D, 0.0D, 6.0D);
	      }
	      else if (i == 5)
	      {
	        loc.add(1.0D, 0.0D, 0.0D);
	      }
	      else
	      {
	        loc.getBlock().setType(m);
	        if (i != 0) {
	          fblocks.add(loc.getBlock().getLocation());
	        }
	        if (m == Material.CHEST)
	        {
	          chests[curchest.intValue()] = ((Chest)loc.getBlock().getState());
	          if (curchest.intValue() < 12)
	          {
	          }
	        }
	        loc.add(1.0D, 0.0D, 0.0D);
	      }
	    }
	  }
	  
	  public static void removeAbove(Block block)
	  {
	    Location loc = block.getLocation();
	    loc.setY(loc.getY() + 1.0D);
	    Block newBlock = ((World)Bukkit.getServer().getWorlds().get(0)).getBlockAt(loc);
	    while (loc.getY() < ((World)Bukkit.getServer().getWorlds().get(0)).getMaxHeight())
	    {
	      newBlock.setType(Material.AIR);
	      loc.setY(loc.getY() + 1.0D);
	      newBlock = ((World)Bukkit.getServer().getWorlds().get(0)).getBlockAt(loc);
	    }
	  }
}
