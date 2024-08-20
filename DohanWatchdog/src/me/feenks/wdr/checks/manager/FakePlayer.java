package me.feenks.wdr.checks.manager;

import java.util.UUID;
import net.minecraft.server.v1_7_R4.EntityPlayer;
import net.minecraft.server.v1_7_R4.PlayerInteractManager;
import net.minecraft.util.com.mojang.authlib.GameProfile;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftServer;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;

public class FakePlayer extends EntityPlayer {
	
	public FakePlayer(String playerName, Location loc) {
		super(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) loc.getWorld()).getHandle(),
				new GameProfile(UUID.randomUUID(), playerName),
				new PlayerInteractManager(((CraftWorld) loc.getWorld()).getHandle()));
		setInvisible(false);
		setHealth(20.0F);
		setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
	}

	public boolean isBlocking() {
		return true;
	}
}
