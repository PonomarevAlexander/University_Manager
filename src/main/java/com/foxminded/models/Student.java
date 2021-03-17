package com.foxminded.models;

public class Student {

    private int id;
    private String name;
    private String lastName;
    private int age;
    private int grade;
    private Group group;
    
    public Student(String name, String lastName, int age) {
        this.name = name;
        this.lastName = lastName;
        this.age = age;
    }
    
    public Student(int id, String name, String lastName, int age, int grade, Group group) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.grade = grade;
        this.group = group;
    }

    public Group getGroup() {
        return group;
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
    
    public int getAge() {
        return age;
    }
    
    public int getGrade() {
        return grade;
    }
    
    public void setGroup(Group group) {
        this.group = group;
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
    
    public void setAge(int age) {
        this.age = age;
    }
    
    public void setGrade(int grade) {
        this.grade = grade;
    }
}
