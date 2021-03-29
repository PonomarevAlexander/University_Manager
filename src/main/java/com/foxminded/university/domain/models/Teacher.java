package com.foxminded.university.domain.models;

public class Teacher {

    private int id;
    private String name;
    private String lastName;
    private Timetable timetable;

    public Teacher() {}

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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        return "Teacher [id=" + id + ", name=" + name + ", lastName=" + lastName + ", timetable=" + timetable + "]";
    }
}
