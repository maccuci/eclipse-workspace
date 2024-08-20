package com.fuzion.core.bukkit.manager.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.fuzion.core.api.item.ItemBuilder;
import com.fuzion.core.api.menu.MenuInventory;

public class BalanceMenu {
	
	public static void open(Player player) {
		MenuInventory menu = new MenuInventory("�0Balanciamento do servidor", 3);
		
		menu.setItem(new ItemBuilder().type(Material.EXP_BOTTLE).name("�bBalanciamento de XP").lore("�7Informa��es logo abaixo:", " ", "�7Varia de acordo com a posi��o do jogador que morreu", "�7e tamb�m varia se as kills forem maior ou menor.", "�7Exemplo do sistema logo abaixo:", "�bSua Posi��o > Morreu Posi��o = xp * 100 / kills", "�bSuas Kills > Kills Morreu = xp * 10 / kills", "�bSua Posi��o < Morreu posi��o = xp * 2 / kills - 5").build(), 11);
		menu.setItem(new ItemBuilder().type(Material.GOLD_INGOT).name("�eBalanciamento de Coins").lore("�7Informa��es logo abaixo:", " ", "�7Ao matar: �a1 a 20 de Coins", "�7Ao morrer: �c5 a 15 de Coins.").build(), 12);
		menu.setItem(new ItemBuilder().type(Material.ANVIL).name("�bBalanciamento de Posi��o").lore("�7Informa��es logo abaixo:", " ", "�7O sistema divide em:", "�7Comparativo entre os �b�lXPs �7dos jogadores", "�7Caso o xp seja igual, o comparativo parte", "�7para quem entrou primeiro.").build(), 14);
		menu.setItem(new ItemBuilder().type(Material.DIAMOND_SWORD).name("�cBalanciamento de Dano").lore("�7Informa��es logo abaixo:", " ", "�7Espada de madeira: �c1 Cora��o.", "�7Espada de pedra: �c2.5 Cora��es.", "�7Espada de ferro: �c3.5 Cora��es.", "�7Espada de diamante: �c4.7 Cora��es.", "�7Sharpness adiciona mais 1.0 de �cCora��o.").build(), 15);
		
		menu.open(player);
	}

}
