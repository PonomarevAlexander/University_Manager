package com.foxminded.university.domain.models;

import java.util.List;
import java.util.Objects;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id &&
                Objects.equals(name, group.name) &&
                Objects.equals(department, group.department) &&
                Objects.equals(studentList, group.studentList) &&
                Objects.equals(timetable, group.timetable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, department, studentList, timetable);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", department=" + department +
                ", studentList=" + studentList +
                ", timetable=" + timetable +
                '}';
    }
}
