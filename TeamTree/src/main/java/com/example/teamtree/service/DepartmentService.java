package com.example.teamtree.service;

import com.example.teamtree.model.Department;
import com.example.teamtree.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Creează sau actualizează un departament
    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    // Obține toate departamentele
    public List<Department> getAll() {
        return departmentRepository.findAll();
    }

    // Obține departament după ID
    public Optional<Department> getById(Long id) {
        return departmentRepository.findById(id);
    }

    // Șterge departament după ID
    public void deleteById(Long id) {
        departmentRepository.deleteById(id);
    }

}
