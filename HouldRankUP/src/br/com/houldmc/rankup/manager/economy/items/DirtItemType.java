package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class DirtItemType extends ItemEconomy {
	
	public DirtItemType() {
		super("Terra", EconomyList.BLOCKS, Material.DIRT, BuyType.MONEY, 5, 15);
	}

}
