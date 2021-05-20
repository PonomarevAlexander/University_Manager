package com.foxminded.university.domain.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import org.springframework.web.bind.annotation.RequestParam;

import com.foxminded.university.domain.models.Lesson;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.LessonService;
import com.foxminded.university.domain.services.TeacherService;

@Controller
@RequestMapping("/lessons")
public class LessonController {
    
    private final LessonService lessonService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    
    @Autowired
    public LessonController(LessonService lessonService, TeacherService teacherService, GroupService groupService) {
        this.lessonService = lessonService;
        this.teacherService = teacherService;
        this.groupService = groupService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("lessons", lessonService.getAll());
        return "lesson/all_lessons";
    }
    
    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        Lesson lesson = lessonService.getById(id);
        model.addAttribute("lesson", lesson);
        model.addAttribute("time", lesson.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return "lesson/lesson";
    }
    
    @GetMapping("/new")
    public String newLessonForm(Model model) {
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("groups", groupService.getAll());
        return "lesson/new_lesson";
    }
    
    @PostMapping()
    public String add(@ModelAttribute("lesson") Lesson lesson, @RequestParam("datetime") String datetime) {
        lesson.setStartTime(LocalDateTime.parse(datetime));
        lessonService.add(lesson);
        return "redirect:/lessons";
    }
    
    @GetMapping("/{id}/update")
    public String getUpdateForm(Model model, @PathVariable("id") int id) {
        model.addAttribute("lesson", lessonService.getById(id));
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("groups", groupService.getAll());
        return "lesson/update_lesson";
    }
    
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("lesson") Lesson lesson, @RequestParam("datetime") String datetime) {
        lesson.setStartTime(LocalDateTime.parse(datetime));
        lessonService.update(lesson);
        return "redirect:/lessons";
    }
    
    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        lessonService.remove(id);
        return "redirect:/lessons";
    }

}
