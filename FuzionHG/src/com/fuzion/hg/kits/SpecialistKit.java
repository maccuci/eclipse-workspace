package com.fuzion.hg.kits;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;

public class SpecialistKit extends Kit {
	
	public SpecialistKit() {
		super("Specialist", "Não encontrado", new ItemStack(Material.BOOK), Group.BETA, "§7Carregue consigo uma mesa de encantamentos e ganhe experiência ao matar jogadores.");
		addItem(new ItemStack(Material.BOOK, 1));
	}
	
    @EventHandler
    public void onKilled(PlayerDeathEvent event) {
    	if (event.getEntity().getKiller() != null && hasKit(event.getEntity().getPlayer())) {
        	Player p = event.getEntity().getKiller().getPlayer();
        	if(!p.getInventory().contains(new ItemStack(Material.EXP_BOTTLE))){
        		p.getInventory().removeItem(new ItemStack(Material.MUSHROOM_SOUP));
        	}
            p.getInventory().addItem(new ItemStack(Material.EXP_BOTTLE, 1));
        }
    }
	
	@EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && hasKit(event.getPlayer())) {
            Player p = event.getPlayer();
            if (p.getItemInHand().getType() == Material.ENCHANTED_BOOK) {
                p.openEnchanting(null, true);
            }
        }
    }
}
