package com.fuzion.hg.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class NameUtils {

	public static String formatString(String string) {
		char[] stringArray = string.toLowerCase().toCharArray();
		stringArray[0] = Character.toUpperCase(stringArray[0]);
		return new String(stringArray);
	}
	
	public static String getEnchantName(Enchantment enchant) {
		return getName(enchant.getName());
	}

	public static String getItemName(ItemStack item) {
		if (item == null)
			item = new ItemStack(Material.AIR);
		return getName(item.getType().name());
	}

	public static String getName(String string) {
		return toReadable(string);
	}

	public static String toReadable(String string) {
		String[] names = string.split("_");
		for (int i = 0; i < names.length; i++) {
			names[i] = names[i].substring(0, 1) + names[i].substring(1).toLowerCase();
		}
		return StringUtils.join(names, " ");
	}

}