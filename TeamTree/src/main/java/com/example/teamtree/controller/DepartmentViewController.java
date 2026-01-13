package com.example.teamtree.controller;

import com.example.teamtree.model.Department;
import com.example.teamtree.service.DepartmentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/departments")
public class DepartmentViewController {

    private final DepartmentService departmentService;

    public DepartmentViewController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public String listDepartments(Model model) {
        model.addAttribute("departments", departmentService.getAll());
        model.addAttribute("pageTitle", "Departments");
        return "departments";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());
        model.addAttribute("pageTitle", "Add Department");
        return "department_form";
    }

    @PostMapping("/save")
    public String saveDepartment(@ModelAttribute Department department) {
        departmentService.save(department);
        return "redirect:/departments";
    }
}
