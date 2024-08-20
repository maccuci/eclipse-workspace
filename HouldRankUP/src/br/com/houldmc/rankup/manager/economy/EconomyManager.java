package br.com.houldmc.rankup.manager.economy;

import java.util.HashMap;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.api.getter.ClassGetter;
import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import lombok.Getter;

public class EconomyManager {
	
	@Getter
	private static final HashMap<String, ItemEconomy> items = new HashMap<>();
	
	public static void loadItems() {
			int i = 0;
			for (Class<?> abilityClass : ClassGetter.getClassesForPackage(Main.class, "br.com.houldmc.rankup.manager.economy.items")) {
				if (ItemEconomy.class.isAssignableFrom(abilityClass)) {
					try {
						ItemEconomy abilityListener;
						try {
							abilityListener = (ItemEconomy) abilityClass.getConstructor(Main.class).newInstance(Main.getPlugin());
						} catch (Exception e) {
							abilityListener = (ItemEconomy) abilityClass.newInstance();
						}
						String kitName = abilityListener.getClass().getSimpleName().toLowerCase().replace("ItemType", "");
						items.put(kitName, abilityListener);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.print("Erro ao carregar o item " + abilityClass.getSimpleName());
					}
					i++;
				}
				Main.getPlugin().getLogger().info(i + " items carregados!");
		}
	}
}
