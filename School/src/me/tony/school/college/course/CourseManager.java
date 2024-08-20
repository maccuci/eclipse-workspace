package me.tony.school.college.course;

import java.util.HashMap;
import java.util.Map;

import me.tony.school.college.teacher.Teacher;

public class CourseManager {

	private static Map<String, Map<Course, Teacher>> courses = new HashMap<>();

	public void registerCourse(Course course, Teacher teacher) {
		Map<Course, Teacher> map = new HashMap<>();
		
		map.put(course, teacher);
		courses.put(course.getName(), map);
		
		course.setTeacher(teacher);
	}

	public Map<String, Map<Course, Teacher>> getAllCourses() {
		return courses;
	}

	public Course getCourseByName(String name) {
		for (Map<Course, Teacher> map : courses.values()) {
			for (Course course : map.keySet()) {
				if (course.getName() == name)
					return course;
			}
		}
		return null;
	}

	public Course getCourseById(int id) {
		for (Map<Course, Teacher> map : courses.values()) {
			for (Course course : map.keySet()) {
				if (course.getId() == id)
					return course;
			}
		}
		return null;
	}
}
