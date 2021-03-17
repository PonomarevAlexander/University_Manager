package com.foxminded.models;

import java.util.ArrayList;
import java.util.List;

public class Department {

    private int id;
    private String name;
    private List<Teacher> teacherList;
    private List<Group> groupList;
    
    public Department(String name) {
        this.name = name;
        this.teacherList = new ArrayList<>();
        this.groupList = new ArrayList<>();
    }

    public Department(String name, List<Teacher> teacherList, List<Group> groupList) {
        this.name = name;
        this.teacherList = teacherList;
        this.groupList = groupList;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Teacher> getTeacherList() {
        return teacherList;
    }

    public List<Group> getGroupList() {
        return groupList;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeacherList(List<Teacher> teacherList) {
        this.teacherList = teacherList;
    }

    public void setGroupList(List<Group> groupList) {
        this.groupList = groupList;
    }
    
}
