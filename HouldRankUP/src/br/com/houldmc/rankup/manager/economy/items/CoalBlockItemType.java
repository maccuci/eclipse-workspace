package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class CoalBlockItemType extends ItemEconomy {
	
	public CoalBlockItemType() {
		super("Bloco de Carvão", EconomyList.ORES, Material.COAL_BLOCK, BuyType.MONEY, 200, 350);
	}

}
