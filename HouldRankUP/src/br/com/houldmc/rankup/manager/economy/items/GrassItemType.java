package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class GrassItemType extends ItemEconomy {
	
	public GrassItemType() {
		super("Bloco de grama", EconomyList.BLOCKS, Material.GRASS, BuyType.MONEY, 5, 20);
	}

}
