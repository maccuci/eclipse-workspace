package me.tony.school.person.contructors.state;

import java.util.ArrayList;
import java.util.List;

import me.tony.school.person.contructors.Person;

public class State {
	
	private String name, abbreviation;
	private List<Person> habitants = new ArrayList<>();
	
	public State(String name, String abbreviation) {
		this.name = name;
		this.abbreviation = abbreviation;
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
	
	public List<Person> getHabitants() {
		return habitants;
	}

}
