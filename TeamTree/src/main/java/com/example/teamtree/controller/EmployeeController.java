package com.example.teamtree.controller;

import com.example.teamtree.model.Employee;
import com.example.teamtree.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Creare angajat
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee saved = employeeService.create(employee);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    // Obține toți angajații
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAll());
    }

    // Obține angajat după ID
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizare angajat
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employee) {
        employee.setId(id);
        return ResponseEntity.ok(employeeService.update(employee));
    }

    // Ștergere angajat
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Atribuire manager
    @PutMapping("/{id}/manager/{managerId}")
    public ResponseEntity<Employee> assignManager(@PathVariable Long id, @PathVariable Long managerId) {
        return ResponseEntity.ok(employeeService.assignManager(id, managerId));
    }

    // Obține subordonații unui angajat
    @GetMapping("/{id}/subordinates")
    public ResponseEntity<List<Employee>> getSubordinates(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getSubordinates(id));
    }
}
