package br.com.houldmc.rankup.api.enchantment;

import java.lang.reflect.Field;
import java.util.HashMap;

import org.bukkit.enchantments.Enchantment;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.api.enchantment.types.CustomEnchantment;

public class EnchantmentAPI {
	
	@SuppressWarnings({"unchecked", "unlikely-arg-type" })
	public void reset(CustomEnchantment enchantment) {
		try {
			Field byIdField = Enchantment.class.getDeclaredField("byId");
			Field byNameField = Enchantment.class.getDeclaredField("byName");

			byIdField.setAccessible(true);
			byNameField.setAccessible(true);

			HashMap<Integer, Enchantment> byId = (HashMap<Integer, Enchantment>) byIdField.get(null);
			HashMap<Integer, Enchantment> byName = (HashMap<Integer, Enchantment>) byNameField.get(null);

			if (byId.containsKey(enchantment.getId())) {
				byId.remove(enchantment.getId());
			}

			if (byName.containsKey(enchantment.getName())) {
				byName.remove(enchantment.getName());
			}
		} catch (Exception ignored) {
		}
	}
	
	public void loadEnchantment(CustomEnchantment enchantment) {
		try {
			try {
				Field f = Enchantment.class.getDeclaredField("acceptingNew");
				f.setAccessible(true);
				f.set(null, true);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Enchantment.registerEnchantment(enchantment);
				Main.getPlugin().getServer().getPluginManager().registerEvents(enchantment, Main.getPlugin());
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
