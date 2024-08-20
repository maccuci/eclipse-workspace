package br.com.houldmc.rankup.api.enchantment.enchantments;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.api.enchantment.types.CustomEnchantment;

public class RemoveBedrockCustomEnchantment extends CustomEnchantment {
	
	public RemoveBedrockCustomEnchantment() {
		super("Remove Bedrock", 300, 1);
	}
	
	@EventHandler
	void breakBedrock(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack itemStack = player.getItemInHand();
		
		if(itemStack.containsEnchantment(this)) {
			if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
				if(event.getClickedBlock().getType() == Material.BEDROCK) {
					event.getClickedBlock().setType(Material.AIR);
					event.getClickedBlock().getWorld().dropItem(event.getClickedBlock().getLocation(), new ItemStack(Material.BEDROCK));
					
					itemStack.setDurability((short) ((short)itemStack.getDurability() - 200));
				}
			}
		}
	}
}
