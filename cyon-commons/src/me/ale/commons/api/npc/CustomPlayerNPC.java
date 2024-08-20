package me.ale.commons.api.npc;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedWatchableObject;
import com.mojang.authlib.GameProfile;

import lombok.Getter;
import me.ale.commons.bukkit.BukkitMain;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;

@Getter
public class CustomPlayerNPC {
	
    private ProtocolManager protocolManager;
    private WrappedDataWatcher ghastWatcher;
    
    public void create(Player p){
        MinecraftServer nmsServer = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle();
       
        EntityPlayer npc = new EntityPlayer(nmsServer, nmsWorld, new GameProfile(UUID.randomUUID(), "NPC"), new PlayerInteractManager(nmsWorld));
       
        npc.setLocation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
       
        PlayerConnection connection = ((CraftPlayer) p).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, npc));
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
      }
    
    public CustomPlayerNPC() {
    	protocolManager = ProtocolLibrary.getProtocolManager();
	      ghastWatcher = getDefaultWatcher(BukkitMain.getPlugin().getServer().getWorlds().get(0), EntityType.GHAST);
	        
	        for (WrappedWatchableObject object : ghastWatcher)
	            System.out.println(object);
	}
	
	@SuppressWarnings("deprecation")
	public void sendPacket(Player p) {
        PacketContainer newPacket = new PacketContainer(24);

        newPacket.getIntegers().
            write(0, 500).
            write(1, (int) EntityType.GHAST.getTypeId()).
            write(2, (int) (p.getLocation().getX() * 32)).
            write(3, (int) (p.getLocation().getY() * 32)).
            write(4, (int) (p.getLocation().getZ() * 32));
        
        newPacket.getDataWatcherModifier().
            write(0, ghastWatcher);
        
        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(p, newPacket);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    
    public WrappedDataWatcher getDefaultWatcher(World world, EntityType type) {
        Entity entity = world.spawnEntity(new Location(world, 0, 256, 0), type);
        WrappedDataWatcher watcher = WrappedDataWatcher.getEntityWatcher(entity).deepClone();
        
        entity.remove();
        return watcher;
    }
}
