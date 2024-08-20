package me.tony.school.college.course;

import java.util.ArrayList;
import java.util.List;

import me.tony.school.college.discipline.Discipline;
import me.tony.school.college.teacher.Teacher;

public class Course {
	
	private String name;
	private int id;
	private Teacher teacher;
	
	private List<Discipline> disciplines = new ArrayList<>();
	
	public Course(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getId() {
		return id;
	}
	
	public Teacher getTeacher() {
		return teacher;
	}
	
	public List<Discipline> getDisciplines() {
		return disciplines;
	}
	
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public void addDiscipline(Discipline discipline) {
		if(disciplines.contains(discipline))
			return;
		disciplines.add(discipline);
	}
	
	public String showInformations() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("============= CURSO =============\n");
		stringBuilder.append("Nome do curso: " + getName() + "\n");
		stringBuilder.append("Coordernador do curso: " + getTeacher().getName() + "\n");
		int students = 0;
		stringBuilder.append("Disciplinas: \n");
		for (Discipline discipline : getDisciplines()) {
			if(discipline.getCourse() == this) {
				students += discipline.getStudents().size();
				stringBuilder.append(
						"  > " + discipline.getName() + " (" + discipline.getTeacher().getName() + ")\n");	
			}
		}
		stringBuilder.append("Alunos: " + students + "\n");
		stringBuilder.append("============= CURSO =============");
		return stringBuilder.toString();
	}
}
