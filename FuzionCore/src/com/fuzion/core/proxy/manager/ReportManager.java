package com.fuzion.core.proxy.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.fuzion.core.api.admin.AdminAPI;
import com.fuzion.core.api.admin.AdminAPI.AdminMode;
import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.ClickType;
import com.fuzion.core.api.menu.MenuClickHandler;
import com.fuzion.core.api.menu.MenuInventory;
import com.fuzion.core.api.menu.MenuItem;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ReportManager {
	
	public static final List<Report> reports = new ArrayList<>();
	private static int itemsPerPage = 21;
	
	public static void addReport(ProxiedPlayer reporter, ProxiedPlayer reported, String reason) {
		Report report = new Report(reporter, reported, reason);
		for(ProxiedPlayer staff : ProxyServer.getInstance().getPlayers()) {
			if(staff.hasPermission("reportview"))
			report.send(staff);
		}
		reports.add(report);
	}
	
	public static void menu(Player player, int page) {
		MenuInventory menu = new MenuInventory("§0Reports - Página " + "[§a" + page + "§0/" + Math.ceil(reports.size() / itemsPerPage) + "]", 6, true);
		List<Report> reportsList = new ArrayList<>(reports);
		
		Collections.sort(reportsList, new Comparator<Report>() {
			@Override
			public int compare(Report o1, Report o2) {
				return String.CASE_INSENSITIVE_ORDER.compare(o1.getReported().getName(), o2.getReported().getName());
			}
		});
		List<MenuItem> items = new ArrayList<>();
		for(int i = 0; i < reportsList.size(); i++) {
			Report report = reportsList.get(i);
			
			items.add(new MenuItem(new ItemBuilder().type(Material.SKULL_ITEM).durability(3).skin(report.getReported().getName()).name("§b" + report.getReported().getName()).lore("§fReportado por: §e" + report.getReporter().getName(), "§fData: §b" + report.getDate(), "§fMotivo: §b" + report.getReason(), " ", "§3§lDIREITO §fpara recusar.", "§3§lESQUERDO §fpara aceitar.").build(), new MenuClickHandler() {
				
				@Override
				public void onClick(Player paramPlayer, Inventory paramInventory, ClickType paramClickType, ItemStack paramItemStack, int paramInt) {
					if(paramClickType == ClickType.LEFT) {
						paramPlayer.sendMessage("§aVocê aceitou o report de " + report.getReporter().getName() + " e foi até o jogador " + report.getReported().getName());
						if(report.getReported() == null) {
							paramPlayer.sendMessage("§cEste jogador está offiline do momento. Caso o jogador que reportou tenha provas deste jogador com hack, você pode bani-lo.");
							return;
						}
						if(!AdminAPI.admin.contains(paramPlayer.getUniqueId())) {
							new AdminAPI().updateMode(paramPlayer, AdminMode.ADMIN);
							paramPlayer.closeInventory();
							paramPlayer.sendMessage("§bSeu modo de jogo foi alterado automaticamente para ADMIN!");
						}
						paramPlayer.closeInventory();
						paramPlayer.teleport((Location) report.getReported());
						paramPlayer.sendMessage("§eVocê foi até o jogador " + report.getReported().getName());
					} else if(paramClickType == ClickType.RIGHT) {
						paramPlayer.sendMessage("§cVocê negou o report do jogador " + report.getReporter().getName());
						if(report.getReporter() == null) {
							return;
						}
						report.getReporter().sendMessage(new TextComponent("§cSeu report foi negado!"));
						reports.remove(report);
						menu(paramPlayer, page);
					}
				}
			}));
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
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Anterior").lore(Arrays.asList("§7Clique para ir para a página", "§7anterior")).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					menu(arg0, page - 1);
				}
			}), 27);
		}
		if (Math.ceil(items.size() / itemsPerPage) + 1 > page) {
			menu.setItem(new MenuItem(new ItemBuilder().type(Material.INK_SACK).durability(10).name("§aPágina Posterior").lore(Arrays.asList("§7Clique para ir para a página", "§7posterior")).build(), new MenuClickHandler() {
				@Override
				public void onClick(Player arg0, Inventory arg1, ClickType arg2, ItemStack arg3, int arg4) {
					menu(arg0, page + 1);
				}
			}), 35);
		} else {
			menu.setItem(new ItemBuilder().type(Material.INK_SACK).durability(8).name("§cPágina Posterior").build(), 35);
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
