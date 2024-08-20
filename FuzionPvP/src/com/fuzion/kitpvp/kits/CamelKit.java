package com.fuzion.kitpvp.kits;

import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.fuzion.core.master.account.Group;
import com.fuzion.kitpvp.manager.kit.Kit;

public class CamelKit extends Kit {
	
	public CamelKit() {
		super("Camel", new ItemStack(Material.SAND), Group.BETA, "§7Caminhe pela areia para ganhar efeitos.");
	}
	
	@EventHandler
	public void onCamel(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SAND || event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.SANDSTONE) {
			if(!hasKit(player))
				return;
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 210, 0));
		}
	}
}
