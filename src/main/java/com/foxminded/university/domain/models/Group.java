package com.foxminded.university.domain.models;

import java.util.List;
import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "groups")
public class Group {

    @Id
    @GeneratedValue()
    private int id;

    @Column(name = "name")
    private String name;

    @OneToOne()
    @JoinColumn(name = "head")
    private Teacher teacher;

    @ManyToOne()
    private Department department;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Student> studentList;
    
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Lesson> timetable;

    public Group() {
    }

    public Group(String name) {
        this.name = name;
    }

    public Group(int id, String name, Teacher teacher, Department department, List<Student> studentList,
            List<Lesson> timetable) {
        super();
        this.id = id;
        this.name = name;
        this.teacher = teacher;
        this.department = department;
        this.studentList = studentList;
        this.timetable = timetable;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Department getDepartment() {
        return department;
    }

    public List<Student> getStudentList() {
        return studentList;
    }

    public List<Lesson> getTimetable() {
        return timetable;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setStudentList(List<Student> studentList) {
        this.studentList = studentList;
    }

    public void setTimetable(List<Lesson> timetable) {
        this.timetable = timetable;
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

    @Override
    public String toString() {
        return "Group [id=" + id + ", name=" + name + ", teacher=" + teacher + ", department=" + department
                + ", studentList=" + studentList + ", timetable=" + timetable + "]";
    }

}
