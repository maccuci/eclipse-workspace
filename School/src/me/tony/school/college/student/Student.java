package me.tony.school.college.student;

import java.util.ArrayList;
import java.util.List;

import me.tony.school.college.course.Course;
import me.tony.school.college.discipline.Discipline;

public class Student {
	
	private String name;
	private Course course;
	
	public Student(String name, Course course) {
		this.name = name;
		this.course = course;
	}
	
	public String getName() {
		return name;
	}
	
	public Course getCourse() {
		return course;
	}
	
	public List<Discipline> getDisciplines() {
		List<Discipline> disciplines = new ArrayList<>();
		
		for(Discipline discipline : Discipline.getDisciplines().values()) {
			if(discipline.getStudents().containsKey(this)) {
				disciplines.add(discipline);
			}
		}
		
		return disciplines;
	}
}
