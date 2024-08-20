package br.com.houldmc.rankup.player.listener;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.manager.mine.MineManager;
import br.com.houldmc.rankup.manager.mine.list.SelectPos;

public class WandListener implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		SelectPos selectPos = MineManager.selectPos.get(event.getPlayer().getUniqueId());
		if(selectPos == null)
			return;
		
		if (ItemBuilder.checkItem(event.getPlayer().getItemInHand(), "§aSelecionar posições")) {
			if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
				Block clicked = event.getClickedBlock();
				
				selectPos.setLocation1(clicked.getLocation());
				event.getPlayer()
				.sendMessage("§6§lWand §7» §aA primeira localização foi setada §6(" + clicked.getLocation().getBlockX() + ","
						+ clicked.getLocation().getBlockY() + "," + clicked.getLocation().getBlockZ() + ").");

			} else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
				Block clicked = event.getClickedBlock();
				selectPos.setLocation2(clicked.getLocation());
				event.getPlayer()
						.sendMessage("§6§lWand §7» §aA segunda localização foi setada §6(" + clicked.getLocation().getBlockX() + ","
								+ clicked.getLocation().getBlockY() + "," + clicked.getLocation().getBlockZ() + ").");
			}
			event.setCancelled(true);
		}
	}
}
