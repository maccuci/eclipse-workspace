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

public class PoseidonKit extends Kit {
	
	public PoseidonKit() {
		super("Poseidon", new ItemStack(Material.WATER_BUCKET), Group.BETA, "§7Entre na água para ganhar efeitos.");
	}
	
	@EventHandler
	public void onPoseidon(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		
		if(event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.WATER || event.getTo().getBlock().getRelative(BlockFace.DOWN).getType() == Material.STATIONARY_WATER) {
			if(!hasKit(player))
				return;
			player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 210, 0));
			player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 210, 0));
		}
	}
}
