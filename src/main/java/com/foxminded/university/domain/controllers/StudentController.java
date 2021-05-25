package com.foxminded.university.domain.controllers;

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

import com.foxminded.university.domain.models.Student;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.StudentService;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final GroupService groupService;

    private static final String MODEL_STUDENT = "student";
    private static final String MODEL_ALL_STUDENTS = "students";
    private static final String MODEL_GROUPS = "groups";
    private static final String VIEW_ALL_STUDENTS = "student/all-students";
    private static final String VIEW_STUDENT = "student/student";
    private static final String VIEW_NEW_STUDENT = "student/new-student";
    private static final String VIEW_UPDATE_STUDENT = "student/update-student";
    private static final String VIEW_REDIRECT_TO_STATUS = "redirect:/students";

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute(MODEL_ALL_STUDENTS, studentService.getAll());
        return VIEW_ALL_STUDENTS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        model.addAttribute(MODEL_STUDENT, studentService.getById(id));
        return VIEW_STUDENT;
    }

    @GetMapping("/new")
    public String getCreatingForm(@ModelAttribute(MODEL_STUDENT) Student student, Model model) {
        model.addAttribute(MODEL_GROUPS, groupService.getAll());
        return VIEW_NEW_STUDENT;
    }

    @PostMapping()
    public String add(@ModelAttribute(MODEL_STUDENT) Student student) {
        studentService.add(student);
        return VIEW_REDIRECT_TO_STATUS;
    }

    @GetMapping("/{id}/update")
    public String getUpdatingForm(@PathVariable("id") int id, Model model) {
        model.addAttribute(MODEL_STUDENT, studentService.getById(id));
        model.addAttribute(MODEL_GROUPS, groupService.getAll());
        return VIEW_UPDATE_STUDENT;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(MODEL_STUDENT) Student student) {
        studentService.update(student);
        return VIEW_REDIRECT_TO_STATUS;
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        studentService.remove(id);
        return VIEW_REDIRECT_TO_STATUS;
    }

}
