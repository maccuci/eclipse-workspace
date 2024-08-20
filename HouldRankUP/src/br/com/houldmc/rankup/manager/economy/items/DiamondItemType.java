package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class DiamondItemType extends ItemEconomy {

	public DiamondItemType() {
		super("Diamante", EconomyList.ORES, Material.DIAMOND, BuyType.MONEY, 250, 750);
	}
}
