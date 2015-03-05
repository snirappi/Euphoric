package com.classical;

import java.util.List;

public class University {
	
	private String name;
	private List<Course> courses; //should be DBCollection 	
	private List<User> users;
	
	public University(String name, List<Course> courses, List<User> users) {
		this.name = name;
		this.courses = courses;
		this.users = users;
	}
	
	public void newSemester(List<Course> newCourses) {
		courses.clear();
		courses.addAll(newCourses);
	}
	
	public List<Course> getCourses() {
		return courses;
	}
	
	public String getName() {
		return name;
	}
	
	public List<User> getUsers() {
		return users;
	}
}
