package me.ale.hg.api.hologram;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

import me.ale.hg.Main;

public class HologramAPI {
	
	private String text = "";
	private double height = 0;
	private Location loc = null;
	private ArmorStand hologram = null;
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

	public HologramAPI() {}

	public void spawn() {
		if (lines.size() == 0) {
			this.loc.setY((this.loc.getY() + this.height) - 1.25);
			hologram = (ArmorStand) this.loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			hologram.setCustomName(this.text);
			hologram.setCustomNameVisible(true);
			hologram.setGravity(false);
			hologram.setVisible(false);
		} else {
			if (lines.size() == 1) {
				spawn();
				return;
			}
			if (lines.size() > 1) {
				this.loc.setY((this.loc.getY() + this.height) - 1.25);
				for (int i = lines.size(); i > 0; i--) {
					final ArmorStand hologram = (ArmorStand) this.loc.getWorld().spawnEntity(loc,
							EntityType.ARMOR_STAND);
					holos.add(hologram);
					hologram.setCustomName(lines.get(i - 1));
					hologram.setCustomNameVisible(true);
					hologram.setGravity(false);
					hologram.setVisible(false);
					this.loc.setY(this.loc.getY() + 0.25);
				}
			}
		}
	}

	public void spawntemp(int time) {
		if (lines.size() == 0) {
			this.loc.setY((this.loc.getY() + this.height) - 1.25);
			hologram = (ArmorStand) this.loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
			hologram.setCustomName(this.text);
			hologram.setCustomNameVisible(true);
			hologram.setGravity(false);
			hologram.setVisible(false);

			Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
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
					final ArmorStand hologram = (ArmorStand) this.loc.getWorld().spawnEntity(loc,
							EntityType.ARMOR_STAND);
					hologram.setCustomName(lines.get(i - 1));
					hologram.setCustomNameVisible(true);
					hologram.setGravity(false);
					hologram.setVisible(false);
					this.loc.setY(this.loc.getY() + 0.25);
					Bukkit.getScheduler().runTaskLater(Main.getPlugin(), new Runnable() {
						@Override
						public void run() {
							hologram.remove();
						}
					}, time);
				}
			}
		}
	}

	public void remove() {
		if (ifhologram()) {
			hologram.remove();
		}
		if (lines.size() > 0) {
			for (Entity entity : holos) {
				entity.remove();
			}
		}
	}

	public void teleport(Location location) {
		if (ifhologram()) {
			hologram.teleport(location);
		}
	}

	public void changeText(String Text) {
		if (ifhologram()) {
			hologram.setCustomName(this.text);
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

	public Boolean ifhologram() {
		if (this.hologram != null) {
			return true;
		} else {
			return false;
		}
	}

}
