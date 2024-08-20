package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class CobblestoneItemType extends ItemEconomy {
	
	public CobblestoneItemType() {
		super("Pedregulho", EconomyList.BLOCKS, Material.COBBLESTONE, BuyType.MONEY, 10, 30);
	}

}
