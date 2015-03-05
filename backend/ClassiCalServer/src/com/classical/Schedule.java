package com.classical;

import java.util.LinkedList;
import java.util.List;

public class Schedule {
	
	private List<ScheduleEvent> events;
	
	public Schedule() {
		this(new LinkedList<ScheduleEvent>());
	}
	
	public Schedule(List<ScheduleEvent> events) {
		this.events = events;
	}
	
	public static Schedule getScheduleFromCrn(int[] crn) {
		List<ScheduleEvent> events = new LinkedList<ScheduleEvent>();
		for (int i = 0; i < crn.length; i++) {
			events.add(ScheduleEvent.getScheduleEventFromCrn(crn[i]));
		}
		return new Schedule(events);
	}
	
	public List<ScheduleEvent> getEvents() {
		return events;
	}
	
	public void addEvent(ScheduleEvent event) {
		events.add(event);
	}
	
	public ScheduleEvent removeEvent(ScheduleEvent event) {
		if (events.remove(event)) {
			return event;
		}
		return null;
	}
	
}
