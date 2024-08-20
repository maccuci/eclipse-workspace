package me.ale.christmas.util;

import java.time.LocalDate;

public class DateUtils {

	public static void main(String[] args) {
		System.out.println(calculeChristmas());
	}
	
	public static boolean isChristmas() {
		LocalDate date = LocalDate.now();
		
		return date.getMonthValue() == 12 || date.getDayOfMonth() == 25;
	}
	
	public static int calculeToChristmas() {
		LocalDate date = LocalDate.now();
		int dayOfChristmas = 25;

		if (date.getMonthValue() == 12) {
			return dayOfChristmas - date.getDayOfMonth();
		}

		return 0;
	}

	public static int calculeChristmas() {
		LocalDate date = LocalDate.now();
		int daysOff = 25;

		if(date.getMonthValue() == 12) {
			daysOff = daysOff - date.getDayOfMonth();
		}

		return daysOff;
	}

}
