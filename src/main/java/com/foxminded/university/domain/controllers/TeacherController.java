package com.foxminded.university.domain.controllers;

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
import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.services.DepartmentService;
import com.foxminded.university.domain.services.TeacherService;

@Controller
@RequestMapping("/teachers")
@Transactional
public class TeacherController {

    private final TeacherService teacherService;
    private final DepartmentService departmentService;

    private static final String MODEL_ALL_TEACHERS = "teachers";
    private static final String MODEL_TEACHER = "teacher";
    private static final String MODEL_TIMETABLE = "timetable";
    private static final String MODEL_ALL_DEPARTMENTS = "departments";
    private static final String VIEW_ALL_TEACHERS = "teacher/all-teachers";
    private static final String VIEW_TEACHER = "teacher/teacher";
    private static final String VIEW_NEW_TEACHER = "teacher/new-teacher";
    private static final String VIEW_UPDATE_TEACHER = "teacher/update-teacher";
    private static final String VIEW_REDIRECT_TO_TICHERS = "redirect:/teachers";

    @Autowired
    public TeacherController(TeacherService teacherService, DepartmentService departmentService) {
        this.teacherService = teacherService;
        this.departmentService = departmentService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute(MODEL_ALL_TEACHERS, teacherService.getAll());
        return VIEW_ALL_TEACHERS;
    }

    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable("id") int id) {
        Teacher teacher = teacherService.getById(id);
        model.addAttribute(MODEL_TEACHER, teacher);
        model.addAttribute(MODEL_TIMETABLE, teacher.getTimetable());
        return VIEW_TEACHER;
    }

    @GetMapping("/new")
    public String getCreatingFrom(@ModelAttribute(MODEL_TEACHER) Teacher teacher, Model model) {
        model.addAttribute(MODEL_ALL_DEPARTMENTS, departmentService.getAll());
        return VIEW_NEW_TEACHER;
    }

    @PostMapping()
    public String add(@ModelAttribute(MODEL_TEACHER) Teacher teacher) {
        teacherService.add(teacher);
        return VIEW_REDIRECT_TO_TICHERS;
    }

    @GetMapping("/{id}/update")
    public String getUpdatingForm(@PathVariable("id") int id, Model model) {
        model.addAttribute(MODEL_TEACHER, teacherService.getById(id));
        return VIEW_UPDATE_TEACHER;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(MODEL_TEACHER) Teacher teacher) {
        teacherService.update(teacher);
        return VIEW_REDIRECT_TO_TICHERS;
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        teacherService.remove(id);
        return VIEW_REDIRECT_TO_TICHERS;
    }

}
