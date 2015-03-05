package com.classical;

public class ScheduleEvent {
	
	public String name;
	public String location;
	public float hourStart;
	public float duration;
	
	public ScheduleEvent(String name, String location, float hourStart, float duration) {
		this.name = name;
		this.location = location;
		this.hourStart = hourStart;
		this.duration = duration;
	}
	
	public static ScheduleEvent getScheduleEventFromCrn(int crn) {
		//TODO: PULL RELEVANT INFO FROM P.IO REQUESTS
		return null;
	}
	
	public String toJson() {
		return "";
	}
	
}