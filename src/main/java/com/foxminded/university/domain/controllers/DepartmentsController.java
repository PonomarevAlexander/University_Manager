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
import com.foxminded.university.domain.models.Department;
import com.foxminded.university.domain.services.DepartmentService;

@Controller
@RequestMapping("/departments")
public class DepartmentsController {

    private final DepartmentService departmentService;
    private static final String MODEL_NAME_DEPARTMENT = "department";
    private static final String MODEL_ALL_DEPARTMENTS = "departments";
    private static final String MODEL_NAME_GROUPS = "groups";
    private static final String MODEL_NAME_TEACHERS = "teachers";
    private static final String VIEW_ALL_DEPARTMENTS = "department/all-departments";
    private static final String VIEW_DEPARTMENT = "department/department";
    private static final String VIEW_ADD_DEPARTMENT = "department/add-department";
    private static final String VIEW_UPDATE_DEPARTMENT = "department/update-department";
    private static final String VIEW_REDIRECT_TO_ALL_DEPARTMENTS = "redirect:/departments";
    
    @Autowired
    public DepartmentsController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute(MODEL_ALL_DEPARTMENTS, departmentService.getAll());
        return VIEW_ALL_DEPARTMENTS;
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        Department department = departmentService.getById(id);
        model.addAttribute(MODEL_NAME_DEPARTMENT, department);
        model.addAttribute(MODEL_NAME_GROUPS, department.getGroupList());
        model.addAttribute(MODEL_NAME_TEACHERS, department.getTeacherList());
        return VIEW_DEPARTMENT;
    }
    
    @GetMapping("/new")
    public String newDepartmentFrom(@ModelAttribute(MODEL_NAME_DEPARTMENT) Department department) {
        return VIEW_ADD_DEPARTMENT;
    }
    
    @PostMapping()
    public String add(@ModelAttribute(MODEL_NAME_DEPARTMENT) Department department) {
        departmentService.add(department);
        return VIEW_REDIRECT_TO_ALL_DEPARTMENTS;
    }
    
    @GetMapping("/{id}/update")
    public String updateForm(Model model, @PathVariable("id") int id) {
        model.addAttribute(MODEL_NAME_DEPARTMENT, departmentService.getById(id));
        return VIEW_UPDATE_DEPARTMENT;
    }
    
    @PatchMapping("/{id}")
    public String update(@ModelAttribute(MODEL_NAME_DEPARTMENT) Department department, @PathVariable("id") int id) {
        departmentService.update(department); 
        return VIEW_REDIRECT_TO_ALL_DEPARTMENTS;
    }
    
    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        departmentService.remove(id);
        return VIEW_REDIRECT_TO_ALL_DEPARTMENTS;
    }

}
