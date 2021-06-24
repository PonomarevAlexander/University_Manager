package com.foxminded.university.domain.controllers;

import java.time.LocalDateTime;
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
    
    private static final String MODEL_LESSONS = "lessons";
    private static final String MODEL_LESSON = "lesson";
    private static final String MODEL_GROUPS = "groups";
    private static final String MODEL_TEACHERS = "teachers";
    private static final String VIEW_ALL_LESSONS = "lesson/all-lessons";
    private static final String VIEW_ONE_LESSON = "lesson/lesson";
    private static final String VIEW_NEW_LESSON = "lesson/new-lesson";
    private static final String VIEW_UPDATE_LESSON = "lesson/update-lesson";
    private static final String VIEW_REDIRECT_TO_ALL_LESSONS = "redirect:/lessons";
    private static final String DATETIME = "datetime";
    
    @Autowired
    public LessonController(LessonService lessonService, TeacherService teacherService, GroupService groupService) {
        this.lessonService = lessonService;
        this.teacherService = teacherService;
        this.groupService = groupService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute(MODEL_LESSONS, lessonService.getAll());
        return VIEW_ALL_LESSONS;
    }
    
    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        Lesson lesson = lessonService.getById(id);
        model.addAttribute(MODEL_LESSON, lesson);
        return VIEW_ONE_LESSON;
    }
    
    @GetMapping("/new")
    public String newLessonForm(Model model) {
        model.addAttribute(MODEL_LESSON, new Lesson());
        model.addAttribute(MODEL_TEACHERS, teacherService.getAll());
        model.addAttribute(MODEL_GROUPS, groupService.getAll());
        return VIEW_NEW_LESSON;
    }
    
    @PostMapping()
    public String add(@ModelAttribute(MODEL_LESSON) Lesson lesson, @RequestParam(DATETIME) String datetime) {
        lesson.setStartTime(LocalDateTime.parse(datetime));
        lessonService.add(lesson);
        return VIEW_REDIRECT_TO_ALL_LESSONS;
    }
    
    @GetMapping("/{id}/update")
    public String getUpdateForm(Model model, @PathVariable("id") int id) {
        model.addAttribute(MODEL_LESSON, lessonService.getById(id));
        model.addAttribute(MODEL_TEACHERS, teacherService.getAll());
        model.addAttribute(MODEL_GROUPS, groupService.getAll());
        return VIEW_UPDATE_LESSON;
    }
    
    @PostMapping("/{id}/update")
    public String update(@ModelAttribute(MODEL_LESSON) Lesson lesson, @RequestParam(DATETIME) String datetime) {
        lesson.setStartTime(LocalDateTime.parse(datetime));
        lessonService.update(lesson);
        return VIEW_REDIRECT_TO_ALL_LESSONS;
    }
    
    @PostMapping("/{id}/delete")
    public String remove(@PathVariable("id") int id) {
        lessonService.remove(id);
        return VIEW_REDIRECT_TO_ALL_LESSONS;
    }

}
