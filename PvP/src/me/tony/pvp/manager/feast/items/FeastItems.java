package me.tony.pvp.manager.feast.items;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FeastItems implements Items {
	private static Random r = new Random();

	@Override
	public List<ItemStack> generateItems() {
		List<ItemStack> feastItems = new ArrayList<>();
		feastItems.addAll(addItem(Material.DIAMOND_SWORD, 1, 3));
		feastItems.addAll(addItem(Material.DIAMOND_CHESTPLATE, 1, 3));
		feastItems.addAll(addItem(Material.MUSHROOM_SOUP, 10, 25));
		feastItems.addAll(addItem(Material.ARROW, 12, 36));
		feastItems.addAll(addItem(Material.BOW, 1, 6));
		feastItems.addAll(addItem(Material.GOLDEN_APPLE, 6, 12));
		feastItems.addAll(addItem(Material.POTION, (short) 16385, 0, 5));
		feastItems.addAll(addItem(Material.POTION, (short) 16386, 0, 5));
		feastItems.addAll(addItem(Material.POTION, (short) 16387, 0, 5));
		feastItems.addAll(addItem(Material.POTION, (short) 16388, 0, 5));
		feastItems.addAll(addItem(Material.POTION, (short) 16389, 0, 5));
		feastItems.addAll(addItem(Material.POTION, (short) 16393, 0, 3));
		feastItems.addAll(addItem(Material.POTION, (short) 16394, 0, 5));
		feastItems.addAll(addItem(Material.POTION, (short) 16396, 0, 5));
		Collections.shuffle(feastItems);
		return feastItems;
	}

	private List<ItemStack> addItem(Material mat, int min, int max) {
		return addItem(mat, (short) 0, min, max);
	}

	private List<ItemStack> addItem(Material mat, short durability, int min, int max) {
		List<ItemStack> items = new ArrayList<>();
		for (int i = 0; i <= min + r.nextInt(max - min); i++) {
			items.add(new ItemStack(mat, 1, durability));
		}
		return items;
	}

}
