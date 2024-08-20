package br.com.houldmc.rankup.manager.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.api.menu.ClickType;
import br.com.houldmc.rankup.api.menu.MenuClickHandler;
import br.com.houldmc.rankup.api.menu.MenuInventory;
import br.com.houldmc.rankup.api.menu.MenuItem;
import br.com.houldmc.rankup.manager.economy.EconomyManager;
import br.com.houldmc.rankup.manager.economy.constructor.ItemEconomy;
import br.com.houldmc.rankup.manager.economy.list.EconomyList;
import br.com.houldmc.rankup.player.account.RankUPPlayer;
import br.com.houldmc.rankup.player.account.RankUPPlayerManager;

public class MarketMenu {
	
	static int amount = 0, itemsPerPage = 21;
	
	public static void principal(Player player) {
		MenuInventory menu = new MenuInventory("�8Lojas", 6, false);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		for(int i = 0; i < 9; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		menu.setItem(13, itemBuilder.type(Material.MOB_SPAWNER).name("�eMob Spawners").lore("�7Clique para ir at� a se��o de Mob Spawner.").build());
		menu.setItem(30, itemBuilder.type(Material.DIAMOND_PICKAXE).name("�eFerramentas").hideAttributes().lore("�7Clique para ir at� a se��o de Ferramentas.").build());
		menu.setItem(32, new MenuItem(itemBuilder.type(Material.DIAMOND).name("�eMin�rios").lore("�7Clique para ir at� a se��o de Min�rios.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				ores(p, 1, menu);
			}
		}));
		menu.setItem(new MenuItem(itemBuilder.type(Material.GRASS).name("�eBlocos").lore("�7Clique para ir at� a se��o de Blocos.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				blocks(p, 1, menu);
			}
		}), 37);
		menu.setItem(43, new MenuItem(itemBuilder.type(Material.POTION).name("�ePo��es").lore("�7Clique para ir at� a se��o de Po��es.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				potions(p, 1, menu);
			}
		}));
		menu.setItem(49, itemBuilder.type(Material.EYE_OF_ENDER).name("�eArtefatos").lore("�7Clique para ir at� a se��o de Artefatos.").build());
		
		menu.open(player);
	}
	
	public static void ores(Player player, int page, MenuInventory lastInventory) {
		MenuInventory menu = new MenuInventory("�8Lojas - Min�rios", 6, false);
		ItemBuilder itemBuilder = new ItemBuilder();
		List<ItemEconomy> economies = new ArrayList<>(EconomyManager.getItems().values());
		List<MenuItem> items = new ArrayList<>();
		Collections.sort(economies, new Comparator<ItemEconomy>() {

			@Override
			public int compare(ItemEconomy o1, ItemEconomy o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.CARPET).durability(14).name("�cVoltar").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(lastInventory != null) {
					lastInventory.open(p);
				}
			}
		}), 0);
		menu.setItem(4, itemBuilder.type(Material.DIAMOND).name("�bMin�rios").lore("�7Seja bem-vindo a se��o de min�rios.", "�7O pre�o de cada min�rio pode variar de", "�7acordo com a economia do servidor!").build());
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		for(int i = 0; i < economies.size(); i++) {
			ItemEconomy itemEconomy = economies.get(i);
			
			if(itemEconomy.getList() == EconomyList.ORES) {
				items.add(new MenuItem(itemBuilder.type(itemEconomy.getType()).name("�e" + itemEconomy.getName()).lore(" ", "�7Pre�o de compra: �c" + itemEconomy.getPriceBuy(), "�7Pre�o de venda: �a" + itemEconomy.getPriceSell(), "�7Voc� possui: �b" + ItemBuilder.getAmount(player, itemEconomy.getType())).build(), new MenuClickHandler() {
					
					@Override
					public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
						buyItem(p, lastInventory, itemEconomy);
					}
				}));
			}
		}
		int pageStart = 0;
		int pageEnd = itemsPerPage;
		if (page > 1) {
			pageStart = ((page - 1) * itemsPerPage);
			pageEnd = (page * itemsPerPage);
		}
		if (pageEnd > items.size()) {
			pageEnd = items.size();
		}
		if (page == 1) {
			
		} else {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).name("�aP�gina Anterior").durability(10).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					ores(arg0, page - 1, lastInventory);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).name("�aP�gina Posterior").durability(10).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					ores(arg0, page + 1, lastInventory);
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.INK_SACK).name("�7Pr�xima p�gina").durability(8).build(), 35);
		}
		int w = 19;

		for (int i = pageStart; i < pageEnd; i++) {
			MenuItem item = items.get(i);
			menu.setItem(item, w);
			if (w % 9 == 7) {
				w += 3;
				continue;
			}
			w += 1;
		}
		
		menu.open(player);
	}
	
	public static void blocks(Player player, int page, MenuInventory lastInventory) {
		MenuInventory menu = new MenuInventory("�8Lojas - Blocos", 6, false);
		ItemBuilder itemBuilder = new ItemBuilder();
		List<ItemEconomy> economies = new ArrayList<>(EconomyManager.getItems().values());
		List<MenuItem> items = new ArrayList<>();
		Collections.sort(economies, new Comparator<ItemEconomy>() {

			@Override
			public int compare(ItemEconomy o1, ItemEconomy o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.CARPET).durability(14).name("�cVoltar").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(lastInventory != null) {
					lastInventory.open(p);
				}
			}
		}), 0);
		menu.setItem(4, itemBuilder.type(Material.GRASS).name("�6Blocos").lore("�7Seja bem-vindo a se��o de blocos.", "�7O pre�o de cada bloco pode variar de", "�7acordo com a economia do servidor!").build());
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		for(int i = 0; i < economies.size(); i++) {
			ItemEconomy itemEconomy = economies.get(i);
			
			if(itemEconomy.getList() == EconomyList.BLOCKS) {
				items.add(new MenuItem(itemBuilder.type(itemEconomy.getType()).name("�e" + itemEconomy.getName()).lore(" ", "�7Pre�o de compra: �c" + itemEconomy.getPriceBuy(), "�7Pre�o de venda: �a" + itemEconomy.getPriceSell(), "�7Voc� possui: �b" + ItemBuilder.getAmount(player, itemEconomy.getType())).build(), new MenuClickHandler() {
					
					@Override
					public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
						buyItem(p, menu, itemEconomy);
					}
				}));
			}
		}
		int pageStart = 0;
		int pageEnd = itemsPerPage;
		if (page > 1) {
			pageStart = ((page - 1) * itemsPerPage);
			pageEnd = (page * itemsPerPage);
		}
		if (pageEnd > items.size()) {
			pageEnd = items.size();
		}
		if (page == 1) {
			
		} else {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.CARPET).name("�cP�gina Anterior").durability(14).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					blocks(arg0, page - 1, lastInventory);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.CARPET).name("�aP�gina Posterior").durability(5).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					blocks(arg0, page + 1, lastInventory);
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.INK_SACK).name("�7Pr�xima p�gina").durability(8).build(), 35);
		}
		int w = 19;

		for (int i = pageStart; i < pageEnd; i++) {
			MenuItem item = items.get(i);
			menu.setItem(item, w);
			if (w % 9 == 7) {
				w += 3;
				continue;
			}
			w += 1;
		}
		
		menu.open(player);
	}
	
	public static void potions(Player player, int page, MenuInventory lastInventory) {
		MenuInventory menu = new MenuInventory("�8Lojas - Po��es", 6, false);
		ItemBuilder itemBuilder = new ItemBuilder();
		List<ItemEconomy> economies = new ArrayList<>(EconomyManager.getItems().values());
		List<MenuItem> items = new ArrayList<>();
		Collections.sort(economies, new Comparator<ItemEconomy>() {

			@Override
			public int compare(ItemEconomy o1, ItemEconomy o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.CARPET).durability(14).name("�cVoltar").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(lastInventory != null) {
					lastInventory.open(p);
				}
			}
		}), 0);
		menu.setItem(4, itemBuilder.type(Material.POTION).name("�cPo��es").lore("�7Seja bem-vindo a se��o de po��es.", "�7O pre�o de cada po��o pode variar de", "�7acordo com a economia do servidor!").build());
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		for(int i = 0; i < economies.size(); i++) {
			ItemEconomy itemEconomy = economies.get(i);
			
			if(itemEconomy.getList() == EconomyList.POTIONS) {
				items.add(new MenuItem(itemBuilder.type(itemEconomy.getType()).name("�e" + itemEconomy.getName()).lore(" ", "�7Pre�o de venda: �a" + itemEconomy.getPriceSell(), "�7Dura��o: �b1 Minuto").build()));
			}
		}
		int pageStart = 0;
		int pageEnd = itemsPerPage;
		if (page > 1) {
			pageStart = ((page - 1) * itemsPerPage);
			pageEnd = (page * itemsPerPage);
		}
		if (pageEnd > items.size()) {
			pageEnd = items.size();
		}
		if (page == 1) {
			
		} else {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.CARPET).name("�cP�gina Anterior").durability(14).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					potions(arg0, page - 1, lastInventory);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.CARPET).name("�aP�gina Posterior").durability(5).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					potions(arg0, page + 1, lastInventory);
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.INK_SACK).name("�7Pr�xima p�gina").durability(8).build(), 35);
		}
		int w = 19;

		for (int i = pageStart; i < pageEnd; i++) {
			MenuItem item = items.get(i);
			menu.setItem(item, w);
			if (w % 9 == 7) {
				w += 3;
				continue;
			}
			w += 1;
		}
		
		menu.open(player);
	}
	
	public static void buyItem(Player player, MenuInventory lastInventory, ItemEconomy itemEconomy) {
		MenuInventory menu = new MenuInventory("�8Lojas - Comprar/Vender", 6, false);
		ItemBuilder itemBuilder = new ItemBuilder();
		RankUPPlayer rankUPPlayer = new RankUPPlayerManager().getAccount(player);
		
		menu.setItem(new MenuItem(itemBuilder.type(Material.CARPET).durability(14).name("�cVoltar").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(lastInventory != null) {
					lastInventory.open(p);
				}
			}
		}), 0);
		menu.setItem(4, itemBuilder.type(Material.EMERALD).name("�aLoja").build());
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		menu.setItem(21, new MenuItem(itemBuilder.type(Material.PAPER).name("�c�lDIMINUIR").lore("�7Quantidade: �b" + amount).build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(amount == 0)
					return;
				amount -= 1;
				buyItem(p, lastInventory, itemEconomy);
			}
		}));
		menu.setItem(22, itemBuilder.type(itemEconomy.getType()).build());
		menu.setItem(23, new MenuItem(itemBuilder.type(Material.PAPER).name("�a�lAUMENTAR").lore("�7Quantidade: �b" + amount).build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(amount == 64) {
					p.sendMessage("�6�lLoja �7� �cVoc� n�o pode comprar mais do que x64!");
					return;
				}
				amount += 1;
				buyItem(p, lastInventory, itemEconomy);
			}
		}));
		double sell = amount * itemEconomy.getPriceSell();
		menu.setItem(39, new MenuItem(itemBuilder.type(Material.WOOL).name("�aComprar").lore("�7Clique para comprar " + amount + " " + itemEconomy.getType().name(), "�7com o valor de �b" + ItemBuilder.format(sell)).durability(5).build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				if(amount == 0) {
					p.sendMessage("�6�lLoja �7� �cA quantidade de item a ser comprada tem que ser maior do que 0.");
					return;
				}
				if(rankUPPlayer.getMoney() < sell) {
					p.sendMessage("�6�lLoja �7� �cVoc� presica de mais " + ItemBuilder.format(sell) + " moedas, para comprar este item.");
					return;
				}
				rankUPPlayer.setMoney(rankUPPlayer.getMoney() - sell);
				rankUPPlayer.update();
				p.getInventory().addItem(new ItemBuilder().type(itemEconomy.getType()).amount(amount).build());
				p.sendMessage("�6�lLoja �7� �aVoc� comprou " + itemEconomy.getType().name() + " x" + amount + ".");
			}
		}));
		double value = itemEconomy.getPriceSell() * ItemBuilder.getAmount(player, itemEconomy.getType());
		menu.setItem(41, new MenuItem(itemBuilder.type(Material.WOOL).name("�cVender").lore("�7Venda por", "�7" + ItemBuilder.format(value) + 
				" moedas - x" + ItemBuilder.getAmount(player, itemEconomy.getType())).durability(14).build(), new MenuClickHandler() {
					
					@Override
					public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
						int amounts = ItemBuilder.getAmount(player, itemEconomy.getType());
						if(amounts == 0) {
							p.sendMessage("�6�lLoja �7� �cVoc� n�o tem este tipo de item para vender.");
							return;
						}
						
						double value = itemEconomy.getPriceSell() * ItemBuilder.getAmount(player, itemEconomy.getType());
						p.sendMessage("�6�lLoja �7� �aVoc� vendeu " + itemEconomy.getType().name() + " x" + amounts + " e ganhou " + value + " moedas.");
						p.getInventory().remove(itemBuilder.type(itemEconomy.getType()).amount(ItemBuilder.getAmount(p, itemEconomy.getType())).build());
						rankUPPlayer.addMoney(value);
						rankUPPlayer.update();
					}
				}));
		
		menu.open(player);
	}
}
