package com.foxminded.models;

import java.util.List;

public class Timetable {

    private List<Lesson> schedule;

    public Timetable(List<Lesson> schedule) {
        this.schedule = schedule;
    }

    public List<Lesson> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Lesson> schedule) {
        this.schedule = schedule;
    }
}
