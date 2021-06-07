package com.foxminded.university.domain.controllers;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.domain.services.DepartmentService;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.TeacherService;

@Controller
@RequestMapping("/groups")
@Transactional
public class GroupController {

    private final GroupService groupService;
    private final TeacherService teacherService;
    private final DepartmentService departmentService;

    private static final String MODEL_GROUPS = "groups";
    private static final String MODEL_GROUP = "group";
    private static final String MODEL_DEPARTMENTS = "departments";
    private static final String MODEL_TEACHERS = "teachers";
    private static final String MODEL_STUDENTS = "students";
    private static final String MODEL_TIMETABLE = "timetable";
    private static final String VIEW_ALL_GROUP = "group/all-groups";
    private static final String VIEW_ONE_GROUP = "group/group";
    private static final String VIEW_NEW_GROUP = "group/new-group";
    private static final String VIEW_UPDATE_GROUP = "group/update-group";
    private static final String VIEW_REDIRECT_TO_ALL_GROUP = "redirect:/groups";

    @Autowired
    public GroupController(GroupService groupService, TeacherService teachersService, DepartmentService departmentService) {
        this.groupService = groupService;
        this.teacherService = teachersService;
        this.departmentService = departmentService;

    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute(MODEL_GROUPS, groupService.getAll());
        return VIEW_ALL_GROUP;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        Group group = groupService.getById(id);
        model.addAttribute(MODEL_GROUP, group);
        model.addAttribute(MODEL_STUDENTS, group.getStudentList());
        model.addAttribute(MODEL_TIMETABLE, group.getTimetable());
        return VIEW_ONE_GROUP;
    }

    @GetMapping("/new")
    public String createGroupForm(Model model) {
        model.addAttribute(MODEL_GROUP, new Group());
        model.addAttribute(MODEL_TEACHERS, teacherService.getAll());
        model.addAttribute(MODEL_DEPARTMENTS, departmentService.getAll());
        return VIEW_NEW_GROUP;
    }

    @PostMapping()
    public String add(@ModelAttribute("group") Group group) {
        groupService.add(group);
        return VIEW_REDIRECT_TO_ALL_GROUP;
    }

    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") int id, Model model) {
        model.addAttribute(MODEL_GROUP, groupService.getById(id));
        model.addAttribute(MODEL_TEACHERS, teacherService.getAll());
        model.addAttribute(MODEL_DEPARTMENTS, departmentService.getAll());
        return VIEW_UPDATE_GROUP;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(MODEL_GROUP) Group group, @PathVariable("id") int id) {
        groupService.update(group);
        return VIEW_REDIRECT_TO_ALL_GROUP;
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        groupService.remove(id);
        return VIEW_REDIRECT_TO_ALL_GROUP;
    }

}