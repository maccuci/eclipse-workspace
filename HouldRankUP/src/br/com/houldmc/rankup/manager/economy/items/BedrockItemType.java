package br.com.houldmc.rankup.manager.economy.items;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;

public class BedrockItemType extends ItemEconomy {
	
	public BedrockItemType() {
		super("Pedra inquebravél", EconomyList.BLOCKS, Material.BEDROCK, BuyType.MONEY, 4000, 7000);
	}

}
