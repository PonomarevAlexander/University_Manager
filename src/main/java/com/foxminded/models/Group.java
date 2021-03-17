package com.foxminded.models;

import java.util.ArrayList;
import java.util.List;

public class Group {

    private int id;
    private String name;
    private Teacher cheef;
    private List<Student> studentList;
    private Timetable timetable;

    public Group(String name, Teacher cheef) {
        this.name = name;
        this.cheef = cheef;
        this.studentList = new ArrayList<>();
    }

    public Group(int id, String name, Teacher cheef, List<Student> studentList, Timetable timetable) {
        this.id = id;
        this.name = name;
        this.cheef = cheef;
        this.studentList = studentList;
        this.timetable = timetable;
    }

    public boolean addStudent(Student student) {
        return studentList.add(student);
    }
    
    public boolean removeStudent(Student student) {
        return studentList.remove(student);
    }
    
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public Teacher getCheef() {
        return cheef;
    }
    public List<Student> getStudentList() {
        return studentList;
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
    public void setCheef(Teacher cheef) {
        this.cheef = cheef;
    }
    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }
    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }
}
