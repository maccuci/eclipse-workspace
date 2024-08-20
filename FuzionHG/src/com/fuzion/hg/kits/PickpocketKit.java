package com.fuzion.hg.kits;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.Main;
import com.fuzion.hg.manager.kit.Kit;

import net.md_5.bungee.api.ChatColor;

public class PickpocketKit extends Kit {
	
	class Pick {
        int itemsStolen = 0;
        long lastUsed = 0;
        Player pickpocket = null;
    }
	
	public PickpocketKit() {
		super("Pickpocket", "Trap e Contra times", new ItemStack(Material.BLAZE_ROD), Group.BETA, "§7Roube no máximo 3 itens do inventário do seu oponente.");
	}
	
    private transient HashMap<ItemStack, Pick> pickpockets = new HashMap<>();
    private boolean stealHotbar = false;
    
    private void closedInv(Inventory inv, Player player) {
        List<Player> peverts = this.getPerverts(inv);
        if (peverts.contains(player)) {
            peverts.remove(player);
            if (peverts.size() == 0){
                ((Player) inv.getHolder()).removeMetadata("Picking", Main.getPlugin());
            }
            for (Pick pick : pickpockets.values()) {
                if (pick.pickpocket == player) {
                    pick.pickpocket = null;
                    pick.itemsStolen = 0;
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
	private List<Player> getPerverts(Inventory inv) {
        if (inv.getHolder() instanceof Player && ((Player) inv.getHolder()).hasMetadata("Picking")){
            return (List<Player>) ((Player) inv.getHolder()).getMetadata("Picking").get(0).value();
        }
        return new ArrayList<Player>();
    }

    private Pick getPick(Player thief) {
        for (Pick pick : pickpockets.values()){
            if (pick.pickpocket == thief){
                return pick;
            }
        }
        return null;
    }
    
    @SuppressWarnings("unchecked")
	@EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        ItemStack item = event.getPlayer().getItemInHand();
        if (event.getRightClicked() instanceof Player && item.getType() == Material.BLAZE_ROD && hasKit(event.getPlayer())) {
            Pick pick = new Pick();
            if (pickpockets.containsKey(item))
                pick = pickpockets.get(item);
            if (pick.lastUsed > System.currentTimeMillis()) {
                event.getPlayer().sendMessage(String.format(ChatColor.RED + "Aguarde %s segundo(s)!", +(-((System.currentTimeMillis() - pick.lastUsed) / 1000))));
            }else{
                pickpockets.put(item, pick);
                pick.lastUsed = System.currentTimeMillis() + (30 * 1000);
                pick.pickpocket = event.getPlayer();
                List<Player> pickers = new ArrayList<Player>();
                if (event.getRightClicked().hasMetadata("Picking")){
                    pickers = (List<Player>) event.getRightClicked().getMetadata("Picking").get(0).value();
                }
                pickers.add(event.getPlayer());
                event.getRightClicked().setMetadata("Picking", new FixedMetadataValue(Main.getPlugin(), pickers));
                event.getPlayer().openInventory(((Player) event.getRightClicked()).getInventory());
            }
        }
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent event) {
        if (event.getCurrentItem() != null && pickpockets.containsKey(event.getCurrentItem()) && pickpockets.get(event.getCurrentItem()).pickpocket != null) {
            event.setCancelled(true);
            ((Player) event.getWhoClicked()).sendMessage(ChatColor.RED + "Você não pode roubar isso!");
            ((Player) event.getWhoClicked()).updateInventory();
        }
        List<Player> peverts = this.getPerverts(event.getInventory());
        if (peverts.contains(event.getWhoClicked())) {
            if (event.getRawSlot() < 36) {
                Pick pick = getPick((Player) event.getWhoClicked());
                if (pick.itemsStolen < 4) {
                    if (stealHotbar || event.getRawSlot() > 8) {
                        if (event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR){
                            pick.itemsStolen++;
                        }
                    }else{
                        event.setCancelled(true);
                        ((Player) event.getWhoClicked()).sendMessage(ChatColor.RED + "Você não pode roubar itens da hotbar!");
                        ((Player) event.getWhoClicked()).updateInventory();
                    }
                }else {
                    event.setCancelled(true);
                    ((Player) event.getWhoClicked()).sendMessage(ChatColor.RED + "Você já roubou o maximo de itens possíveis!");
                    ((Player) event.getWhoClicked()).updateInventory();
                }
            }
        }
    }
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        closedInv(event.getInventory(), (Player) event.getPlayer());
    }
    
    @EventHandler
    public void onKilled(PlayerDeathEvent event) {
        for (HumanEntity entity : event.getEntity().getInventory().getViewers()){
            closedInv(event.getEntity().getPlayer().getInventory(), (Player) entity);
        }
        closedInv(event.getEntity().getPlayer().getInventory(), event.getEntity().getKiller().getPlayer());
    }
    
    @SuppressWarnings("unchecked")
	@EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.getPlayer().hasMetadata("Picking")) {
            List<Player> pickers = (List<Player>) event.getPlayer().getMetadata("Picking").get(0).value();
            List<Player> cloned = new ArrayList<Player>();
            for (Player p : pickers){
                cloned.add(p);
            }
            for (Player picker : cloned){
                if (event.getTo().distance(picker.getLocation()) > 6) {
                    picker.closeInventory();
                }
            }
        }
    }
}
