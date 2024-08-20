package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class ObsiadianItemType extends ItemEconomy {
	
	public ObsiadianItemType() {
		super("Obisidiana", EconomyList.BLOCKS, Material.OBSIDIAN, BuyType.MONEY, 2500, 5500);
	}

}
