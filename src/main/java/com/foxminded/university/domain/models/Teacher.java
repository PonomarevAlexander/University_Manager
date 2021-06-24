package com.foxminded.university.domain.models;

import java.util.List;

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
    
    @OneToOne(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private Group group;
    
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Lesson> timetable;

    public Teacher() {}

    public Teacher(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }

    public Teacher(int id, String name, String lastName, Department department, Group group, List<Lesson> timetable) {
        super();
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.department = department;
        this.group = group;
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

    public Group getGroup() {
        return group;
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

    public void setGroup(Group group) {
        this.group = group;
    }

    public void setTimetable(List<Lesson> timetable) {
        this.timetable = timetable;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((department == null) ? 0 : department.hashCode());
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + id;
        result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        Teacher other = (Teacher) obj;
        if (department == null) {
            if (other.department != null)
                return false;
        } else if (!department.equals(other.department))
            return false;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (id != other.id)
            return false;
        if (lastName == null) {
            if (other.lastName != null)
                return false;
        } else if (!lastName.equals(other.lastName))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
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
        return "Teacher [id=" + id + ", name=" + name + ", lastName=" + lastName + ", department=" + department
                + ", group=" + group + ", timetable=" + timetable + "]";
    }

}
