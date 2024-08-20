package me.tony.he4rt.cosmetics.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import me.tony.he4rt.cosmetics.api.TonyMCAPI;
import me.tony.he4rt.cosmetics.list.cosmetics.gadgets.GadgetTNT;
import me.tony.he4rt.cosmetics.list.cosmetics.gadgets.GadgetTsunami;
import me.tony.he4rt.cosmetics.menu.CosmeticsMenu;

public class PlayerListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		TonyMCAPI itemBuilder = new TonyMCAPI();
		
		player.getInventory().addItem(itemBuilder.type(Material.TNT).name("§aTNT §7(Clique)").build());
		player.getInventory().addItem(itemBuilder.type(Material.WATER_BUCKET).name("§aTsunami §7(Clique)").build());
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		
		if(TonyMCAPI.checkItem(player.getItemInHand(), "§aTNT §7(Clique)")) {
			GadgetTNT gadgetTNT = new GadgetTNT(player);
			
			gadgetTNT.onRightClick();
		}
		
		if(TonyMCAPI.checkItem(player.getItemInHand(), "§aTsunami §7(Clique)")) {
			GadgetTsunami gadgetTsunami = new GadgetTsunami(player);
			
			event.setCancelled(true);
			gadgetTsunami.onRightClick();
		}
	}
	
	@EventHandler
	public void test(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		
		CosmeticsMenu.open(player);
	}
}
