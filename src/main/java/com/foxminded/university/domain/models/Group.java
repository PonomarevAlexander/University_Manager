package com.foxminded.university.domain.models;

import java.util.List;

public class Group {

    private int id;
    private String name;
    private Teacher teacher;
    private Department department;
    private List<Student> studentList;
    private Timetable timetable;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(int id, String name, Teacher teacher, List<Student> studentList, Timetable timetable, Department department) {
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.studentList = studentList;
        this.timetable = timetable;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + ", teacher=" + teacher + ", department=" + department
                + ", studentList=" + studentList + ", timetable=" + timetable + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((department == null) ? 0 : department.hashCode());
        result = prime * result + id;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((studentList == null) ? 0 : studentList.hashCode());
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
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
        if (department == null) {
            if (other.department != null)
                return false;
        } else if (!department.equals(other.department))
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
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        if (timetable == null) {
            if (other.timetable != null)
                return false;
        } else if (!timetable.equals(other.timetable))
            return false;
        return true;
    }
}
