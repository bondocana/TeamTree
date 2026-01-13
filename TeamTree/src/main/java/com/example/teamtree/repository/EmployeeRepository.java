package com.example.teamtree.repository;

import com.example.teamtree.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // Exemplu de query custom: găsește toți angajații unui manager
    List<Employee> findByManagerId(Long managerId);

    // Alte metode custom le poți adăuga după nevoie
}
