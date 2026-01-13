package com.example.teamtree.repository;

import com.example.teamtree.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    // JpaRepository oferă metode de bază: save(), findAll(), findById(), deleteById()
}
