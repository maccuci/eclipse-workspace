package me.tony.school.person.contructors;

import me.tony.school.person.contructors.city.City;
import me.tony.school.person.contructors.state.State;
import me.tony.school.person.manager.CpfManager;

public class Person {
	
	private String name, cpf;
	private State state;
	private City city;
	private int age;
	
	public Person(String name) {
		this.name = name;
		this.cpf = CpfManager.generateCpf();
	}
	
	public String getName() {
		return name;
	}
	
	public String getCPF() {
		return cpf;
	}
	
	public State getState() {
		return state;
	}
	
	public int getAge() {
		return age;
	}
	
	public City getCity() {
		return city;
	}
	
	public void setState(State state) {
		this.state = state;
	}
	
	public void setCity(City city) {
		this.city = city;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public void registerHabitant() {
		city.addHabitant(this);
		state.addHabitant(this);
	}
	
	public String showPersonInformations() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("============= PESSOA =============\n");
		stringBuilder.append("Nome: " + getName() + "\n");
		stringBuilder.append("Idade: " + (getAge() == 0 ? "Não definida." : getAge()) + "\n");
		stringBuilder.append("CPF: " + (getCPF() == null ? "Não registrado." : getCPF()) + "\n");
		stringBuilder.append("Estado: " + (getState() == null ? "Não encontrado." : getState().getName()) + "\n");
		stringBuilder.append("Cidade: " + (getCity() == null ? "Não encontrada." : getCity().getName()) + "\n");
		stringBuilder.append("============= PESSOA =============");
		
		return stringBuilder.toString();
	}
}
