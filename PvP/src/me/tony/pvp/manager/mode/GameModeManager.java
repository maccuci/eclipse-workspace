package me.tony.pvp.manager.mode;

import java.util.Scanner;

import me.tony.pvp.manager.Manager;

public class GameModeManager {

	@SuppressWarnings("resource")
	public void scannerMode() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Qual estilo de jogo será? \n[1] - FullIron | [2] - Simulator");

		String input = scanner.nextLine();

		if (input.contains("1")) {
			Manager.FULL_IRON_MODE = true;
			System.out.println("Estilo de jogo alterado para FullIron.");
		} else if (input.contains("2")) {
			Manager.FULL_IRON_MODE = false;
			System.out.println("Estilo de jogo alterado para Simulator.");
		} else {
			Manager.FULL_IRON_MODE = true;
			System.out.println("Estilo de jogo não encontrado. Alterando para FullIron...");
		}
	}

}
