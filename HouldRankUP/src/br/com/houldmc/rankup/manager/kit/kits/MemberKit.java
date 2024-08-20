package br.com.houldmc.rankup.manager.kit.kits;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.manager.kit.Kit;

public class MemberKit extends Kit {
	
	private List<ItemStack> items = getItems();
	
	public MemberKit() {
		super("Membro", new ArrayList<>());
		items.add(new ItemBuilder().type(Material.IRON_SWORD).enchantment(Enchantment.DAMAGE_ALL, 10).enchantment(Enchantment.DURABILITY, 3).build());
		items.add(new ItemBuilder().type(Material.CHAINMAIL_HELMET).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20).enchantment(Enchantment.DURABILITY, 3).build());
		items.add(new ItemBuilder().type(Material.CHAINMAIL_CHESTPLATE).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20).enchantment(Enchantment.DURABILITY, 3).build());
		items.add(new ItemBuilder().type(Material.CHAINMAIL_LEGGINGS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20).enchantment(Enchantment.DURABILITY, 3).build());
		items.add(new ItemBuilder().type(Material.CHAINMAIL_BOOTS).enchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 20).enchantment(Enchantment.DURABILITY, 3).build());
		items.add(new ItemBuilder().type(Material.GOLDEN_APPLE).amount(25).build());
	}

	@Override
	public void givePlayer(Player player) {
		for(ItemStack itemStack : getItems()) {
			player.getInventory().addItem(itemStack);
		}
	}

}
