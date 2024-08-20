package me.ale.pvp.api.npc;

import me.ale.pvp.manager.Reflections;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
 
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.DataWatcher;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import net.minecraft.server.v1_8_R3.PacketPlayOutBed;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityStatus;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_8_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_8_R3.WorldSettings.EnumGamemode;
 
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;
 
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class FakePlayerAPI extends Reflections {
	
	private int entityID;
    private Location location;
    private GameProfile gameprofile;
    private String skinUrl;
   
   
    public FakePlayerAPI(String name,Location location, String skinUrl){
            entityID = (int)Math.ceil(Math.random() * 1000) + 2000;
            gameprofile = new GameProfile(UUID.randomUUID(), name);
            changeSkin();
            this.location = location.clone();
            this.skinUrl = skinUrl;
    }
   
    public void changeSkin(){
            gameprofile.getProperties().put("textures", new Property("textures", Base64.getEncoder().encodeToString(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinUrl).getBytes(StandardCharsets.UTF_8))));
    }
   
   
    public void animation(int animation){
            PacketPlayOutAnimation packet = new PacketPlayOutAnimation();
            setValue(packet, "a", entityID);
            setValue(packet, "b", (byte)animation);
            sendPacket(packet);
    }
   
    public void status(int status){
            PacketPlayOutEntityStatus packet = new PacketPlayOutEntityStatus();
            setValue(packet, "a", entityID);
            setValue(packet, "b", (byte)status);
            sendPacket(packet);
    }
   
    public void equip(int slot,ItemStack itemstack){
            PacketPlayOutEntityEquipment packet = new PacketPlayOutEntityEquipment();
            setValue(packet, "a", entityID);
            setValue(packet, "b", slot);
            setValue(packet, "c", itemstack);
            sendPacket(packet);
    }
   
    @SuppressWarnings("deprecation")
	public void sleep(boolean state){
            if(state){
                    Location bedLocation = new Location(location.getWorld(), 1, 1, 1);
                    PacketPlayOutBed packet = new PacketPlayOutBed();
                    setValue(packet, "a", entityID);
                    setValue(packet, "b", new BlockPosition(bedLocation.getX(),bedLocation.getY(),bedLocation.getZ()));
                   
                    for(Player pl : Bukkit.getOnlinePlayers()){
                            pl.sendBlockChange(bedLocation, Material.BED_BLOCK, (byte)0);
                    }

                    sendPacket(packet);
                    teleport(location.clone().add(0,0.3,0));
            }else{
                    animation(2);
                    teleport(location.clone().subtract(0,0.3,0));
            }
    }
   
    public void spawn(){
            PacketPlayOutNamedEntitySpawn packet = new PacketPlayOutNamedEntitySpawn();
           
            setValue(packet, "a", entityID);
            setValue(packet, "b", gameprofile.getId());
            setValue(packet, "c", getFixLocation(location.getX()));
            setValue(packet, "d", getFixLocation(location.getY()));
            setValue(packet, "e", getFixLocation(location.getZ()));
            setValue(packet, "f", getFixRotation(location.getYaw()));
            setValue(packet, "g", getFixRotation(location.getPitch()));
            setValue(packet, "h", 0);
            DataWatcher w = new DataWatcher(null);
            w.a(6,(float)20);
            w.a(10,(byte)127);
            setValue(packet, "i", w);
            addToTablist();
            sendPacket(packet);
            headRotation(location.getYaw(), location.getPitch());
    }
   
    public void teleport(Location location){
            PacketPlayOutEntityTeleport packet = new PacketPlayOutEntityTeleport();
            setValue(packet, "a", entityID);
            setValue(packet, "b", getFixLocation(location.getX()));
            setValue(packet, "c", getFixLocation(location.getY()));
            setValue(packet, "d", getFixLocation(location.getZ()));
            setValue(packet, "e", getFixRotation(location.getYaw()));
            setValue(packet, "f", getFixRotation(location.getPitch()));

            sendPacket(packet);
            headRotation(location.getYaw(), location.getPitch());
            this.location = location.clone();
    }
   
    public void headRotation(float yaw,float pitch){
            PacketPlayOutEntityLook packet = new PacketPlayOutEntityLook(entityID, getFixRotation(yaw),getFixRotation(pitch) , true);
            PacketPlayOutEntityHeadRotation packetHead = new PacketPlayOutEntityHeadRotation();
            setValue(packetHead, "a", entityID);
            setValue(packetHead, "b", getFixRotation(yaw));
           

            sendPacket(packet);
            sendPacket(packetHead);
    }
   
    public void destroy(){
            PacketPlayOutEntityDestroy packet = new PacketPlayOutEntityDestroy(new int[] {entityID});
            rmvFromTablist();
            sendPacket(packet);
    }
   
    public void addToTablist(){
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
            PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
            @SuppressWarnings("unchecked")
            List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
            players.add(data);
           
            setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER);
            setValue(packet, "b", players);
           
            sendPacket(packet);
    }
   
    public void rmvFromTablist(){
            PacketPlayOutPlayerInfo packet = new PacketPlayOutPlayerInfo();
            PacketPlayOutPlayerInfo.PlayerInfoData data = packet.new PlayerInfoData(gameprofile, 1, EnumGamemode.NOT_SET, CraftChatMessage.fromString(gameprofile.getName())[0]);
            @SuppressWarnings("unchecked")
            List<PacketPlayOutPlayerInfo.PlayerInfoData> players = (List<PacketPlayOutPlayerInfo.PlayerInfoData>) getValue(packet, "b");
            players.add(data);
           
            setValue(packet, "a", PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER);
            setValue(packet, "b", players);
           
            sendPacket(packet);
    }
   
    public int getFixLocation(double pos){
            return (int)MathHelper.floor(pos * 32.0D);
    }
   
    public int getEntityID() {
            return entityID;
    }
   
    public byte getFixRotation(float yawpitch){
            return (byte) ((int) (yawpitch * 256.0F / 360.0F));
    }

}
