package br.com.houldmc.rankup.manager.kit;

import java.util.HashMap;

import br.com.houldmc.rankup.Main;
import br.com.houldmc.rankup.api.getter.ClassGetter;
import lombok.Getter;

public class KitManager {
	
	@Getter
	private static final HashMap<String, Kit> kits = new HashMap<>();
	
	public static void loadKits() {
			int i = 0;
			for (Class<?> abilityClass : ClassGetter.getClassesForPackage(Main.class, "br.com.houldmc.rankup.manager.kit.kits")) {
				if (Kit.class.isAssignableFrom(abilityClass)) {
					try {
						Kit abilityListener;
						try {
							abilityListener = (Kit) abilityClass.getConstructor(Main.class).newInstance(Main.getPlugin());
						} catch (Exception e) {
							abilityListener = (Kit) abilityClass.newInstance();
						}
						String kitName = abilityListener.getClass().getSimpleName().toLowerCase().replace("Kit", "");
						kits.put(kitName, abilityListener);
						System.out.print(kitName);
					} catch (Exception e) {
						e.printStackTrace();
						System.out.print("Erro ao carregar o kit " + abilityClass.getSimpleName());
					}
				i++;
			}
		}
		Main.getPlugin().getLogger().info(i + " kit carregados!");
	}
	
	public static Kit getKit(String kitName) {
		if (kits.containsKey(kitName.toLowerCase()))
			return kits.get(kitName.toLowerCase());
		else
			System.out.print("Tried to find ability '" + kitName + "' but failed!");
		return null;
	}
	
	public static Kit getKitTest(String kitName) {
		Kit kit = null;
		for(Kit k : kits.values()) {
			if(k.getName() == kitName)
				kit = k;
		}
		return kit;
	}
}
