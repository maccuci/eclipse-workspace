package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class StoneItemType extends ItemEconomy {
	
	public StoneItemType() {
		super("Bloco de pedra", EconomyList.BLOCKS, Material.STONE, BuyType.MONEY, 15, 40);
	}

}
