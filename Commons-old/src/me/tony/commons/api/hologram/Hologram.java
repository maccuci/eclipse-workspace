package me.tony.commons.api.hologram;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.tony.commons.bukkit.BukkitMain;
import net.minecraft.server.v1_7_R4.DataWatcher;
import net.minecraft.server.v1_7_R4.EntityHorse;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.EntityWitherSkull;
import net.minecraft.server.v1_7_R4.PacketPlayOutAttachEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntity;
import net.minecraft.server.v1_7_R4.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_7_R4.WatchableObject;
import net.minecraft.server.v1_7_R4.World;
import net.minecraft.util.gnu.trove.map.TIntObjectMap;

public class Hologram {

	private Location location;
	private EntityHorse horse1_7;
	private EntityWitherSkull skull1_7;
	private EntityHorse armorstand1_8;
	private PacketPlayOutSpawnEntityLiving spawnArmorStand1_8;
	private PacketPlayOutSpawnEntity spawnSkull1_7;
	private PacketPlayOutSpawnEntityLiving spawnHorse1_7;
	private PacketPlayOutAttachEntity attach1_7;
	private PacketPlayOutEntityDestroy despawn1_8;
	private PacketPlayOutEntityDestroy despawn1_7;
	private List<UUID> showing = new ArrayList<>();
	private HashSet<Hologram> lines = new HashSet<>();
	private String text;

	private double linesSpace;
	private boolean register;

	@SuppressWarnings("unchecked")
	public Hologram(String text, Location location, boolean register) {
		this.text = text;
		this.location = location;

		World world = ((CraftWorld) location.getWorld()).getHandle();

		armorstand1_8 = new EntityHorse(world);
		armorstand1_8.setLocation(location.getX(), location.getY() - 1, location.getZ(), 0, 0);

		DataWatcher dataWatcher = armorstand1_8.getDataWatcher();
		TIntObjectMap<WatchableObject> dataValues = (TIntObjectMap<WatchableObject>) getValue("dataValues", dataWatcher);

		List<Integer> toRemove = new ArrayList<>();

		for (int i = 0; i < 100; i++) {

			WatchableObject current = dataValues.get(i);

			if (current == null)
				continue;

			int index = (int) getValue("b", current);

			if (index == 2) {
				setValue("c", current, getText());
			} else if (index == 3) {
				setValue("c", current, getText().isEmpty() ? (byte) 0 : (byte) 1);
			} else {
				toRemove.add(Integer.valueOf(index));
			}
		}

		for (int i : toRemove) {
			dataValues.remove(i);
		}

		dataValues.put(0, new WatchableObject(0, 0, (byte) 32));

		horse1_7 = new EntityHorse(world);
		horse1_7.setLocation(getLocation().getX(), getLocation().getY() + 55, getLocation().getZ(), 0, 0);
		horse1_7.setCustomName(text);
		horse1_7.setCustomNameVisible(!text.isEmpty());
		horse1_7.setAge(-1700000);

		skull1_7 = new EntityWitherSkull(world);
		skull1_7.setLocation(getLocation().getX(), getLocation().getY() + 1 + 55, getLocation().getZ(), 0, 0);

		spawnSkull1_7 = new PacketPlayOutSpawnEntity(skull1_7, 66);
		spawnHorse1_7 = new PacketPlayOutSpawnEntityLiving(horse1_7);

		spawnArmorStand1_8 = new PacketPlayOutSpawnEntityLiving(armorstand1_8);
		setValue("b", spawnArmorStand1_8, 30);

		attach1_7 = new PacketPlayOutAttachEntity(0, horse1_7, skull1_7);

		despawn1_7 = new PacketPlayOutEntityDestroy(horse1_7.getId(), skull1_7.getId());
		despawn1_8 = new PacketPlayOutEntityDestroy(armorstand1_8.getId());
		this.register = register;
		HologramListener.getHolograms().add(this);
	}

	public boolean isRegister() {
		return register;
	}

	public HashSet<Hologram> getLines() {
		return lines;
	}

	public List<UUID> getShowing() {
		return showing;
	}

	public Location getLocation() {
		return location;
	}

	public String getText() {
		return text;
	}

	public boolean isVisible(Player p) {
		return showing.contains(p.getUniqueId());
	}

	public Hologram addLine(String... s) {
		Hologram hologram = null;
		for (String string : s) {
			hologram = addLine(string);
		}
		return hologram;
	}

	public Hologram addLine(String s) {
		linesSpace -= 0.25;
		Hologram hologram = new Hologram(s, getLocation().clone().add(0, linesSpace, 0), register);
		lines.add(hologram);
		return hologram;
	}

	public void update(String text) {

		if (showing.isEmpty())
			return;

		this.text = text;

		horse1_7.setCustomName(text);
		armorstand1_8.setCustomName(text);
		horse1_7.setCustomNameVisible(!text.isEmpty());
		armorstand1_8.setCustomNameVisible(!text.isEmpty());

		PacketPlayOutEntityMetadata metadata1_7 = new PacketPlayOutEntityMetadata(horse1_7.getId(), horse1_7.getDataWatcher(), true);
		PacketPlayOutEntityMetadata metadata1_8 = new PacketPlayOutEntityMetadata(armorstand1_8.getId(), armorstand1_8.getDataWatcher(), true);

		for (UUID uuid : showing) {

			if (Bukkit.getPlayer(uuid) == null)
				continue;

			EntityPlayer entityPlayer = ((CraftPlayer) Bukkit.getPlayer(uuid)).getHandle();

			if (entityPlayer.playerConnection.networkManager.getVersion() >= 47) {
				entityPlayer.playerConnection.sendPacket(metadata1_8);
			} else {
				entityPlayer.playerConnection.sendPacket(metadata1_7);
			}

		}
	}

	public void update(Player player) {

		if (!showing.contains(player.getUniqueId()))
			return;

		EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

		if (entityPlayer.playerConnection.networkManager.getVersion() >= 47) {
			PacketPlayOutEntityMetadata metadata1_8 = new PacketPlayOutEntityMetadata(armorstand1_8.getId(), armorstand1_8.getDataWatcher(), true);
			entityPlayer.playerConnection.sendPacket(metadata1_8);
		} else {
			PacketPlayOutEntityMetadata metadata1_7 = new PacketPlayOutEntityMetadata(horse1_7.getId(), horse1_7.getDataWatcher(), true);
			entityPlayer.playerConnection.sendPacket(metadata1_7);
		}

		for (Hologram hologram : lines) {
			hologram.update(player);
		}

	}

	public void show(Player player) {
		
		showing.add(player.getUniqueId());

		EntityPlayer nmsPlayer = ((CraftPlayer) player).getHandle();

		if (nmsPlayer.playerConnection.networkManager.getVersion() >= 47) {
			nmsPlayer.playerConnection.sendPacket(spawnArmorStand1_8);
			PacketPlayOutEntityMetadata metadata1_8 = new PacketPlayOutEntityMetadata(armorstand1_8.getId(), armorstand1_8.getDataWatcher(), true);
			nmsPlayer.playerConnection.sendPacket(metadata1_8);
		} else {
			nmsPlayer.playerConnection.sendPacket(spawnHorse1_7);
			nmsPlayer.playerConnection.sendPacket(spawnSkull1_7);
			nmsPlayer.playerConnection.sendPacket(attach1_7);
			PacketPlayOutEntityMetadata metadata1_7 = new PacketPlayOutEntityMetadata(horse1_7.getId(), horse1_7.getDataWatcher(), true);
			nmsPlayer.playerConnection.sendPacket(metadata1_7);
		}

		for (Hologram hologram : lines) {
			hologram.show(player);
		}
	}

	public void hide(Player player) {
		if (showing.contains(player.getUniqueId())) {
			EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();

			if (entityPlayer.playerConnection.networkManager.getVersion() >= 47) {
				entityPlayer.playerConnection.sendPacket(despawn1_8);
			} else {
				entityPlayer.playerConnection.sendPacket(despawn1_7);
			}

			showing.remove(player.getUniqueId());

		}

		for (Hologram hologram : lines) {
			hologram.hide(player);
		}
	}

	public void remove() {
		register = false;

		for (UUID uuid : showing) {
			if (Bukkit.getPlayer(uuid) != null) {
				hide(Bukkit.getPlayer(uuid));
			}
		}

		for (Hologram hologram : lines) {
			hologram.remove();
		}

		showing.clear();
		horse1_7.die();
		skull1_7.die();
		armorstand1_8.die();
	}

	public synchronized void teleport(Location location) {
		skull1_7.setLocation(location.getX(), location.getY() + 1 + 55, location.getZ(), location.getYaw(), location.getPitch());
		armorstand1_8.setLocation(location.getX(), location.getY() - 1, location.getZ(), location.getYaw(), location.getPitch());
		PacketPlayOutEntityTeleport teleport1_7Skull = new PacketPlayOutEntityTeleport(skull1_7);
		PacketPlayOutEntityTeleport teleport1_8Horse = new PacketPlayOutEntityTeleport(armorstand1_8);

		for (UUID uuid : this.showing) {
			if (Bukkit.getPlayer(uuid) == null) {
				return;
			}
			EntityPlayer entityPlayer = ((CraftPlayer) Bukkit.getPlayer(uuid)).getHandle();
			if (entityPlayer.playerConnection.networkManager.getVersion() >= 47) {
				entityPlayer.playerConnection.sendPacket(teleport1_8Horse);
			} else {
				entityPlayer.playerConnection.sendPacket(teleport1_7Skull);
			}
		}
	}

	private HashSet<UUID> lock = new HashSet<>();

	public void lock(final Player player, long ticks) {
		lock.add(player.getUniqueId());
		new BukkitRunnable() {
			public void run() {
				lock.remove(player.getUniqueId());
			}
		}.runTaskLater(BukkitMain.getPlugin(BukkitMain.class), ticks);
	}

	public boolean isLocked(Player player) {
		return lock.contains(player.getUniqueId());
	}
	
	public void setValue(String field, Class<?> clazz, Object instance, Object value) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Exception exception) {

		}
	}

	public Object getValue(String field, Class<?> clazz, Object instance) {
		try {
			Field f = clazz.getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception exception) {

		}
		return null;
	}

	public void setValue(String field, Object instance, Object value) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			f.set(instance, value);
		} catch (Exception exception) {

		}
	}

	public Object getValue(String field, Object instance) {
		try {
			Field f = instance.getClass().getDeclaredField(field);
			f.setAccessible(true);
			return f.get(instance);
		} catch (Exception exception) {

		}
		return null;
	}
}