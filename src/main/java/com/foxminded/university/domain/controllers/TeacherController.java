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

import com.foxminded.university.domain.models.Teacher;
import com.foxminded.university.domain.services.TeacherService;
import com.foxminded.university.domain.services.TimetableService;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final TimetableService timetableService;

    @Autowired
    public TeacherController(TeacherService teacherService, TimetableService timeTableService) {
        this.teacherService = teacherService;
        this.timetableService = timeTableService;
    }
    
    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        return "teacher/all_teachers";
    }
    
    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable("id") int id) {
        model.addAttribute("teacher", teacherService.getById(id));
        return "teacher/teacher";
    }
    
    @GetMapping("/new")
    public String getCreatingFrom(@ModelAttribute("teacher") Teacher teacher, Model model) {
        model.addAttribute("timetables", timetableService.getAll());
        return "teacher/new_teacher";
    }
    
    @PostMapping()
    public String add(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.add(teacher);
        return "redirect:/teachers";
    }
        
    @GetMapping("/{id}/update")
    public String getUpdatingForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("teacher", teacherService.getById(id));
        model.addAttribute("timetables", timetableService.getAll());
        return "teacher/update_teacher";
    }
    
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("teacher") Teacher teacher) {
        teacherService.update(teacher);
        return "redirect:/teachers";
    }
    
    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        teacherService.remove(id);
        return "redirect:/teachers";
    }
    
}
