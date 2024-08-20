package me.ale.commons.api.disiguise;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityBat;
import net.minecraft.server.v1_8_R3.EntityBlaze;
import net.minecraft.server.v1_8_R3.EntityCaveSpider;
import net.minecraft.server.v1_8_R3.EntityChicken;
import net.minecraft.server.v1_8_R3.EntityCow;
import net.minecraft.server.v1_8_R3.EntityCreeper;
import net.minecraft.server.v1_8_R3.EntityEnderDragon;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.EntityGhast;
import net.minecraft.server.v1_8_R3.EntityGiantZombie;
import net.minecraft.server.v1_8_R3.EntityHorse;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.EntityIronGolem;
import net.minecraft.server.v1_8_R3.EntityMagmaCube;
import net.minecraft.server.v1_8_R3.EntityMushroomCow;
import net.minecraft.server.v1_8_R3.EntityOcelot;
import net.minecraft.server.v1_8_R3.EntityPig;
import net.minecraft.server.v1_8_R3.EntityPigZombie;
import net.minecraft.server.v1_8_R3.EntitySheep;
import net.minecraft.server.v1_8_R3.EntitySilverfish;
import net.minecraft.server.v1_8_R3.EntitySkeleton;
import net.minecraft.server.v1_8_R3.EntitySlime;
import net.minecraft.server.v1_8_R3.EntitySnowman;
import net.minecraft.server.v1_8_R3.EntitySpider;
import net.minecraft.server.v1_8_R3.EntitySquid;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.EntityWolf;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.World;

@Getter
public class DisiguiseAPI {
	
	private String name;
	private EntityType type;
	private Player player;
	
	public DisiguiseAPI(Player player, EntityType type, String name) {
		this.name = name;
		this.type = type;
		this.player = player;
	}
	
	public void transformPlayerToMob() {
		PacketPlayOutEntityDestroy entitydestroy = new PacketPlayOutEntityDestroy(new int[]{player.getEntityId()});
		World world = ((CraftPlayer)player).getHandle().world;
		Entity ent = getEntityLiving(world, type);
		ent.setPosition(player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
		ent.d(player.getEntityId());
		EntityInsentient entI = (EntityInsentient) ent;
		entI.setCustomName(name);
		entI.setCustomNameVisible(true);
		PacketPlayOutSpawnEntityLiving spawnentityliving = new PacketPlayOutSpawnEntityLiving();
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(pls != player){
			((CraftPlayer)pls).getHandle().playerConnection.sendPacket(entitydestroy);
			((CraftPlayer)pls).getHandle().playerConnection.sendPacket(spawnentityliving);
			pls.showPlayer(player);
			}
		}
	}
	
	public void removeTransform(){
		PacketPlayOutEntityDestroy entitydestroy = new PacketPlayOutEntityDestroy(new int[]{player.getEntityId()});
		PacketPlayOutNamedEntitySpawn entityspawn = new PacketPlayOutNamedEntitySpawn(((CraftPlayer)player).getHandle());
		for(Player pls : Bukkit.getOnlinePlayers()){
			if(pls != player){
			((CraftPlayer)pls).getHandle().playerConnection.sendPacket(entitydestroy);
			((CraftPlayer)pls).getHandle().playerConnection.sendPacket(entityspawn);
			pls.showPlayer(player);
			}
		}
	}
	
	public Entity getEntityLiving(World world,EntityType mob){
        if (mob.equals(EntityType.BAT)){
            return new EntityBat(world);
        }
        if (mob.equals(EntityType.BLAZE)){
            return new EntityBlaze(world);
        }
        if (mob.equals(EntityType.CAVE_SPIDER)){
            return new EntityCaveSpider(world);
        }
        if (mob.equals(EntityType.CHICKEN)){
            return new EntityChicken(world);
        }
        if (mob.equals(EntityType.COW)){
            return new EntityCow(world);
        }
        if (mob.equals(EntityType.CREEPER)){
            return new EntityCreeper(world);
        }
        if (mob.equals(EntityType.ENDER_DRAGON)){
            return new EntityEnderDragon(world);
        }
        if (mob.equals(EntityType.ENDERMAN)){
            return new EntityEnderman(world);
        }
        if (mob.equals(EntityType.GHAST)){
            return new EntityGhast(world);
        }
        if (mob.equals(EntityType.GIANT)){
            return new EntityGiantZombie(world);
        }
        if (mob.equals(EntityType.HORSE)){
            return new EntityHorse(world);
        }
        if (mob.equals(EntityType.IRON_GOLEM)){
            return new EntityIronGolem(world);
        }
        if (mob.equals(EntityType.MAGMA_CUBE)){
            return new EntityMagmaCube(world);
        }
        if (mob.equals(EntityType.MUSHROOM_COW)){
            return new EntityMushroomCow(world);
        }
        if (mob.equals(EntityType.OCELOT)){
            return new EntityOcelot(world);
        }
        if (mob.equals(EntityType.PIG)){
            return new EntityPig(world);
        }
        if (mob.equals(EntityType.PIG_ZOMBIE)){
            return new EntityPigZombie(world);
        }
        if (mob.equals(EntityType.SHEEP)){
            return new EntitySheep(world);
        }
        if (mob.equals(EntityType.SILVERFISH)){
            return new EntitySilverfish(world);
        }
        if (mob.equals(EntityType.SKELETON)){
            return new EntitySkeleton(world);
        }
        if (mob.equals(EntityType.SLIME)){
            return new EntitySlime(world);
        }
        if (mob.equals(EntityType.SNOWMAN)){
            return new EntitySnowman(world);
        }
        if (mob.equals(EntityType.SPIDER)){
            return new EntitySpider(world);
        }
        if (mob.equals(EntityType.SQUID)){
            return new EntitySquid(world);
        }
        if (mob.equals(EntityType.VILLAGER)){
            return new EntityVillager(world);
        }
        if (mob.equals(EntityType.WITCH)){
            return new EntityWitch(world);
        }
        if (mob.equals(EntityType.WITHER)){
            return new EntityWither(world);
        }
        if (mob.equals(EntityType.WOLF)){
            return new EntityWolf(world);
        }
        if (mob.equals(EntityType.ZOMBIE)){
            return new EntityZombie(world);
        }
        return null;
	}
}
