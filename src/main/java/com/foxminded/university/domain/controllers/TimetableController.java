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

    private static final String MODEL_TIMETABLE = "timetable";
    private static final String MODEL_ALL_TIMETABLES = "timetables";
    private static final String MODEL_CREATED = "creationDate";
    private static final String MODEL_DATE_TIME = "datetime";
    private static final String MODEL_LESSONS = "lessons";
    private static final String MODEL_GROUPS = "groups";
    private static final String MODEL_TEACHERS = "teachers";
    private static final String MODEL_LESSON = "lesson";
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
    private static final String VIEW_ALL_TIMETABLES = "timetable/all_timetables";
    private static final String VIEW_NEW_TIMETABLES = "timetable/new_timetable";
    private static final String VIEW_TIMETABLE = "timetable/timetable";
    private static final String VIEW_UPDATE_TIMETABLE = "timetable/update_timetable";
    private static final String VIEW_REDIRECT_TO_STATUS_TIMETABLES = "redirect:/timetables";
    private static final String VIEW_REDIRECT_TO_PROFILE_TIMETABLE = "redirect:/timetables/{id}";
    private static final String VIEW_ADD_LESSON_TO_TIMETABLE = "timetable/add_lesson_to_timetable";

    @Autowired
    public TimetableController(TimetableService timetableService, TeacherService teacherService,
            GroupService groupService, LessonService lessonService) {
        this.timetableService = timetableService;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.lessonService = lessonService;

    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute(MODEL_ALL_TIMETABLES, timetableService.getAll());
        return VIEW_ALL_TIMETABLES;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        Timetable timetable = timetableService.getById(id);
        model.addAttribute(MODEL_TIMETABLE, timetable);
        model.addAttribute(MODEL_CREATED, timetable.getCreationDate().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
        return VIEW_TIMETABLE;
    }

    @GetMapping("/new")
    public String getCreationForm(@ModelAttribute(MODEL_TIMETABLE) Timetable timetable) {
        return VIEW_NEW_TIMETABLES;
    }

    @PostMapping()
    public String add(@ModelAttribute(MODEL_TIMETABLE) Timetable timetable) {
        timetable.setCreationDate(LocalDateTime.now());
        timetableService.add(timetable);
        return VIEW_REDIRECT_TO_STATUS_TIMETABLES;
    }

    @GetMapping("/{id}/update")
    public String getUpdatingForm(@PathVariable("id") int id, Model model) {
        Timetable timetable = timetableService.getById(id);
        model.addAttribute(MODEL_TIMETABLE, timetable);
        model.addAttribute(MODEL_LESSONS, timetable.getSchedule());
        return VIEW_UPDATE_TIMETABLE;
    }

    @GetMapping("/{id}/update/addLesson")
    public String addLessonForm(@ModelAttribute(MODEL_LESSON) Lesson lesson, @PathVariable("id") int id, Model model) {
        model.addAttribute(MODEL_TEACHERS, teacherService.getAll());
        model.addAttribute(MODEL_GROUPS, groupService.getAll());
        model.addAttribute(MODEL_TIMETABLE, timetableService.getById(id));
        return VIEW_ADD_LESSON_TO_TIMETABLE;
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute(MODEL_LESSON) Lesson lesson, @PathVariable("id") int id, @RequestParam(MODEL_DATE_TIME) String datetime) {
        Timetable timetable = timetableService.getById(id);
        lesson.setStartTime(LocalDateTime.parse(datetime));
        lesson.setId(lessonService.add(lesson));
        timetable.appendToSchedule(lesson);
        timetableService.update(timetable);
        return VIEW_REDIRECT_TO_PROFILE_TIMETABLE;
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        timetableService.remove(id);
        return VIEW_REDIRECT_TO_STATUS_TIMETABLES;
    }

}
