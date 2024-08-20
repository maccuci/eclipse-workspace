package com.fuzion.kitpvp.listeners;

import org.bukkit.event.player.*;

import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import org.bukkit.event.*;
import org.bukkit.event.hanging.*;
import org.bukkit.event.entity.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;

public class ItemFrameListener implements Listener {
	
	@EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent event) {
        if (!(event.getRightClicked() instanceof ItemFrame)) {
            return;
        }
        Player player = event.getPlayer();
        ItemFrame frame = (ItemFrame) event.getRightClicked();
        event.setCancelled(true);
        if (frame.getItem().getType() == Material.AIR) {
        	if (player.getGameMode() == GameMode.CREATIVE) {
        		ItemStack item = player.getItemInHand();
                if (item == null) {
                    return;
                }
                if (item.getType() == Material.MUSHROOM_SOUP) {
                    this.setMessage(frame, item, "§3§lSopas");
                }else if (item.getType() == Material.RED_MUSHROOM) {
                    this.setMessage(frame, item, "§c§lCogumelos Vermelhos");
                }
                else if (item.getType() == Material.BROWN_MUSHROOM) {
                    this.setMessage(frame, item, "§e§lCogumelos Marrons");
                }
                else if (item.getType() == Material.BOWL) {
                    this.setMessage(frame, item, "§7§lPotes");
                }
        	}
        	return;
        }
        final ItemStack item = frame.getItem();
        if (!item.hasItemMeta()) {
            return;
        }
        if (!item.getItemMeta().hasDisplayName()) {
            return;
        }
        final String line = item.getItemMeta().getDisplayName();
        if (line.toLowerCase().contains("sopas")) {
            final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 36, String.valueOf(ChatColor.DARK_AQUA.toString()) + ChatColor.BOLD + "Sopas");
            for (int i = 0; i < 36; ++i) {
                inv.setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
            }
            player.openInventory(inv);
            return;
        }
        if (line.toLowerCase().contains("marrons")) {
            final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 36, String.valueOf(ChatColor.YELLOW.toString()) + ChatColor.BOLD + "Cogumelos Marrons");
            for (int i = 0; i < 36; ++i) {
                inv.setItem(i, new ItemStack(Material.BROWN_MUSHROOM, 64));
            }
            player.openInventory(inv);
            return;
        }
        if (line.toLowerCase().contains("vermelhos")) {
            final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 36, String.valueOf(ChatColor.RED.toString()) + ChatColor.BOLD + "Cogumelos Vermelhos");
            for (int i = 0; i < 36; ++i) {
                inv.setItem(i, new ItemStack(Material.RED_MUSHROOM, 64));
            }
            player.openInventory(inv);
            return;
        }
        if (line.toLowerCase().contains("potes")) {
            final Inventory inv = Bukkit.createInventory((InventoryHolder)null, 36, String.valueOf(ChatColor.GRAY.toString()) + ChatColor.BOLD + "Potes");
            for (int i = 0; i < 36; ++i) {
                inv.setItem(i, new ItemStack(Material.BOWL, 64));
            }
            player.openInventory(inv);
            return;
        }
	}
	
	@EventHandler
    public void onHanging(final HangingBreakEvent event) {
        if (event.getCause() != HangingBreakEvent.RemoveCause.ENTITY) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onHanging(final HangingBreakByEntityEvent event) {
        if (!(event.getEntity() instanceof ItemFrame)) {
            return;
        }
        if (!(event.getRemover() instanceof Player)) {
            event.setCancelled(true);
            return;
        }
        final Player p = (Player)event.getRemover();
        if (p.getGameMode() != GameMode.CREATIVE || !p.hasPermission("fuzion.build")) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof ItemFrame)) {
            return;
        }
        if (!(event.getDamager() instanceof Player)) {
            return;
        }
        final Player p = (Player)event.getDamager();
        if (p.getGameMode() != GameMode.CREATIVE || !p.hasPermission("fuzion.build")) {
            event.setCancelled(true);
        }
    }
    
    private void setMessage(final ItemFrame frame, final ItemStack item, final String message) {
        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(message);
        item.setItemMeta(meta);
        frame.setItem(item);
        frame.setRotation(Rotation.NONE);
    }
}
