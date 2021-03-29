package com.foxminded.university.domain.models;

import java.util.List;

public class Group {

    private int id;
    private String name;
    private Teacher headOfGroup;
    private List<Student> studentList;
    private Timetable timetable;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(int id, String name, Teacher cheef, List<Student> studentList, Timetable timetable) {
        this.id = id;
        this.name = name;
        this.headOfGroup = cheef;
        this.studentList = studentList;
        this.timetable = timetable;
    }

    public void addStudent(Student student) {
        studentList.add(student);
    }

    public void removeStudent(Student student) {
        studentList.remove(student);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Teacher getCheef() {
        return headOfGroup;
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
        this.headOfGroup = cheef;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((headOfGroup == null) ? 0 : headOfGroup.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((studentList == null) ? 0 : studentList.hashCode());
        result = prime * result + ((timetable == null) ? 0 : timetable.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Group other = (Group) obj;
        if (headOfGroup == null) {
            if (other.headOfGroup != null)
                return false;
        } else if (!headOfGroup.equals(other.headOfGroup))
            return false;
        if (id != other.id)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (studentList == null) {
            if (other.studentList != null)
                return false;
        } else if (!studentList.equals(other.studentList))
            return false;
        if (timetable == null) {
            if (other.timetable != null)
                return false;
        } else if (!timetable.equals(other.timetable))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + ", headOfGroup=" + headOfGroup + ", studentList=" + studentList
                + ", timetable=" + timetable + "]";
    }
}
