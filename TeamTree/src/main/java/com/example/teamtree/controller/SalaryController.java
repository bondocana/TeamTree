package com.example.teamtree.controller;

import com.example.teamtree.model.Salary;
import com.example.teamtree.service.SalaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salaries")
public class SalaryController {

    private final SalaryService salaryService;

    public SalaryController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    // Get all salaries
    @GetMapping
    public ResponseEntity<List<Salary>> getAllSalaries() {
        return ResponseEntity.ok(salaryService.getAll());
    }

    // Get salary by ID
    @GetMapping("/{id}")
    public ResponseEntity<Salary> getSalaryById(@PathVariable Long id) {
        return salaryService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create or update salary
    @PostMapping
    public ResponseEntity<Salary> createSalary(@RequestBody Salary salary) {
        return ResponseEntity.ok(salaryService.save(salary));
    }

    // Delete salary
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalary(@PathVariable Long id) {
        salaryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // Optional: Get all salaries of a specific employee
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Salary>> getSalariesByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(salaryService.getByEmployeeId(employeeId));
    }
}
