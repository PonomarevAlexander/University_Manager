package com.foxminded.university.domain.models;

import java.time.LocalDateTime;
import java.util.List;

public class Timetable {

    private int id;
    private String description;
    private List<Lesson> schedule;
    private LocalDateTime creationDate;

    public Timetable() {
    }

    public Timetable(List<Lesson> schedule) {
        this.schedule = schedule;
    }
    
    public Timetable(List<Lesson> schedule, String name) {
        this.schedule = schedule;
        this.description = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Lesson> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Lesson> schedule) {
        this.schedule = schedule;
    }
    
    public void appendToSchedule(Lesson lesson) {
        this.schedule.add(lesson);
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
        result = prime * result + id;
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((schedule == null) ? 0 : schedule.hashCode());
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
        Timetable other = (Timetable) obj;
        if (creationDate == null) {
            if (other.creationDate != null)
                return false;
        } else if (!creationDate.equals(other.creationDate))
            return false;
        if (id != other.id)
            return false;
        if (description == null) {
            if (other.description != null)
                return false;
        } else if (!description.equals(other.description))
            return false;
        if (schedule == null) {
            if (other.schedule != null)
                return false;
        } else if (!schedule.equals(other.schedule))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Timetable [id=" + id + ", name=" + description + ", schedule=" + schedule + ", creationDate=" + creationDate
                + "]";
    }

}
