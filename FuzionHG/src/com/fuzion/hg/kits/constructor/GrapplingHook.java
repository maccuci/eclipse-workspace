package com.fuzion.hg.kits.constructor;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_7_R4.CraftWorld;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftSnowball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;

import com.fuzion.hg.manager.game.GameManager;

import net.minecraft.server.v1_7_R4.EntityFishingHook;
import net.minecraft.server.v1_7_R4.EntityHuman;
import net.minecraft.server.v1_7_R4.EntitySnowball;
import net.minecraft.server.v1_7_R4.PacketPlayOutEntityDestroy;

public class GrapplingHook extends EntityFishingHook {
	
	private Snowball sb;
	private EntitySnowball controller;
	public int a;
	public EntityHuman owner;
	public Entity hooked;
	public boolean lastControllerDead;
	public boolean isHooked;

	public GrapplingHook(org.bukkit.World world, EntityHuman entityhuman) {
		super(((CraftWorld) world).getHandle(), entityhuman);
		this.owner = entityhuman;
	}

	@Override
	public void h() {
		if (!this.lastControllerDead && this.controller.dead) {
			((Player) this.owner.getBukkitEntity()).sendMessage("�aSua corda se agarrou em algo!");
			if (hooked instanceof Player)
				((Player) hooked).sendMessage("�aA corda de um grappler agarrou em voc�!");
		}
		this.lastControllerDead = this.controller.dead;
		for (Entity entity : this.controller.world.getWorld().getEntities()) {
			if (entity instanceof Firework)
				continue;
			if (entity instanceof Snowball)
				continue;
			if (entity.getEntityId() == getBukkitEntity().getEntityId())
				continue;
			if (entity.getEntityId() == this.owner.getBukkitEntity().getEntityId())
				continue;
			if (entity.getEntityId() == this.controller.getBukkitEntity().getEntityId())
				continue;
			if (entity.getLocation().distance(this.controller.getBukkitEntity().getLocation()) > 2.0D)
				continue;
			if (entity instanceof Player && GameManager.isSpector((Player) entity))
				continue;
			this.controller.die();
			this.hooked = entity;
			this.isHooked = true;
			this.locX = entity.getLocation().getX();
			this.locY = entity.getLocation().getY();
			this.locZ = entity.getLocation().getZ();
			this.motX = 0.0D;
			this.motY = 0.04D;
			this.motZ = 0.0D;
		}
		try {
			this.locX = this.hooked.getLocation().getX();
			this.locY = this.hooked.getLocation().getY();
			this.locZ = this.hooked.getLocation().getZ();
			this.motX = 0.0D;
			this.motY = 0.04D;
			this.motZ = 0.0D;
			this.isHooked = true;
		} catch (Exception e) {
			if (this.controller.dead) {
				this.isHooked = true;
			}
			this.locX = this.controller.locX;
			this.locY = this.controller.locY;
			this.locZ = this.controller.locZ;
		}
	}

	public void die() {
	}

	public void remove() {
		super.die();
	}

	@SuppressWarnings("deprecation")
	public void spawn(Location location) {
		this.sb = (Snowball) this.owner.getBukkitEntity().launchProjectile(Snowball.class);
		this.controller = ((CraftSnowball) this.sb).getHandle();
		PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] { this.controller.getId() });
		for (Player p : Bukkit.getOnlinePlayers()) {
			((CraftPlayer) p).getHandle().playerConnection.sendPacket(packet);
		}
		((CraftWorld) location.getWorld()).getHandle().addEntity(this);
	}

	public boolean isHooked() {
		return this.isHooked;
	}

	public void setHookedEntity(Entity damaged) {
		this.hooked = damaged;
	}

}
