package com.fuzion.hg.manager.kit;

import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import lombok.Getter;

@Getter
public class SimpleKit {
	
	public static HashMap<String, SimpleKit> kits = new HashMap<String, SimpleKit>();
	private ItemStack[] armor;
	private ItemStack[] inventory;
	private Collection<PotionEffect> effects;

	public SimpleKit(Player p) {
		ItemStack[] copyInventory = new ItemStack[p.getInventory().getContents().length];
		for (int i = 0; i < p.getInventory().getContents().length; i++) {
			ItemStack item = p.getInventory().getContents()[i];
			if (item != null) {
				copyInventory[i] = item;
			}
		}
		this.inventory = copyInventory;
		ItemStack[] copyArmor = new ItemStack[p.getInventory().getArmorContents().length];
		for (int i = 0; i < p.getInventory().getArmorContents().length; i++) {
			ItemStack item = p.getInventory().getArmorContents()[i];
			if (item != null) {
				copyArmor[i] = item;
			}
		}
		this.armor = copyArmor;
		this.effects = p.getActivePotionEffects();
	}

	public static void addKit(Player sender, String s, SimpleKit sk) {
		if (kits.containsKey(s)) {
			sender.sendMessage("§7O kit '" + s + "' já existe");
			return;
		}
		kits.put(s, sk);
		sender.sendMessage("§7O kit '" + s + "' foi criado com sucesso");
	}

	public static void removeKit(Player sender, String s) {
		if (!kits.containsKey(s)) {
			sender.sendMessage("§7O kit '" + s + "' não existe");
			return;
		}
		kits.remove(s);
		sender.sendMessage("§7O kit '" + s + "' foi removido com sucesso");
	}

	@SuppressWarnings("deprecation")
	public static void applyKit(Player sender, String s, Player target) {
		if (!kits.containsKey(s)) {
			sender.sendMessage("§7O kit '" + s + "' não existe");
			return;
		}
		SimpleKit sk = (SimpleKit) kits.get(s);
		if (target == null) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p != sender) {
					p.sendMessage("§7O kit '" + s + "' foi aplicado com sucesso");
				}
				p.getInventory().clear();
				p.getInventory().setArmorContents(null);
				for (PotionEffect effect : p.getActivePotionEffects()) {
					p.removePotionEffect(effect.getType());
				}
				p.getInventory().setHelmet(sk.getArmor()[0]);
				p.getInventory().setChestplate(sk.getArmor()[1]);
				p.getInventory().setLeggings(sk.getArmor()[2]);
				p.getInventory().setBoots(sk.getArmor()[3]);
				p.getInventory().setArmorContents(sk.getArmor());
				p.getInventory().setContents(sk.getInventory());
				for (PotionEffect effect : sk.getEffects()) {
					p.addPotionEffect(effect);
				}
			}
			sender.sendMessage("§7O kit '" + s + "' foi aplicado com sucesso em todos os players");
			return;
		}
		target.getInventory().clear();
		target.getInventory().setArmorContents(null);
		for (PotionEffect effect : target.getActivePotionEffects()) {
			target.removePotionEffect(effect.getType());
		}
		target.getInventory().setArmorContents(sk.getArmor());
		target.getInventory().setContents(sk.getInventory());
		for (PotionEffect effect : sk.getEffects()) {
			target.addPotionEffect(effect);
		}
		sender.sendMessage("§7O kit '" + s + "' foi aplicado com sucesso em " + target.getName());
	}

}
