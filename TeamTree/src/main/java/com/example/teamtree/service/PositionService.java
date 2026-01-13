package com.example.teamtree.service;

import com.example.teamtree.model.Position;
import com.example.teamtree.repository.PositionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PositionService {

    private final PositionRepository positionRepository;

    public PositionService(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    // Creează sau actualizează un departament
    public Position save(Position position) {
        return positionRepository.save(position);
    }

    // Obține toate departamentele
    public List<Position> getAll() {
        return positionRepository.findAll();
    }

    // Obține departament după ID
    public Optional<Position> getById(Long id) {
        return positionRepository.findById(id);
    }

    // Șterge departament după ID
    public void deleteById(Long id) {
        positionRepository.deleteById(id);
    }

}
