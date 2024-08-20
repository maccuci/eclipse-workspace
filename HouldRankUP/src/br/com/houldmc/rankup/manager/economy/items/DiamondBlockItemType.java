package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class DiamondBlockItemType extends ItemEconomy {
	
	public DiamondBlockItemType() {
		super("Bloco de Diamante", EconomyList.ORES, Material.DIAMOND_BLOCK, BuyType.MONEY, 500, 1500);
	}

}
