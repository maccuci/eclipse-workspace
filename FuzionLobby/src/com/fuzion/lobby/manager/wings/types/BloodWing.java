package com.fuzion.lobby.manager.wings.types;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.fuzion.lobby.Main;
import com.fuzion.lobby.manager.wings.Point3D;
import com.fuzion.lobby.manager.wings.WingData;
import com.fuzion.lobby.manager.wings.constructor.ParticleEffect;

public class BloodWing {
	
public static int k = 0;
	
	private Point3D[] outline = { new Point3D(0, 0, -0.5f), new Point3D(0.1f, 0.01f, -0.5f),
			new Point3D(0.3f, 0.03f, -0.5f), new Point3D(0.4f, 0.04f, -0.5f), new Point3D(0.6f, 0.1f, -0.5f),
			new Point3D(0.61f, 0.2f, -0.5f), new Point3D(0.62f, 0.4f, -0.5f), new Point3D(0.63f, 0.6f, -0.5f),
			new Point3D(0.635f, 0.7f, -0.5f), new Point3D(0.7f, 0.7f, -0.5f), new Point3D(0.9f, 0.75f, -0.5f),
			new Point3D(1.2f, 0.8f, -0.5f), new Point3D(1.4f, 0.9f, -0.5f), new Point3D(1.6f, 1f, -0.5f),
			new Point3D(1.8f, 1.1f, -0.5f), new Point3D(1.85f, 0.9f, -0.5f), new Point3D(1.9f, 0.7f, -0.5f),
			new Point3D(1.85f, 0.5f, -0.5f), new Point3D(1.8f, 0.3f, -0.5f), new Point3D(1.75f, 0.1f, -0.5f),
			new Point3D(1.7f, -0.1f, -0.5f), new Point3D(1.65f, -0.3f, -0.5f), new Point3D(1.55f, -0.5f, -0.5f),
			new Point3D(1.45f, -0.7f, -0.5f), new Point3D(1.30f, -0.75f, -0.5f), new Point3D(1.15f, -0.8f, -0.5f),
			new Point3D(1.0f, -0.85f, -0.5f), new Point3D(0.8f, -0.87f, -0.5f), new Point3D(0.6f, -0.7f, -0.5f),
			new Point3D(0.5f, -0.5f, -0.5f), new Point3D(0.4f, -0.3f, -0.5f), new Point3D(0.3f, -0.3f, -0.5f),
			new Point3D(0.15f, -0.3f, -0.5f), new Point3D(0f, -0.3f, -0.5f),

			//
			new Point3D(0.9f, 0.55f, -0.5f), new Point3D(1.2f, 0.6f, -0.5f), new Point3D(1.4f, 0.7f, -0.5f),
			new Point3D(1.6f, 0.9f, -0.5f),
			//
			new Point3D(0.9f, 0.35f, -0.5f), new Point3D(1.2f, 0.4f, -0.5f), new Point3D(1.4f, 0.5f, -0.5f),
			new Point3D(1.6f, 0.7f, -0.5f),
			//
			new Point3D(0.9f, 0.15f, -0.5f), new Point3D(1.2f, 0.2f, -0.5f), new Point3D(1.4f, 0.3f, -0.5f),
			new Point3D(1.6f, 0.5f, -0.5f),
			//
			new Point3D(0.9f, -0.05f, -0.5f), new Point3D(1.2f, 0f, -0.5f), new Point3D(1.4f, 0.1f, -0.5f),
			new Point3D(1.6f, 0.3f, -0.5f),
			//
			new Point3D(0.7f, -0.25f, -0.5f), new Point3D(1.0f, -0.2f, -0.5f), new Point3D(1.2f, -0.1f, -0.5f),
			new Point3D(1.4f, 0.1f, -0.5f),
			//
			new Point3D(0.7f, -0.45f, -0.5f), new Point3D(1.0f, -0.4f, -0.5f), new Point3D(1.2f, -0.3f, -0.5f),
			new Point3D(1.4f, -0.1f, -0.5f),
			//
			new Point3D(1.30f, -0.55f, -0.5f), new Point3D(1.15f, -0.6f, -0.5f), new Point3D(1.0f, -0.65f, -0.5f) };

	public void display(Player player, WingData data) {
		if (data.equipped) {
			k = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
				
				@Override
				public void run() {
					display(player);
				}
			}, 0, 5);
		}
	}

	public void display(Player player) {
		Location playerLocation = player.getEyeLocation();
		World playerWorld = player.getWorld();
		float x = (float) playerLocation.getX();
		float y = (float) playerLocation.getY() - 0.2f;
		float z = (float) playerLocation.getZ();
		float rot = -playerLocation.getYaw() * 0.017453292F;

		Point3D rotated = null;
		for (Point3D point : outline) {
			rotated = point.rotate(rot);

			ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(255, 0, 0),
					new Location(playerWorld, rotated.x + x, rotated.y + y, rotated.z + z), 50);

			point.z *= -1;
			rotated = point.rotate(rot + 3.1415f);
			point.z *= -1;

			ParticleEffect.REDSTONE.display(new ParticleEffect.OrdinaryColor(255, 0, 0),
					new Location(playerWorld, rotated.x + x, rotated.y + y, rotated.z + z), 50f);
		}

/*		for (Point3D point : fill) {
			rotated = point.rotate(rot);

			ParticleEffect.HEART.display(3f, 3f, 3f, 1f, 0,
					new Location(playerWorld, rotated.x + x, rotated.y + y, rotated.z + z), 50f);

			point.z *= -1;
			rotated = point.rotate(rot + 3.1415f);
			point.z *= -1;

			ParticleEffect.HEART.display(3f, 3f, 3f, 1f, 0,
					new Location(playerWorld, rotated.x + x, rotated.y + y, rotated.z + z), 50f);
		}*/
	}

	public String getName() {
		return "Blood";
	}

}
