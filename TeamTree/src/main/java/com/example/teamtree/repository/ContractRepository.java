package com.example.teamtree.repository;

import com.example.teamtree.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    // JpaRepository îți oferă metodele de bază:
    // save(), findAll(), findById(), deleteById() etc.
}
