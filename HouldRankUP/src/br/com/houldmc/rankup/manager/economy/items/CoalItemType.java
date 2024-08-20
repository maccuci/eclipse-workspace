package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class CoalItemType extends ItemEconomy {

	public CoalItemType() {
		super("Carvão", EconomyList.ORES, Material.COAL, BuyType.MONEY, 50, 150);
	}
}
