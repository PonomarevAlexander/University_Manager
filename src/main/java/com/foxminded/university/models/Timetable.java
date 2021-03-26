package com.foxminded.university.models;

import java.time.LocalDateTime;
import java.util.List;

public class Timetable {

    private int id;
    private List<Lesson> schedule;
    private LocalDateTime creationDate;

    public Timetable() {
    }

    public Timetable(List<Lesson> schedule) {
        this.schedule = schedule;
    }

    public int getId() {
        return id;
    }

    public List<Lesson> getSchedule() {
        return schedule;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSchedule(List<Lesson> schedule) {
        this.schedule = schedule;
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
        if (schedule == null) {
            if (other.schedule != null)
                return false;
        } else if (!schedule.equals(other.schedule))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Timetable [id=" + id + ", schedule=" + schedule + ", creationDate=" + creationDate + "]";
    }
}
