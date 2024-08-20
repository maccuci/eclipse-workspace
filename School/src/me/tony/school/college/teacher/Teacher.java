package me.tony.school.college.teacher;

import me.tony.school.college.discipline.Discipline;

public class Teacher {
	
	private String name;
	
	public Teacher(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public String showInformations() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("============= PROFESSOR =============\n");
		stringBuilder.append("Nome: " + getName() + "\n");
		int disciplines = 0;
		stringBuilder.append("Disciplinas: \n");
		for(Discipline discipline : Discipline.getDisciplines().values()) {
			if(discipline.getTeacher().getName() == getName()) {
				disciplines += Discipline.getDisciplines().size();
				stringBuilder.append(disciplines + "\n");
			}
		}
		stringBuilder.append("============= PROFESSOR =============");
		
		return stringBuilder.toString();
	}
}
