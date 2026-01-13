package com.example.teamtree.repository;

import com.example.teamtree.model.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    // JpaRepository oferă metode de bază: save(), findAll(), findById(), deleteById()
}
