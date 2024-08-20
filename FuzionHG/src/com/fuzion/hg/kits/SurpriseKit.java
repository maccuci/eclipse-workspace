package com.fuzion.hg.kits;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.master.account.Group;
import com.fuzion.hg.manager.kit.Kit;
import com.fuzion.hg.manager.kit.KitManager;

public class SurpriseKit extends Kit {
	
	public static ArrayList<String> supriseList = new ArrayList<>();
	
	public SurpriseKit() {
		super("Surprise", "Todos", new ItemStack(Material.NAME_TAG), Group.BETA, "§7Um kit aleatório será sorteado para você.");
		load();
	}
	
	public void load() {
		List<Kit> kits = new ArrayList<>(KitManager.getKits().values());
		
		for(int i = 0; i < kits.size(); i++) {
			Kit kit = kits.get(i);
			
			supriseList.add(kit.getName());
		}
	}
	
	 public static String getViableKit() {
		 if (supriseList.size() > 0) {
			 return supriseList.get(new Random().nextInt(supriseList.size()));
		 }
		 return null;
	 }
}
