package com.example.teamtree.service;

import com.example.teamtree.model.Salary;
import com.example.teamtree.repository.SalaryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalaryService {

    private final SalaryRepository salaryRepository;

    public SalaryService(SalaryRepository salaryRepository) {
        this.salaryRepository = salaryRepository;
    }

    // Creează sau actualizează salariu
    public Salary save(Salary salary) {
        return salaryRepository.save(salary);
    }

    // Toate salariile
    public List<Salary> getAll() {
        return salaryRepository.findAll();
    }

    // După ID
    public Optional<Salary> getById(Long id) {
        return salaryRepository.findById(id);
    }

    // Ștergere
    public void deleteById(Long id) {
        salaryRepository.deleteById(id);
    }

    // TOATE salariile unui angajat (istoric)
    public List<Salary> getByEmployeeId(Long employeeId) {
        return salaryRepository.findByEmployeeId(employeeId);
    }

    // Salariul activ al unui angajat
    public Optional<Salary> getCurrentSalary(Long employeeId) {
        return salaryRepository.findByEmployeeIdAndEndDateIsNull(employeeId);
    }
}


