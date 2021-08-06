package com.foxminded.university.domain.models;

import java.util.List;
import java.util.Objects;

import javax.persistence.*;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
@Table(name = "teachers")
public class Teacher {

    @Id
    @GeneratedValue()
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "last_name")
    private String lastName;
    
    @ManyToOne()
    private Department department;
    
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Lesson> timetable;

    public Teacher() {}

    public Teacher(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public Teacher(int id, String name, String lastName, Department department, List<Lesson> timetable) {
        super();
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.department = department;
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

    public Department getDepartment() {
        return department;
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

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setTimetable(List<Lesson> timetable) {
        this.timetable = timetable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Teacher teacher = (Teacher) o;
        return id == teacher.id &&
                Objects.equals(name, teacher.name) &&
                Objects.equals(lastName, teacher.lastName) &&
                Objects.equals(department, teacher.department) &&
                Objects.equals(timetable, teacher.timetable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lastName, department, timetable);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", department=" + department +
                ", timetable=" + timetable +
                '}';
    }
}
