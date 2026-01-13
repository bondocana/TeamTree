package com.example.teamtree.repository;

import com.example.teamtree.model.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {

    List<Salary> findByEmployeeId(Long employeeId);

    Optional<Salary> findByEmployeeIdAndEndDateIsNull(Long employeeId);
}


