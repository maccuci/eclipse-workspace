package me.tony.school.person;

import me.tony.school.person.contructors.Person;
import me.tony.school.person.contructors.city.City;
import me.tony.school.person.contructors.state.State;

public class Main {
	
	public static void main(String[] args) {
		Person person = new Person("Josh");
		person.setState(new State("Minas Gerais", "MG"));
		person.setCity(new City("Juiz de Fora", "JF", person.getState()));
		
		person.registerHabitant();
		
		System.out.println(person.showPersonInformations());
	}

}
