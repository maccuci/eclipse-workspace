package me.ale.pvp.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.FallingBlock;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.ale.pvp.Main;
import me.ale.pvp.kits.YinYangKit;

public class BlockUtil {
	
	 public static ArrayList<BlockState> allBlocks = new ArrayList<>();
	 public static List<UUID> fallingBlocks = new ArrayList<>();
	  
	  @SuppressWarnings("deprecation")
	public static void resetOnReload() {
	    for (BlockState s : allBlocks) {
	      s.getLocation().getBlock().setType(s.getType());
	      s.getLocation().getBlock().setData(s.getData().getData());
	    }
	  }
	
	  public static void regenerateBlocks(Collection<Block> blocks, final Material type, final byte data, final int blocksPerTime, long delay, Comparator<Block> comparator)
	  {
	    final List<Block> orderedBlocks = new ArrayList<>();
	    orderedBlocks.addAll(blocks);
	    if (comparator != null) {
	      Collections.sort(orderedBlocks, comparator);
	    }
	    int size = orderedBlocks.size();
	    if (size > 0) {
	      new BukkitRunnable()
	      {
	        int index;
	        
	        public void run()
	        {
	          for (int i = 0; i < blocksPerTime; i++) {
	            if (this.index >= 0)
	            {
	              Block block = (Block)orderedBlocks.get(this.index);
	              
	              regenerateBlock(block, type, data);
	              
	              this.index -= 1;
	            }
	            else
	            {
	              cancel();
	              return;
	            }
	          }
	        }
	      }.runTaskTimer(Main.getPlugin(), 0L, delay);
	    }
	  }
	  
	  public static void regenerateBlocks(Collection<BlockState> blocks, final int blocksPerTime, long delay, Comparator<BlockState> comparator)
	  {
	    final List<BlockState> orderedBlocks = new ArrayList<>();
	    
	    orderedBlocks.addAll(blocks);
	    if (comparator != null) {
	      Collections.sort(orderedBlocks, comparator);
	    }
	    int size = orderedBlocks.size();
	    if (size > 0) {
	      new BukkitRunnable()
	      {
	        int index;
	        
	        @SuppressWarnings("deprecation")
			public void run()
	        {
	          for (int i = 0; i < blocksPerTime; i++) {
	            if (this.index >= 0)
	            {
	              BlockState state = (BlockState)orderedBlocks.get(this.index);
	              allBlocks.remove(state);
	              regenerateBlock(state.getBlock(), state.getType(), state.getData().getData());
	              
	              this.index -= 1;
	            }
	            else
	            {
	              cancel();
	              return;
	            }
	          }
	        }
	      }.runTaskTimer(Main.getPlugin(), 0L, delay);
	    }
	  }
	  
	  public static void spawnFallingBlocks(Collection<Block> blocks, long delay, Comparator<Block> comparator)
	  {
	    List<Block> orderedBlocks = new ArrayList<>();
	    
	    orderedBlocks.addAll(blocks);
	    if (comparator != null) {
	      Collections.sort(orderedBlocks, comparator);
	    }
	    final int size = orderedBlocks.size();
	    if (size > 0) {
	      new BukkitRunnable()
	      {
	        int index = 0;
	        int yOff = getLowestBlockOnYAxis(blocks);
	        
	        @SuppressWarnings("deprecation")
			public void run()
	        {
	          for (int i = 0; i < getBlocksOnYAxis(blocks, this.yOff); i++) {
	            if (this.index < size)
	            {
	              Block block = (Block)blocks;
	              Location loc = block.getLocation();
	              Material type = block.getType();
	              byte data = block.getData();
	              
	              regenerateBlock(block, Material.AIR, (byte)0);
	              
	              FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(loc, type, data);
	              UUID id = fallingBlock.getUniqueId();
	              
	              fallingBlock.setDropItem(false);
	              fallingBlock.setFireTicks(0);
	              fallingBlock.setVelocity(getRandomVelocity());
	              fallingBlocks.add(id);
	              
	              this.index += 1;
	            }
	            else
	            {
	              cancel();
	              return;
	            }
	          }
	          this.yOff += 1;
	        }
	      }.runTaskTimer(Main.getPlugin(), 0L, delay);
	    }
	  }
	  
	  @SuppressWarnings("deprecation")
	public static void regenerateBlock(Block block, Material type, byte data)
	  {
	    Location loc = block.getLocation();
	    
	    loc.getWorld().playEffect(loc, Effect.STEP_SOUND, type == Material.AIR ? block.getType().getId() : type.getId());
	    block.setType(type);
	    block.setData(data);
	  }
	  
	  public static List<Location> circle(Location loc, int radius, int height, boolean hollow, boolean sphere, int plusY)
	  {
	    List<Location> circleblocks = new ArrayList<>();
	    int cx = loc.getBlockX();
	    int cy = loc.getBlockY();
	    int cz = loc.getBlockZ();
	    for (int x = cx - radius; x <= cx + radius; x++) {
	      for (int z = cz - radius; z <= cz + radius; z++) {
	        for (int y = sphere ? cy - radius : cy; y < (sphere ? cy + radius : cy + height); y++)
	        {
	          double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
	          if ((dist < radius * radius) && ((!hollow) || (dist >= (radius - 1) * (radius - 1))))
	          {
	            Location l = new Location(loc.getWorld(), x, y + plusY, z);
	            circleblocks.add(l);
	          }
	        }
	      }
	    }
	    return circleblocks;
	  }
	  
	  public static List<Block> getRelativeBlocks(Location center, int radius, Collection<Block> startBlocks, Set<Material> types)
	  {
	    List<Block> relative = new ArrayList<>();
	    for (int x = -radius; x < radius; x++) {
	      for (int y = -radius; y < radius; y++) {
	        for (int z = -radius; z < radius; z++)
	        {
	          Block newBlock = center.getBlock().getRelative(x, y, z);
	          if ((types.contains(newBlock.getType())) && 
	            (newBlock.getLocation().distance(center) <= radius)) {
	            relative.add(newBlock);
	          }
	        }
	      }
	    }
	    return relative;
	  }
	  
	  public static List<Block> getNearbyBlocks(Location center, int radius)
	  {
	    List<Location> locs = circle(center, radius, radius, true, true, 0);
	    List<Block> blocks = new ArrayList<>();
	    for (Location loc : locs) {
	      blocks.add(loc.getBlock());
	    }
	    return blocks;
	  }
	  
	  private static int getBlocksOnYAxis(Collection<Block> blocks, int y)
	  {
	    int num = 0;
	    for (Block block : blocks) {
	      if (block.getY() == y) {
	        num++;
	      }
	    }
	    return num;
	  }
	  
	  private static int getLowestBlockOnYAxis(Collection<Block> blocks)
	  {
	    int lowestY = 360;
	    for (Block block : blocks)
	    {
	      int y = block.getY();
	      if (y < lowestY) {
	        lowestY = y;
	      }
	    }
	    return lowestY;
	  }
	  
	  public static void performWorldRegen(List<BlockState> blocks, Location center, final int blocksPerTime, final long speed, long delay) {
	    new BukkitRunnable()
	    {
	      public void run()
	      {
	        regenerateBlocks(blocks, blocksPerTime, speed, new Comparator<BlockState>()
	        {
	          public int compare(BlockState state1, BlockState state2)
	          {
	            return Double.compare(state1.getLocation().distance(center), state2.getLocation().distance(center));
	          }
	        });
	        for (BlockState s : blocks) {
	          if (YinYangKit.used.containsKey(s.getLocation())) {
	        	  YinYangKit.used.remove(s.getLocation());
	          }
	        }
	      }
	    }.runTaskLater(Main.getPlugin(), delay);
	  }
	  
	  public static Vector getRandomVelocity() {
	    Random random = new Random();
	    double rix = random.nextBoolean() ? -0.3D : 0.3D;
	    double riz = random.nextBoolean() ? -0.3D : 0.3D;
	    double x = random.nextBoolean() ? rix * (0.25D + random.nextInt(3) / 5) : 0.0D;
	    double y = 0.6D + random.nextInt(2) / 4.5D;
	    double z = random.nextBoolean() ? riz * (0.25D + random.nextInt(3) / 5) : 0.0D;
	    Vector velocity = new Vector(x, y, z);
	    return velocity;
	  }
}
