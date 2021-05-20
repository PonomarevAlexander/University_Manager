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
    
    @Autowired
    public DepartmentsController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping()
    public String getAll(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        return "department/all_departments";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") int id, Model model) {
        Department department = departmentService.getById(id);
        model.addAttribute("department", department);
        model.addAttribute("groups", department.getGroupList());
        model.addAttribute("teachers", department.getTeacherList());
        return "department/department";
    }
    
    @GetMapping("/new")
    public String newDepartmentFrom(@ModelAttribute("department") Department department) {
        return "department/add_department";
    }
    
    @PostMapping()
    public String add(@ModelAttribute("department") Department department) {
        departmentService.add(department);
        return "redirect:/departments";
    }
    
    @GetMapping("/{id}/update")
    public String updateForm(Model model, @PathVariable("id") int id) {
        model.addAttribute("department", departmentService.getById(id));
        return "department/update_department";
    }
    
    @PatchMapping("/{id}")
    public String update(@ModelAttribute("department") Department department, @PathVariable("id") int id) {
        departmentService.update(department); 
        return "redirect:/departments";
    }
    
    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        departmentService.remove(id);
        return "redirect:/departments";
    }

}
