package com.example.teamtree.service;

import com.example.teamtree.model.Employee;
import com.example.teamtree.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // Creare angajat
    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Obține toți angajații
    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    // Obține angajat după ID
    public Optional<Employee> getById(Long id) {
        return employeeRepository.findById(id);
    }

    // Actualizare angajat
    public Employee update(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Ștergere angajat
    public void delete(Long id) {
        employeeRepository.deleteById(id);
    }

    // Atribuire manager unui angajat
    public Employee assignManager(Long employeeId, Long managerId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        Employee manager = employeeRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        if(employee.getId().equals(manager.getId())) {
            throw new RuntimeException("Employee cannot be their own manager");
        }

        employee.setManager(manager);
        return employeeRepository.save(employee);
    }

    // Obține subordonații unui angajat
    public List<Employee> getSubordinates(Long managerId) {
        return employeeRepository.findAll().stream()
                .filter(e -> e.getManager() != null && e.getManager().getId().equals(managerId))
                .toList();
    }

    public void deleteById(long l) {
    }
}
