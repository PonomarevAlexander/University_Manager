package com.foxminded.university.domain.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.foxminded.university.domain.exceptions.ServiceException;
import com.foxminded.university.domain.models.Group;
import com.foxminded.university.domain.services.DepartmentService;
import com.foxminded.university.domain.services.GroupService;
import com.foxminded.university.domain.services.TeacherService;
import com.foxminded.university.domain.services.TimetableService;

@Controller
@RequestMapping("/groups")
public class GroupController {
    
    private final GroupService groupService;
    private final TeacherService teacherService;
    private final TimetableService timetableService;
    private final DepartmentService departmentService;

    @Autowired
    public GroupController(GroupService groupService, TeacherService teachersService, TimetableService timetableService,
            DepartmentService departmentService) {
        this.groupService = groupService;
        this.teacherService = teachersService;
        this.timetableService = timetableService;
        this.departmentService = departmentService;
        
    }
    
    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("groups", groupService.getAll());
        return "group/all_groups";
    }
    
    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        Group group = groupService.getById(id);
        model.addAttribute("group", group);
        model.addAttribute("students", group.getStudentList());
        return "group/group";
    }
    
    @GetMapping("/new")
    public String createGroupForm(Model model) {
        model.addAttribute("group", new Group());
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("timetables", timetableService.getAll());
        model.addAttribute("departments", departmentService.getAll());
        return "group/new_group";
    }
    
    @PostMapping()
    public String add(@ModelAttribute("group") Group group) {
        groupService.add(group);
        return "redirect:/groups";
    }
    
    @GetMapping("/{id}/update")
    public String updateForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("group", groupService.getById(id));
        model.addAttribute("teachers", teacherService.getAll());
        model.addAttribute("timetables", timetableService.getAll());
        model.addAttribute("departments", departmentService.getAll());
        return "group/update_group";
    }
    
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("group") Group group, @PathVariable("id") int id) {
        groupService.update(group);
        return "redirect:/groups";
    }
    
    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        groupService.remove(id);
        return "redirect:/groups";
    }
    
    @ExceptionHandler(ServiceException.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("message", ex.getMessage());
        return "error";
    }
    
}