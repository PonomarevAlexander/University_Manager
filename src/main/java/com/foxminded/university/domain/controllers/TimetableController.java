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
import com.foxminded.university.domain.models.Timetable;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.LessonService;
import com.foxminded.university.domain.services.TeacherService;
import com.foxminded.university.domain.services.TimetableService;

@Controller
@RequestMapping("/timetables")
public class TimetableController {
    
    private final TimetableService timetableService;
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final LessonService lessonService;
    
    @Autowired
    public TimetableController(TimetableService timetableService, TeacherService teacherService, GroupService groupService, LessonService lessonService) {
        this.timetableService = timetableService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.lessonService = lessonService;
        
    }
    
    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("timetables", timetableService.getAll());
        return "timetable/all_timetables";
    }
    
    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        Timetable timetable = timetableService.getById(id);
        model.addAttribute("timetable", timetable);
        model.addAttribute("creationDate", timetable.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        return "timetable/timetable";
    }
    
    @GetMapping("/new")
    public String getCreationForm(@ModelAttribute("timetable") Timetable timetable) {
        return "timetable/new_timetable";
    }
    
    @PostMapping()
    public String add(@ModelAttribute("timetable") Timetable timetable) {
        timetable.setCreationDate(LocalDateTime.now());
        timetableService.add(timetable);
        return "redirect:/timetables";
    }
    
    @GetMapping("/{id}/update")
    public String getUpdatingForm(@PathVariable("id") int id, Model model) {
        Timetable timetable = timetableService.getById(id);
        model.addAttribute("timetable", timetable);
        model.addAttribute("lessons", timetable.getSchedule());
        return "timetable/update_timetable";
    }
    
    @GetMapping("/{id}/update/addLesson")
    public String addLessonForm(@ModelAttribute("lesson") Lesson lesson, @PathVariable("id") int id, Model model) {
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("groups", groupService.getAll());
        model.addAttribute("timetable", timetableService.getById(id));
        return "timetable/add_lesson_to_timetable";
    }
    
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("lesson") Lesson lesson, @PathVariable("id") int id, @RequestParam("datetime") String datetime) {
        Timetable timetable = timetableService.getById(id);
        lesson.setStartTime(LocalDateTime.parse(datetime));
        lesson.setId(lessonService.add(lesson));
        timetable.appendToSchedule(lesson);
        timetableService.update(timetable);
        return "redirect:/timetables/{id}";
    }
    
    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        timetableService.remove(id);
        return "redirect:/timetables";
    }
    
}
