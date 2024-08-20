package me.ale.commons.bukkit.permission;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import me.ale.commons.bukkit.BukkitMain;

public class PermissionConfig {
	
	private File file;
	  private FileConfiguration files;
	  
	  public PermissionConfig()
	  {
	    this.file = new File(BukkitMain.getPlugin().getDataFolder() + "/pex/config.yml");
	    this.files = YamlConfiguration.loadConfiguration(this.file);
	    save();
	  }
	  
	  public void save() {
	    try
	    {
	      this.files.save(this.file);
	    }
	    catch (Exception localException) {}
	  }
	  
	  
	  public void addPermissionPlayer(String name, String perm) {
	    List<String> perms = this.files.getStringList(name + ".permissions");
	    if (!perms.contains(perm)) {
	      perms.add(perm);
	    } else {
	    	return;
	    }
	    this.files.set(name + ".permissions", perms);
	    save();
	  }
	  
	  public void removePermPlayer(String name, String perm) {
	    List<String> perms = this.files.getStringList(name + ".permissions");
	    if (perms.contains(perm)) {
	      perms.remove(perm);
	    } else {
	    	return;
	    }
	    this.files.set(name + ".permissions", perms);
	    save();
	  }
	  
	  public List<String> getPermissions(String nick) {
	    return this.files.getStringList(nick + ".permissions");
	  }

}
