package me.tony.school.college.discipline;

import java.util.HashMap;
import java.util.Map;

import me.tony.school.college.course.Course;
import me.tony.school.college.student.Student;
import me.tony.school.college.teacher.Teacher;

public class Discipline {
	
	private String name;
	private Teacher teacher;
	private Course course;
	
	private Map<Student, Situation> students = new HashMap<>();
	private static HashMap<String, Discipline> disciplines = new HashMap<>();
	
	public static HashMap<String, Discipline> getDisciplines() {
		return disciplines;
	}
	
	public Discipline getDiscipline(String name) {
		if(disciplines.containsKey(name))
			return disciplines.get(name);
		return null;
	}
	
	public Discipline(String name, Course course) {
		this.name = name;
		this.course = course;
	}
	
	public String getName() {
		return name;
	}
	
	public Teacher getTeacher() {
		return teacher;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
	
	public String showInformations() {
		StringBuilder stringBuilder = new StringBuilder();
		
		stringBuilder.append("============= DISCIPLINA =============\n");
		stringBuilder.append("Nome: " + getName() + "\n");
		stringBuilder.append("Professor: " + getTeacher().getName() + "\n");
		stringBuilder.append("Estudantes: \n");
		if(getStudents().isEmpty()) {
			stringBuilder.append("Não há estudantes nesta disciplina.\n");
		} else {
			for (Student student : getStudents().keySet()) {
				stringBuilder.append(
						"  > " + student.getName() + " (" + getStudents().get(student).getName() + ")\n");
			}
		}
		stringBuilder.append("============= DISCIPLINA =============");
		
		return stringBuilder.toString();
	}
	
	public Map<Student, Situation> getStudents() {
		return students;
	}
	
	public void register() {
		disciplines.put(name, this);
	}
	
	public void addStudent(Student student, Situation situation) {
		if(students.containsKey(student))
			return;
		students.put(student, situation);
	}
	
	public static enum Situation {
		REGISTRERED("Matriculado"), PENDENT("Pendente");

		private String name;

		Situation(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

}
