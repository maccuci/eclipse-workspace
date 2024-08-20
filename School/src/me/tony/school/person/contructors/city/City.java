package me.tony.school.person.contructors.city;

import java.util.ArrayList;
import java.util.List;

import me.tony.school.person.contructors.Person;
import me.tony.school.person.contructors.state.State;

public class City {
	
	private String name, abbreviation;
	private State state;
	private List<Person> habitants = new ArrayList<>();
	
	public City(String name, String abbreviation, State state) {
		this.name = name;
		this.abbreviation = abbreviation;
		this.state = state;
	}
	
	public void addHabitant(Person person) {
		if(habitants.contains(person))
			return;
		habitants.add(person);
	}
	
	public String getName() {
		return name;
	}
	
	public String getAbbreviation() {
		return abbreviation;
	}
	
	public State getState() {
		return state;
	}
	
	public List<Person> getHabitants() {
		return habitants;
	}

}
