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

    @Autowired
    public StudentController(StudentService studentService, GroupService groupService) {
        this.studentService = studentService;
        this.groupService = groupService;
    }
    
    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("students", studentService.getAll());
        return "student/all_students";
    }
    
    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        model.addAttribute("student", studentService.getById(id));
        return "student/student";
    }
    
    @GetMapping("/new")
    public String getCreatingForm(@ModelAttribute("student") Student student, Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "student/new_student";
    }
    
    @PostMapping()
    public String add(@ModelAttribute("student") Student student) {
        studentService.add(student);
        return "redirect:/students";
    }
    
    @GetMapping("/{id}/update")
    public String getUpdatingForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("student", studentService.getById(id));
        model.addAttribute("groups", groupService.getAll());
        return "student/update_student";
    }
    
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("student") Student student) {
        studentService.update(student);
        return "redirect:/students";
    }
    
    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        studentService.remove(id);
        return "redirect:/students";
    }

}
