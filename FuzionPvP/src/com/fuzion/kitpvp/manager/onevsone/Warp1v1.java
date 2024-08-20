package com.fuzion.kitpvp.manager.onevsone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.fuzion.kitpvp.Main;
import com.fuzion.kitpvp.event.PlayerWarpJoinEvent;
import com.fuzion.kitpvp.manager.kit.KitManager;
import com.fuzion.kitpvp.manager.protection.ProtectionManager;
import com.fuzion.kitpvp.manager.warp.WarpManager;
import com.fuzion.kitpvp.manager.warp.Warps;
import com.fuzion.core.api.cooldown.CooldownAPI;
import com.fuzion.core.api.date.DateUtils;
import com.fuzion.core.api.item.ItemBuilder;

public class Warp1v1 implements Listener {
	
	public enum ChallengeType {
		DEFAULT, CUSTOM;
	}
	
	public static List<Player> playersIn1v1 = new ArrayList<>();
	public static List<Player> playersInQueue = new ArrayList<>();
	public static HashMap<Player, Player> fighting = new HashMap<>();
	public static HashMap<Player, Player> invites = new HashMap<>();
	
	@EventHandler
	public void onWarp(PlayerWarpJoinEvent event) {
		Player player = event.getPlayer();
		if(event.getWarp() == "1v1") {
			WarpManager.teleport(player, Warps.ONE_VS_ONE);
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = event.getItem();
		
		if(new KitManager().getKitName(player) != "Nenhum")
			return;
		if (item == null) {
            return;
        }
        if (!event.getAction().toString().contains("RIGHT")) {
            return;
        }
        if (item.getType() != Material.INK_SACK) {
            return;
        }
		
		if (item.getDurability() == 8) {
			if (playersInQueue.contains(player)) {
				item.setDurability((short)10);
				player.sendMessage("§eVocê já esta na lista de espera, aguarde...");
                return;
			}
			if(playersInQueue.size() > 0) {
				Player p = playersInQueue.get(0);
				if (p != null) {
					player.sendMessage("§bO 1v1 rápido encontrou o jogador §e" + p.getName() + " §bpara batalhar com você.");
					p.sendMessage("§bO 1v1 rápido encontrou o jogador §e" + player.getName() + " §bpara batalhar com você.");
					send1v1(new Challenge(player, p));
					return;
				}
			}
			item.setDurability((short)10);
			playersInQueue.add(player);
			player.sendMessage("§e1v1 rápido está procurando alguém...");
		} else {
			if (!playersInQueue.contains(player)) {
                player.sendMessage("§eVocê não está na lista de espera.");
                item.setDurability((short)8);
                return;
			}
			item.setDurability((short)8);
            player.sendMessage("§eVocê saiu da lista de espera para o 1v1 rápido.");
            playersInQueue.remove(player);
		}
	}
	@EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
		 Player player = event.getPlayer();
	     ItemStack item = player.getItemInHand();
	     
	     if(new KitManager().getKitName(player) != "Nenhum") {
	    	 return;
	     }
		if (!(event.getRightClicked() instanceof Player)) {
			return;
		}
		if (item == null) {
			return;
		}
		if(item.getType() == Material.BLAZE_ROD) {
			Player target = (Player)event.getRightClicked();
			
			if(fighting.containsKey(target)) {
				player.sendMessage("§cEste jogador já se encontra em uma 1v1.");
				return;
			}
			if(CooldownAPI.isInCooldown(player.getUniqueId(), "1v1")) {
				player.sendMessage("§cEspere mais " + DateUtils.formatDifference(CooldownAPI.getTimeLeft(player.getUniqueId(), "1v1")) + " para desafiar novamente.");
				return;
			}
			if(invites.containsKey(player) && invites.containsKey(target)) {
				send1v1(new Challenge(player, target));
				return;
			}
			
			invites.put(player, target);
			new BukkitRunnable() {
				
				@Override
				public void run() {
					invites.remove(target);
				}
			}.runTaskLater(Main.getPlugin(), 300);
			player.sendMessage("§eVocê desafiou o jogador " + target.getName() + " para uma batalha.");
			target.sendMessage("§eVocê foi desafiado pelo jogador " + player.getName() + " para uma batalha.");
			CooldownAPI cooldown = new CooldownAPI(player.getUniqueId(), "1v1", 3);
			cooldown.start();
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		if(!fighting.containsKey(player))
			return;
		Player p = Bukkit.getPlayer(fighting.get(player).getName());
		
		if(fighting.containsKey(player) || fighting.containsKey(p)) {
			fighting.remove(player);
			fighting.remove(p);
			p.sendMessage("§bVocê venceu a batalha pois, o seu desafiante deslogou!");
			p.teleport(WarpManager.getLocation("1v1Spawn"));
			p.getInventory().setItem(3, new ItemBuilder().type(Material.BLAZE_ROD).name("§eDesafie um Jogador").build());
			p.getInventory().setItem(4, new ItemBuilder().type(Material.WORKBENCH).name("§cDesafio Customizado").build());
			p.getInventory().setItem(5, new ItemBuilder().type(Material.INK_SACK).name("§e1v1 Rápido").durability(8).build());
		}
	}
	
	@SuppressWarnings("deprecation")
	public void send1v1(Challenge challenge) {
		if(challenge == null)
			return;
		ProtectionManager.removeProtectionToPlayer(challenge.getChallenger());
		ProtectionManager.removeProtectionToPlayer(challenge.getChallenged());
		Player challenger = challenge.getChallenger();
		Player challenged = challenge.getChallenged();
		if (playersInQueue.contains(challenger)) {
            playersInQueue.remove(challenger);
        }
        if (playersInQueue.contains(challenged)) {
            playersInQueue.remove(challenged);
        }
        fighting.put(challenger, challenged);
        fighting.put(challenged, challenger);
        challenger.getInventory().setArmorContents(new ItemStack[0]);
        challenger.getInventory().setContents(new ItemStack[0]);
        challenged.getInventory().setArmorContents(new ItemStack[0]);
        challenged.getInventory().setContents(new ItemStack[0]);
		for(int i = 0; i < 9; i++) {
			challenger.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
			challenged.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
		}
//        boolean refil = challenge.hasRefil();
//        boolean recraft = challenge.hasRecraft();
        boolean strenght = challenge.hasStrenght();
        challenger.getInventory().setHelmet(challenge.getHelmet());
        challenger.getInventory().setChestplate(challenge.getChestplate());
        challenger.getInventory().setLeggings(challenge.getLeggins());
        challenger.getInventory().setBoots(challenge.getBoots());
        challenged.getInventory().setHelmet(challenge.getHelmet());
        challenged.getInventory().setChestplate(challenge.getChestplate());
        challenged.getInventory().setLeggings(challenge.getLeggins());
        challenged.getInventory().setBoots(challenge.getBoots());
        challenger.getInventory().setItem(0, new ItemBuilder().type(Material.DIAMOND_SWORD).name("§bDiamond Sword").enchantment(Enchantment.DAMAGE_ALL).build());
        challenged.getInventory().setItem(0, new ItemBuilder().type(Material.DIAMOND_SWORD).name("§bDiamond Sword").enchantment(Enchantment.DAMAGE_ALL).build());
        if(strenght) {
        	challenger.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2000000, 0));
            challenged.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 2000000, 0));
        }
        challenger.teleport(WarpManager.getLocation("1v1pos1"));
        challenged.teleport(WarpManager.getLocation("1v1pos2"));
		for (Player online : Bukkit.getOnlinePlayers()) {
			online.hidePlayer(challenger);
			online.hidePlayer(challenged);
		}
		challenger.showPlayer(challenged);
		challenged.showPlayer(challenger);
	}
	
    public String getNextSwordLevel(final String str) {
        switch (str) {
            case "DIAMOND": {
                return "WOOD";
            }
            case "IRON": {
                return "DIAMOND";
            }
            case "WOOD": {
                return "STONE";
            }
            case "STONE": {
                return "IRON";
            }
            default:
                break;
        }
        return "";
    }
    
    public String getNextArmorLevel(final String str) {
        switch (str) {
            case "DIAMOND": {
                return "GLASS";
            }
            case "CHAINMAIL": {
                return "IRON";
            }
            case "IRON": {
                return "DIAMOND";
            }
            case "GLASS": {
                return "LEATHER";
            }
            case "LEATHER": {
                return "CHAINMAIL";
            }
            default:
                break;
        }
        return "";
    }
    
    public static boolean isIn1v1(Player player) {
        return playersIn1v1.contains(player);
    }
}
