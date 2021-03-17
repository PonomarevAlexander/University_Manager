package com.foxminded.models;

import java.time.LocalDateTime;

public class Lesson {

    private int id;
    private String name;
    private LocalDateTime startTime;
    private int lessonDurationSecond;
    private Teacher teacher;
    
    public Lesson(String name, LocalDateTime startTime, int lessonDurationSecond, Teacher teacher) {
        this.name = name;
        this.startTime = startTime;
        this.lessonDurationSecond = lessonDurationSecond;
        this.teacher = teacher;
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
}
