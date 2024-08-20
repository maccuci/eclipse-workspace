package me.ale.commons.api.hologram;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.ale.commons.bukkit.BukkitMain;

public class HologramAPI {
	
	private String text = "";
	private double height = 0;
	private Location loc = null;
	private ArmorStand Hologram = null;
	private List<String> lines = new ArrayList<String>();
	private ArrayList<Entity> holos = new ArrayList<Entity>();

	public HologramAPI(Location location,String text, double height){
            this.text = text;
            this.loc = location;
            this.height = height;
    }

	public HologramAPI(Location location, List<String> lines, double height){
            this.lines = lines;
            this.loc = location;
            this.height = height;
    }

	public HologramAPI(){}

	public void spawn() {
		if (lines.size() == 0) {
			this.loc.setY((this.loc.getY() + this.height) - 1.25);
			Hologram = (ArmorStand) this.loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			Hologram.setCustomName(this.text);
			Hologram.setCustomNameVisible(true);
			Hologram.setGravity(false);
			Hologram.setVisible(false);
		} else {
			if (lines.size() == 1) {
				spawn();
				return;
			}
			if (lines.size() > 1) {
				this.loc.setY((this.loc.getY() + this.height) - 1.25);
				for (int i = lines.size(); i > 0; i--) {
					final ArmorStand Hologram = (ArmorStand) this.loc.getWorld().spawnEntity(loc,
							EntityType.ARMOR_STAND);
					holos.add(Hologram);
					Hologram.setCustomName(lines.get(i - 1));
					Hologram.setCustomNameVisible(true);
					Hologram.setGravity(false);
					Hologram.setVisible(false);
					this.loc.setY(this.loc.getY() + 0.25);
				}
			}
		}
	}

	public void spawntemp(int time) {
		if (lines.size() == 0) {
			this.loc.setY((this.loc.getY() + this.height) - 1.25);
			Hologram = (ArmorStand) this.loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			Hologram.setCustomName(this.text);
			Hologram.setCustomNameVisible(true);
			Hologram.setGravity(false);
			Hologram.setVisible(false);

			Bukkit.getScheduler().runTaskLater(BukkitMain.getPlugin(), new Runnable() {
				@Override
				public void run() {
					remove();
				}
			}, time);
		} else {
			if (lines.size() == 1) {
				lines.clear();
				spawntemp(time);
				return;
			}

			if (lines.size() > 1) {
				this.loc.setY((this.loc.getY() + this.height) - 1.25);
				for (int i = lines.size(); i > 0; i--) {
					final ArmorStand Hologram = (ArmorStand) this.loc.getWorld().spawnEntity(loc,
							EntityType.ARMOR_STAND);
					Hologram.setCustomName(lines.get(i - 1));
					Hologram.setCustomNameVisible(true);
					Hologram.setGravity(false);
					Hologram.setVisible(false);
					this.loc.setY(this.loc.getY() + 0.25);
					Bukkit.getScheduler().runTaskLater(BukkitMain.getPlugin(), new Runnable() {
						@Override
						public void run() {
							Hologram.remove();
						}
					}, time);
				}
			}
		}
	}

	public void remove() {
		if (ifHologram()) {
			Hologram.remove();
		}
		if (lines.size() > 0) {
			for (Entity entity : holos) {
				entity.remove();
			}
		}
	}

	public void teleport(Location location) {
		if (ifHologram()) {
			Hologram.teleport(location);
		}
	}

	public void changeText(String Text) {
		if (ifHologram()) {
			Hologram.setCustomName(this.text);
		}
	}

	public void setText(String Text) {
		this.text = Text;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public void setLocation(Location location) {
		this.loc = location;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public Boolean ifHologram() {
		if (this.Hologram != null) {
			return true;
		} else {
			return false;
		}
	}

}
