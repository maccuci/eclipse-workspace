package me.feenks.pvp.manager.ability;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import lombok.Getter;
import me.feenks.core.bukkit.api.item.ItemBuilder;
import me.feenks.core.master.utils.ClassGetter;
import me.feenks.pvp.Main;

public class AbilityManager {
	
	private static final HashMap<UUID, Ability> currentAbility = new HashMap<>();
	@Getter private static final HashMap<String, Ability> abilities = new HashMap<>();
	
	public static Ability getAbility(String kitName) {
		if (abilities.containsKey(kitName.toLowerCase()))
			return abilities.get(kitName.toLowerCase());
		else
			System.out.print("Tried to find ability '" + kitName + "' but failed!");
		return null;
	}
	
	public static void initializateAbilities(String packageName) {
		int i = 0;
		for (Class<?> abilityClass : ClassGetter.getClassesForPackage(Main.class, packageName)) {
			if (Ability.class.isAssignableFrom(abilityClass)) {
				try {
					Ability abilityListener;
					try {
						abilityListener = (Ability) abilityClass.getConstructor(Main.class).newInstance(Main.getPlugin());
					} catch (Exception e) {
						abilityListener = (Ability) abilityClass.newInstance();
					}
					String kitName = abilityListener.getClass().getSimpleName().toLowerCase().replace("ability", "");
					abilities.put(kitName, abilityListener);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar a habilidade " + abilityClass.getSimpleName());
				}
				i++;
			}
			if(Listener.class.isAssignableFrom(abilityClass)) {
				try {
					Listener listener;
					try {
						listener = (Listener)abilityClass.getConstructor(Main.class).newInstance(Main.getPlugin());
					} catch (Exception e) {
						listener = (Listener)abilityClass.newInstance();
					}
					Main.getPlugin().getServer().getPluginManager().registerEvents(listener, Main.getPlugin());
				} catch (Exception e) {
					e.printStackTrace();
					System.out.print("Erro ao carregar a habilidade " + abilityClass.getSimpleName());
				}
			}
		}
		Main.getPlugin().getLogger().info(i + " kits carregados!");
	}
	
	public void send(Player player, Ability ability) {
		applyAbility(player, ability);
		
		if(ability.getName() == "PvP") {
			player.getInventory().setItem(0, new ItemBuilder().type(Material.STONE_SWORD).name("§bEspada de pedra").enchantment(Enchantment.DAMAGE_ALL).build());
		} else {
			player.getInventory().setItem(0, new ItemBuilder().type(Material.STONE_SWORD).name("§bEspada de pedra").build());
		}
		
		if(ability.getItems() == null)
			return;
		for(ItemStack stacks : ability.getItems()) {
			player.getInventory().addItem(stacks);
		}
		
		for(int i = 0; i < 34; i++) {
			player.getInventory().setItem(i, new ItemStack(Material.MUSHROOM_SOUP));
		}
	}
	
	public void applyAbility(Player player, Ability ability) {
		currentAbility.put(player.getUniqueId(), ability);
	}
	
	public String getAbilityName(Player player) {
		return getAbility(player) != null ? getAbility(player).getName() : "Nenhum";
	}
	
	public Ability getAbility(Player player) {
		return currentAbility.containsKey(player.getUniqueId()) ? currentAbility.get(player.getUniqueId()) : null;
	}

}
