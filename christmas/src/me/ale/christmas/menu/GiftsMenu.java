package me.ale.christmas.menu;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import me.ale.christmas.util.DateUtils;
import me.ale.christmas.util.ItemBuilder;
import me.ale.christmas.util.menu.MenuInventory;

public class GiftsMenu {
	
    private ItemBuilder builder = new ItemBuilder();
	
	public void open(Player player) {
		MenuInventory menu = new MenuInventory("�8Presentes", 3);
		
		menu.setItem(8, builder.type(Material.PAPER).name("�aInforma��es").lore(DateUtils.isChristmas() == true ? "O natal � hoje! Boas festas." : "�7Faltam �f" + DateUtils.calculeToChristmas() + " �7dias para o natal!").build());
		menu.setItem(13, builder.type(Material.SKULL_ITEM).name("�aSeus Presentes").lore("�7Voc� possui nenhum presente! Comece as miss�es que o", "�7ajudante do papai noel lhe deu para come�ar.").durability(3).skinURL("http://textures.minecraft.net/texture/71e82a346f0c5cd2bd2a18bd2dbb5eb43e1fd2581d72b198e1b79d17708e0e91").build());
		menu.setItem(15, builder.type(Material.BOOK).name("�aMiss�es").lore("�7Clique para resgatar recompensas de suas miss�es", "�7ou clique para come�ar uma nova miss�o", " ", "�7Voc� fez �f0 �7de �f10 �7miss�es.").build());
		
		menu.open(player);
	}
}
