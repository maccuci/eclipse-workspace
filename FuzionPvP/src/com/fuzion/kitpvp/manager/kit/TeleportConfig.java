package com.fuzion.kitpvp.manager.kit;

import java.io.File;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.fuzion.kitpvp.Main;

public class TeleportConfig {

	private static File file;
	private static FileConfiguration files;

	public void setup() {
		file = new File(Main.getPlugin().getDataFolder() + "/tp.yml");
		files = YamlConfiguration.loadConfiguration(file);
		save();
		if (files.getString("Locs") == null) {
			files.set("Locs", Integer.valueOf(0));
		}
		save();
	}

	public void save() {
		try {
			files.save(file);
		} catch (Exception localException) {
		}
	}

	public void setLoc(Location loc) {
		setup();
		int name = files.getInt("Locs") + 1;
		files.set("Locs", Integer.valueOf(name));
		int x = loc.getBlockX();
		int y = loc.getBlockY();
		int z = loc.getBlockZ();
		int pitch = (int) loc.getPitch();
		int yau = (int) loc.getYaw();

		files.set(name + ".X", Integer.valueOf(x));
		files.set(name + ".Y", Integer.valueOf(y));
		files.set(name + ".Z", Integer.valueOf(z));
		files.set(name + ".YAW", Integer.valueOf(yau));
		files.set(name + ".PITCH", Integer.valueOf(pitch));
		save();
	}

	public int pos() {
		return files.getInt("Locs") + 1;
	}

	public Location getLocation() {
		Location loc = null;
		int all = files.getInt("Locs");

		int name = new Random().nextInt(all);
		if (name == 0) {
			name = 1;
		}
		int x = files.getInt(name + ".X");
		int y = files.getInt(name + ".Y");
		int z = files.getInt(name + ".Z");
		int yaw = files.getInt(name + ".YAW");
		int pitch = files.getInt(name + ".PITCH");

		loc = new Location(Bukkit.getWorld("world"), x, y, z, yaw, pitch);

		return loc;
	}

	public boolean teleport() {
		return files.getInt("Locs") > 0;
	}
}
