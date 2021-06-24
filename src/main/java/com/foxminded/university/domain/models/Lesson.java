package com.foxminded.university.domain.models;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {
    
    private static final String FORMAT = "yyyy-MM-dd HH:mm";

    @Id
    @GeneratedValue()
    private int id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "duration")
    private int lessonDurationSecond;
    
    @ManyToOne()
    private Teacher teacher;
    
    @ManyToOne()
    private Group group;

    public Lesson() {
    }

    public Lesson(String name, LocalDateTime startTime, int lessonDurationSecond) {
        this.name = name;
        this.startTime = startTime;
        this.lessonDurationSecond = lessonDurationSecond;
    }

    public Lesson(int id, String name, LocalDateTime startTime, int lessonDurationSecond, Teacher teacher,
            Group group) {
        super();
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.lessonDurationSecond = lessonDurationSecond;
        this.teacher = teacher;
        this.group = group;
    }

    public static String getFormat() {
        return FORMAT;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public int getLessonDurationSecond() {
        return lessonDurationSecond;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Group getGroup() {
        return group;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setLessonDurationSecond(int lessonDurationSecond) {
        this.lessonDurationSecond = lessonDurationSecond;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((group == null) ? 0 : group.hashCode());
        result = prime * result + id;
        result = prime * result + lessonDurationSecond;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
        result = prime * result + ((teacher == null) ? 0 : teacher.hashCode());
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
        Lesson other = (Lesson) obj;
        if (group == null) {
            if (other.group != null)
                return false;
        } else if (!group.equals(other.group))
            return false;
        if (id != other.id)
            return false;
        if (lessonDurationSecond != other.lessonDurationSecond)
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (startTime == null) {
            if (other.startTime != null)
                return false;
        } else if (!startTime.equals(other.startTime))
            return false;
        if (teacher == null) {
            if (other.teacher != null)
                return false;
        } else if (!teacher.equals(other.teacher))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Lesson [id=" + id + ", name=" + name + ", startTime=" + startTime + ", lessonDurationSecond="
                + lessonDurationSecond + ", teacher=" + teacher + ", group=" + group + "]";
    }

}
