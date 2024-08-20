package com.fuzion.hg.kits;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

import net.md_5.bungee.api.ChatColor;

public class CheckpointKit extends Kit {
	
	private HashMap<Player, Location> register = new HashMap<>();
	
	public CheckpointKit() {
		super("Checkpoint", "Teleporte", new ItemStack(Material.NETHER_FENCE), Group.BETA, "§7Salve uma localização e depois teleporte para a mesma.");
		addItem(new ItemStack(Material.NETHER_FENCE));
		addItem(new ItemStack(Material.FLOWER_POT_ITEM));
	}
	
	@SuppressWarnings("unlikely-arg-type")
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Block block = event.getClickedBlock();
		if (hasKit(player) && player.getItemInHand() != null && player.getItemInHand().getType() == Material.FLOWER_POT_ITEM){
			event.setCancelled(true);
			if (!register.containsKey(player)) {
				player.sendMessage(ChatColor.RED + "Você não salvou nenhum local!");
				return;
			}
			if (GladiatorKit.inGladiator(player)){
				player.sendMessage(ChatColor.RED + "Você não pode usar esse kit durante uma batalha com um Gladiator!");
				return;
			}
			Location loc = (Location)register.get(player);
			player.teleport(loc);
			loc.getBlock().setType(Material.AIR);
			register.remove(player);
			player.playSound(player.getLocation(), Sound.PISTON_RETRACT, 1.0F, 2.0F);
			ItemStack s = new ItemStack(Material.NETHER_FENCE, 1);
			ItemMeta sm = s.getItemMeta();
			sm.setDisplayName(ChatColor.GOLD + "Salvar");
			s.setItemMeta(sm);
			if(player.getInventory().contains(Material.FLOWER_POT_ITEM)){
				player.getInventory().removeItem(new ItemStack(Material.FLOWER_POT_ITEM));
			}
			if(!player.getInventory().contains(Material.NETHER_FENCE)){
				player.getInventory().addItem(s);
			}
			player.sendMessage(ChatColor.GREEN + "Você teleportou-se para seu checkpoint!");
		}else if (event.getAction().name().contains("LEFT") && block != null && block.getType() == Material.NETHER_FENCE && register.containsKey(block.getLocation())) {
			Player p = (Player)register.get(block.getLocation());
			p.getWorld().createExplosion(block.getLocation().add(0.0D, 1.0D, 0.0D), 0.0F);
			block.setType(Material.AIR);
			ItemStack s = new ItemStack(Material.NETHER_FENCE, 1);
			ItemMeta sm = s.getItemMeta();
			sm.setDisplayName(ChatColor.GOLD + "Salvar");
			s.setItemMeta(sm);
			if(p.getInventory().contains(Material.FLOWER_POT_ITEM)){
				p.getInventory().removeItem(new ItemStack(Material.FLOWER_POT_ITEM));
			}
			if(!player.getInventory().contains(Material.NETHER_FENCE)){
				p.getInventory().addItem(s);
			}
			p.sendMessage(ChatColor.RED + "O seu checkpoint foi quebrado!");
			register.remove(p);
		}
	}
  
	@EventHandler
	public void onPlace(BlockPlaceEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		Block block = event.getBlockPlaced();
		Location l = block.getLocation();
		if (hasKit(player) && block != null && block.getType() == Material.NETHER_FENCE){
			if(!register.containsKey(player)){
				register.put(player, l);
				player.playSound(l, Sound.CAT_MEOW, 1.0F, 2.0F);
				player.sendMessage("Você salvou este local (" + l.getX() + ", " + l.getY() + ", " + l.getZ() + ").");
			}else{
				event.setCancelled(true);
				ItemStack s = new ItemStack(Material.FLOWER_POT_ITEM, 1);
				ItemMeta sm = s.getItemMeta();
				sm.setDisplayName(ChatColor.GOLD + "Usar");
				s.setItemMeta(sm);
				if(player.getInventory().contains(Material.NETHER_FENCE)){
					player.getInventory().removeItem(new ItemStack(Material.NETHER_FENCE));
				}
				if(!player.getInventory().contains(Material.FLOWER_POT_ITEM)){
					player.getInventory().addItem(s);
				}
			}
			return;
		}
		if(hasKit(player) && block != null && block.getType() == Material.FLOWER_POT_ITEM){
			event.setCancelled(true);
			if (!register.containsKey(player)) {
				player.sendMessage(ChatColor.RED + "Você não salvou nenhum local!");
				return;
			}
			if (GladiatorKit.inGladiator(player)){
				player.sendMessage(ChatColor.RED + "Você não pode usar esse kit durante uma batalha com um Gladiator!");
				return;
			}
			Location loc = (Location)register.get(player);
			player.teleport(loc);
			loc.getBlock().setType(Material.AIR);
			register.remove(player);
			player.playSound(player.getLocation(), Sound.PISTON_RETRACT, 1.0F, 2.0F);
			ItemStack s = new ItemStack(Material.NETHER_FENCE, 1);
			ItemMeta sm = s.getItemMeta();
			sm.setDisplayName(ChatColor.GOLD + "Salvar");
			s.setItemMeta(sm);
			if(player.getInventory().contains(Material.FLOWER_POT_ITEM)){
				player.getInventory().removeItem(new ItemStack(Material.FLOWER_POT_ITEM));
			}
			if(!player.getInventory().contains(Material.NETHER_FENCE)){
				player.getInventory().addItem(s);
			}
			player.sendMessage(ChatColor.GREEN + "Você teleportou-se para seu checkpoint!");
			return;
		}
	}
}
