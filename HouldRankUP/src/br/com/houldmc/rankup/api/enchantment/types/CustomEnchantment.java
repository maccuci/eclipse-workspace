package br.com.houldmc.rankup.api.enchantment.types;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public abstract class CustomEnchantment extends Enchantment implements Listener {

	private String name;
	private int id, maxLevel;
	
	public CustomEnchantment(String name, int id, int maxLevel) {
		super(id);
		this.id = id;
		this.name = name;
		this.maxLevel = maxLevel;
		
	}
	
	@Override
	public int getId() {
		return id;
	}

	@Override
	public boolean canEnchantItem(ItemStack arg0) {
		return true;
	}

	@Override
	public boolean conflictsWith(Enchantment arg0) {
		return false;
	}

	@Override
	public EnchantmentTarget getItemTarget() {
		return null;
	}

	@Override
	public int getMaxLevel() {
		return maxLevel;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getStartLevel() {
		return 1;
	}
}
