package com.foxminded.models;

import java.util.List;

public class Timetable {

	private int id;
    private List<Lesson> schedule;

    public Timetable(List<Lesson> schedule) {
        this.schedule = schedule;
    }

    public int getId() {
    	return id;
    }
    
    public List<Lesson> getSchedule() {
        return schedule;
    }

    public void setId(int id) {
    	this.id = id;
    }
    
    public void setSchedule(List<Lesson> schedule) {
        this.schedule = schedule;
    }
}
