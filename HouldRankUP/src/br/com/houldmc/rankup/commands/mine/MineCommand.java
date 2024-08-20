package br.com.houldmc.rankup.commands.mine;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.com.houldmc.rankup.api.item.ItemBuilder;
import br.com.houldmc.rankup.manager.command.Command;
import br.com.houldmc.rankup.manager.command.CommandArgs;
import br.com.houldmc.rankup.manager.command.CommandLoader.CommandClass;
import br.com.houldmc.rankup.manager.mine.MineManager;
import br.com.houldmc.rankup.manager.mine.list.SelectPos;

public class MineCommand implements CommandClass {
	
	@Command(name = "wand", aliases = "setpos")
	public void wand(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		
		if(args.length == 0) {
			if(MineManager.selectPos.containsKey(player.getUniqueId())) {
				MineManager.selectPos.remove(player.getUniqueId());
				player.sendMessage("§6§lWand §7» §aVocê saiu do modo de construção de minas.");
			} else {
				MineManager.selectPos.put(player.getUniqueId(), new SelectPos());
				player.getInventory().addItem(new ItemBuilder().type(Material.BLAZE_ROD).name("§aSelecionar posições").build());
				player.sendMessage("§6§lWand §7» §aVocê entrou no modo de construção de minas.");
			}
		}
	}
	
	@Command(name = "createmine", aliases = "criarmina")
	public void createmine(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		MineManager mineManager = new MineManager();
		
		if(args.length == 0) {
			player.sendMessage("§c/" + commandArgs.getLabel() + " <name>");
			return;
		}
		
		if(args.length == 1) {
			String name = args[0];
			SelectPos selectPos = MineManager.selectPos.get(player.getUniqueId());
			
			if(selectPos == null || selectPos.getLocation1() == null || selectPos.getLocation2() == null){
				player.sendMessage("§6§lMina §7» §cVocê não selecionou as posições.");
				return;
			}
			
			if(mineManager.createMine(name, selectPos)) {
				player.sendMessage("§6§lMina §7» §aVocê criou a mina " + name + " com sucesso.");
			} else {
				player.sendMessage("§6§lMina §7» §eEsta mina já existe.");
				return;
			}
		}
	}
	
	@Command(name = "deletemine", aliases = "deletarmina")
	public void deletemine(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		MineManager mineManager = new MineManager();
		
		if(args.length == 0) {
			player.sendMessage("§c/" + commandArgs.getLabel() + " <name>");
			return;
		}
		
		if(args.length == 1) {
			String name = args[0];
			
			if(mineManager.deleteMine(name)) {
				player.sendMessage("§6§lMina §7» §aVocê deletou a mina " + name + " com sucesso.");
			} else {
				player.sendMessage("§6§lMina §7» §eEsta mina não existe.");
				return;
			}
		}
	}
	
	@Command(name = "addblock")
	public void addblock(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		MineManager mineManager = new MineManager();
		
		if(args.length < 1) {
			player.sendMessage("§c/" + commandArgs.getLabel() + " <mine> <percentage>");
			player.sendMessage("§c/" + commandArgs.getLabel() + " <mine> <percentage> [block]");
			return;
		}
		
		if(args.length == 2) {
			String name = args[0];
			Integer porcentage = Integer.parseInt(args[1]);
			ItemStack itemStack = player.getItemInHand();
			

			if(itemStack == null || itemStack.getType() == Material.AIR || !itemStack.getType().isBlock()) {
				player.sendMessage("§6§lMina §7» §cVocê não está segurando nenhum bloco.");
				return;
			}
			
			int result = mineManager.addBlock(name, itemStack, porcentage);
			switch (result) {
			case 0:
				player.sendMessage("§6§lMina §7» §eA mina " + name + " não foi encontrada.");
				return;
			case 1:
				player.sendMessage("§6§lMina §7» §eEste bloco já existe.");
				return;
			case 2:
				player.sendMessage("§6§lMina §7» §aO bloco " + itemStack.getType().name() + " foi adicionado com a porcentagem de " + porcentage + "%");
				return;
			}
		}
		
		if(args.length == 3) {
			String name = args[0];
			Integer porcentage = Integer.parseInt(args[1]);
			Material material = Material.getMaterial(args[2]);
			

			if(material == null || material == Material.AIR || !material.isBlock()) {
				player.sendMessage("§6§lMina §7» §cVocê não está segurando nenhum bloco.");
				return;
			}
			
			int result = mineManager.addBlock(name, material, porcentage);
			switch (result) {
			case 0:
				player.sendMessage("§6§lMina §7» §eA mina " + name + " não foi encontrada.");
				return;
			case 1:
				player.sendMessage("§6§lMina §7» §eEste bloco já existe.");
				return;
			case 2:
				player.sendMessage("§6§lMina §7» §fO bloco " + material.name() + " foi adicionado com a porcentagem de §b" + porcentage + "%");
				return;
			}
		}
	}
	
	@Command(name = "resetmine")
	public void setinterval(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		MineManager mineManager = new MineManager();
		
		if(args.length == 0) {
			player.sendMessage("§c/" + commandArgs.getLabel() + " <mine>");
			return;
		}
		
		if(args.length == 1) {
			String name = args[0];
			
			if(mineManager.resetArenaByName(name)) {
				player.sendMessage("§6§lMina §7» §aVocê resetou a mina " + name + ".");
				return;
			}
			player.sendMessage("§6§lMina §7» §eEsta mina não existe.");
		}
	}
	
	@Command(name = "setresetinterval")
	public void resetmine(CommandArgs commandArgs) {
		Player player = commandArgs.getPlayer();
		String[] args = commandArgs.getArgs();
		MineManager mineManager = new MineManager();
		
		if(args.length < 1) {
			player.sendMessage("§c/" + commandArgs.getLabel() + " <name> <interval>");
			return;
		}
		
		if(args.length == 2) {
			String name = args[0];
			Integer interval = NumberUtils.toInt(args[1]);
			
			if(mineManager.setIntervalReset(name, interval)) {
				player.sendMessage("§6§lMina §7» §aO tempo de resetar da mina " + args[0] + " foi alterado para " + ItemBuilder.getMessage(interval * 60));
				return;
			}
			player.sendMessage("§6§lMina §7» §eEsta mina não existe.");
		}
	}
}
