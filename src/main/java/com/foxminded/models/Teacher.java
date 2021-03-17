package com.foxminded.models;

public class Teacher {

    private int id;
    private String name;
    private String lastName;
    private Timetable timetable;
    
    public Teacher(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public Teacher(int id, String name, String lastName, Timetable timetable) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.timetable = timetable;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
}
