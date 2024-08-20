package br.com.houldmc.rankup.manager.mine;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.manager.mine.list.Mine;
import br.com.houldmc.rankup.manager.mine.list.SelectPos;
import br.com.houldmc.rankup.manager.mine.serialize.SerializeLocation;
import lombok.Getter;

@Getter
public class MineManager {
	
	public static HashMap<String, Mine> mines = new HashMap<>();
	public static HashMap<UUID, SelectPos> selectPos  = new HashMap<>();
	
	private Main plugin = Main.getPlugin();
	
	public void loadMines() {
		if(plugin.getConfig().getString("mines") != null) {
			Set<String> listArenas = plugin.getConfig().getConfigurationSection("mines").getKeys(false);
			plugin.getLogger().log(new LogRecord(Level.INFO, "Load the all mines."));
			int loadeadArenas = 0;
			for(String arenaName : listArenas) {
				if(plugin.getConfig().getString("mines." + arenaName) != null){
					boolean enabled = plugin.getConfig().getBoolean("mines." + arenaName + ".enabled");
					String pos1 = plugin.getConfig().getString("mines." + arenaName + ".area.pos1");
					String pos2 = plugin.getConfig().getString("mines." + arenaName + ".area.pos2");
					int interval = plugin.getConfig().getInt("mines." + arenaName + ".reset_interval");
					
					Mine mine = new Mine(arenaName, enabled, SerializeLocation.deserializeLocation(pos1, false), SerializeLocation.deserializeLocation(pos2, false));
					loadBlocks(mine);
					
					mine.setResetInterval(interval);
					mines.put(arenaName, mine);
					
					plugin.getLogger().log(new LogRecord(Level.INFO, "The mine " + arenaName + " was loaded."));
					loadeadArenas++;
				}
			}
			plugin.getLogger().log(new LogRecord(Level.INFO, loadeadArenas  + " Mines was loaded."));
		}
		//countdown
	}
	
	private void loadBlocks(Mine mine){
		Map<Material, Integer> blocks = new HashMap<>();
		if(plugin.getConfig().getString("mines." + mine.getName() + ".blocks") != null){
			for(String block_name : plugin.getConfig().getConfigurationSection("mines." + mine.getName() + ".blocks").getKeys(false)){
				if(!block_name.equals("")){
					if(plugin.getConfig().getString("mines." + mine.getName() + ".blocks." + block_name) != null){
						int porcentage = plugin.getConfig().getInt("mines." + mine.getName() + ".blocks." + block_name + ".percentage");
						blocks.put(Material.getMaterial(block_name), porcentage);
					}
				}
			}
		}
		mine.setBlocks(blocks);
	}
	
	public boolean createMine(String name, SelectPos selectPos){
		if(mines.containsKey(name)) {
			return false;
		}

		plugin.getConfig().set("mines." + name + ".enabled", true);
		plugin.getConfig().set("mines." + name + ".area.pos1", SerializeLocation.serializeLocation(selectPos.getLocation1(), false));
		plugin.getConfig().set("mines." + name + ".area.pos2", SerializeLocation.serializeLocation(selectPos.getLocation2(), false));
		plugin.getConfig().set("mines." + name + ".reset_interval" , 1);
		plugin.saveConfig();

		Mine mine = new Mine(name, false, selectPos.getLocation1(), selectPos.getLocation2());
		mine.setResetInterval(1);
		mines.put(name, mine);
		loadMines();
		return true;
	}
	
	public boolean deleteMine(String mineName) {
		Mine mine = mines.get(mineName);
		if(mine == null){
			return false;
		}
		
		plugin.getConfig().set("mines." + mine.getName(), null);
		plugin.saveConfig();
		
		mines.remove(mineName);
		return true;
	}
	
	/* SET THE INTERVAL TO RESET A MINE */
	public boolean setIntervalReset(String mineName, Integer interval){
		Mine mine = mines.get(mineName);
		if(mine == null){
			return false;
		}
		
		mine.setResetInterval(interval);

		plugin.getConfig().set("mines." + mine.getName() + ".reset_interval" , interval);
		plugin.saveConfig();
		return true;
	}
	
	/* CHANGE ENABLE MODE */
	public boolean setEnable(String mineName, boolean enabled){
		Mine mine = mines.get(mineName);
		
		if(mine == null){
			return false;
		}

		plugin.getConfig().set("mines." + mine.getName() + ".enabled" , enabled);
		plugin.saveConfig();
		
		mine.setEnabled(enabled);
		
		if(enabled){
			mine.resetMine();
		}
		return true;
	}
	
	/* ADD A BLOCK TO A MINE */
	public int addBlock(String mineName, ItemStack material, Integer percentage){
		Mine mine = mines.get(mineName);
		
		if(mine == null){
			return 0;
		}
		if(mine.getBlocks().containsKey(material.getType())) {
			return 1;
		}
		mine.addBlock(material.getType(), percentage);

		plugin.getConfig().set("mines." + mine.getName() + ".blocks." + material.getType().name() + ".percentage", percentage);
		plugin.saveConfig();
		
		return 2;
	}
	
	public int addBlock(String mineName, Material material, Integer percentage){
		Mine mine = mines.get(mineName);
		
		if(mine == null){
			return 0;
		}
		if(mine.getBlocks().containsKey(material)) {
			return 1;
		}
		mine.addBlock(material, percentage);

		plugin.getConfig().set("mines." + mine.getName() + ".blocks." + material.name() + ".percentage", percentage);
		plugin.saveConfig();
		
		return 2;
	}
	
	/* REMOVE A BLOCK OF A MINE */
	public int removeBlock(String mineName, ItemStack material){
		Mine mine = mines.get(mineName);
		
		if(mine == null){
			return 0;
		}
		
		if(!mine.getBlocks().containsKey(material.getType())) {
			return 1;
		}
		
		mine.removeBlock(material.getType());
		plugin.getConfig().set("mines." + mine.getName() + ".blocks." + material.getType().name(), null);
		plugin.saveConfig();
		return 2;
	}
	
	/* RESET A MINE BY NAME */
	public boolean resetArenaByName(String mineName) {
		Mine mine = mines.get(mineName);
		
		if(mine == null){
			return false;
		}
		
		mine.resetMine();
		return true;
	}
	
	/* START THE COUNTDOWN */
	void startCountDown() {
		new BukkitRunnable() {
			@Override
			public void run() {
				for(Mine mine : mines.values()) {
					if(mine.isEnabled()) {
						mine.resetMine();
						Bukkit.broadcastMessage("�6�lMina �7� �aA mina " + mine.getName() + " acabou de ser resetada.");
					}
				}
			}
		}.runTaskTimer(plugin, 60 * 100L, 60 * 100L);
	}
}
