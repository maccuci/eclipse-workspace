package br.com.houldmc.rankup.manager.menu;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.api.menu.ClickType;
import br.com.houldmc.rankup.api.menu.MenuClickHandler;
import br.com.houldmc.rankup.api.menu.MenuInventory;
import br.com.houldmc.rankup.api.menu.MenuItem;

public class BankMenu {
	
	static int itemsPerPage = 21;
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("§8Banco", 6);
		ItemBuilder itemBuilder = new ItemBuilder();
		
		menu.setItem(4, itemBuilder.type(Material.PRISMARINE_CRYSTALS).name("§eBanco").lore("§7Seja bem-vindo ao banco do servidor.", "§7Aqui você pode: §bTrocar §7dinheiro por cash.", "§bDepositar §7dinheiro para algum jogador.", "§7E você pode ver também como está a §aeconomia§7.").build());
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		menu.setItem(29, itemBuilder.type(Material.GOLD_INGOT).name("§eTrocar dinheiro por cash").lore("§7Clique para escolher a quantia de", "§7dinheiro que você irá trocar.", " ", "§7Cada cash é igual a §e1.000§7 de dinheiro.").build());
		menu.setItem(31, new MenuItem(itemBuilder.type(Material.PAPER).name("§bDepositar").lore("§7Clique para escolhar algum jogador", "§7para você depositar cash ou dinheiro.").build(), new MenuClickHandler() {
			
			@Override
			public void onClick(Player p, Inventory inv, ClickType type, ItemStack stack, int slot) {
				deposit(p, 1);
			}
		}));
		menu.setItem(33, itemBuilder.type(Material.BOOK).name("§6Economia").lore("§7Valorização: §aBoa", "§7Compra de produtos: §d50% §7(Razoavel)", "§7Venda de produtos: §c65%").glow().build());
		
		menu.open(player);
	}
	
	public static void deposit(Player player, int page) {
		MenuInventory menu = new MenuInventory("§8Banco - Depositar", 6);
		ItemBuilder itemBuilder = new ItemBuilder();
		List<MenuItem> items = new ArrayList<>();
		
		for(int i = 9; i < 18; i++) {
			menu.setItem(i, itemBuilder.type(Material.STAINED_GLASS_PANE).durability(4).name(" ").build());
		}
		
		for(Player online : Bukkit.getOnlinePlayers()) {
			if(online == null)
				return;
			if(!online.isOnline())
				return;
			
			items.add(new MenuItem(itemBuilder.type(Material.SKULL_ITEM).name("§eDeposite para §7" + player.getName()).lore("§7Clique e escolha a quantia.").durability(3).skin(player.getName()).build()));
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
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).name("§aPágina Anterior").durability(10).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					deposit(arg0, page - 1);
				}
			}), 3);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).name("§aPágina Posterior").durability(10).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					deposit(arg0, page + 1);
				}
			}), 5);
		} else {
			menu.setItem(new ItemBuilder().type(Material.INK_SACK).name("§7Próxima página").durability(8).build(), 35);
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
}
