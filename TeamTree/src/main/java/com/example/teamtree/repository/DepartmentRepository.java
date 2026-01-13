package com.example.teamtree.repository;

import com.example.teamtree.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // JpaRepository oferă metodele de bază:
    // save(), findAll(), findById(), deleteById() etc.
}
