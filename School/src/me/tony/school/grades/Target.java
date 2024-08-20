/**
 * Copyright (2019) Txny, this software contains all rights reserved
 * unauthorized copying of this file, via any medium is 
 * strictly prohibited proprietary and confidential.
*/
package me.tony.school.grades;

import javax.swing.*;

public class Target {

	private static String name = "", matter = "";
	private static double gradeO = 0, gradeT = 0;
	private static double average;
	private static boolean aproved = false;

	public static void main(String[] args) {

		name = JOptionPane.showInputDialog("Digite o nome do aluno.");
		matter = JOptionPane.showInputDialog("Coloque a matéria");
		try {
			gradeO = Double.parseDouble(JOptionPane.showInputDialog("Digite a primeira nota"));
			gradeT = Double.parseDouble(JOptionPane.showInputDialog("Digite a segunda nota"));
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "A nota tem que ser um número.", "Erro", 1);
			return;
		}
		average = (gradeO + gradeT) / 2;

		if (gradeO > 10 || gradeT > 10) {
			JOptionPane.showMessageDialog(null, "A nota não pode ser maior do que 10.", "Erro", 1);
			return;
		}
		
		aprove();
		JOptionPane.showMessageDialog(null, "Aluno: " + name + "\nMatéria: " + matter + "\n1° Nota: " + gradeO
				+ "\n2° Nota: " + gradeT + "\n\nMédia: " + average + "\nAprovado: " + hasAproved(), "Notas finais", 1);

	}
	
	public static String hasAproved() {
		if(aproved) {
			return "Sim";
		} else {
			return "Não";	
		}
	}
	
	private static boolean aprove() {
		if (average >= 7) {
			aproved = true;
		} else if (average < 3) {
			aproved = false;
		} else if (average >= 3 && average <= 6.9) {
			aproved = false;
		}
		return aproved;
	}
}
