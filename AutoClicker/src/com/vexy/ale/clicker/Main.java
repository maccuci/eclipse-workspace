package com.vexy.ale.clicker;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.util.Scanner;

public class Main {
	
	public static Robot robot;
	
	public static void function() {
		try {
			robot = new Robot();
		} catch (AWTException e1) {
			e1.printStackTrace();
		}
		Scanner scanner = new Scanner(System.in);
		System.out.println("Fenks AutoClicker | v1.0-SNAPSHOT");
		
		System.out.println("Coloque a quantidade de cliques:");
		int clicks = scanner.nextInt();
		System.out.println("Coloque a quantidade de milisegundos:");
		int delay = scanner.nextInt();
		
		System.out.println("AutoClicker dando inicio em 3 segundos...");
		
		try {
			Thread.sleep(3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i <= clicks; i++) {
			robot.mousePress(MouseEvent.BUTTON1_MASK);
			robot.mouseRelease(MouseEvent.BUTTON1_MASK);
		}
		robot.delay(delay);
		
		
		System.out.println("AutoClicker terminado.");
		scanner.close();
	}
	
	public static void main(String[] args) {
		function();
	}
}
