package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class HasteItemType extends ItemEconomy {
	
	public HasteItemType() {
		super("Haste", EconomyList.POTIONS, Material.POTION, BuyType.CASH, 0, 10);
	}

}
