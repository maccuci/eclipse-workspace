package br.com.houldmc.rankup.manager.economy.constructor;

import org.bukkit.Material;

import br.com.houldmc.rankup.manager.economy.list.BuyType;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;
import lombok.Getter;

@Getter
public abstract class ItemEconomy {
	
	private String name;
	private int priceSell, priceBuy;
	private Material type;
	private EconomyList list;
	private BuyType buyType;
	
	public ItemEconomy(String name, EconomyList list, Material type, BuyType buyType, int priceSell, int priceBuy) {
		this.name = name;
		this.list = list;
		this.type = type;
		this.buyType = buyType;
		this.priceSell = priceSell;
		this.priceBuy = priceBuy;
	}
}
