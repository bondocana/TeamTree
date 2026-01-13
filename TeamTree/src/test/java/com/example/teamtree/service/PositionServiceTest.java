package com.example.teamtree.service;

import com.example.teamtree.model.Position;
import com.example.teamtree.repository.PositionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PositionServiceTest {

    @Mock
    private PositionRepository positionRepository;

    @InjectMocks
    private PositionService positionService;

    @Test
    void testSavePosition() {
        Position pos = Position.builder().title("Senior Java Dev").build();
        Position savedPos = Position.builder().id(1L).title("Senior Java Dev").build();

        // Metoda ta din service este 'save'
        when(positionRepository.save(any(Position.class))).thenReturn(savedPos);

        Position result = positionService.save(pos);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Senior Java Dev", result.getTitle());
        verify(positionRepository, times(1)).save(pos);
    }

    @Test
    void testGetAllPositions() {
        Position p1 = Position.builder().title("Junior Dev").build();
        Position p2 = Position.builder().title("Manager").build();

        when(positionRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Position> list = positionService.getAll();

        assertEquals(2, list.size());
        verify(positionRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        Position p = Position.builder().id(1L).title("QA Engineer").build();
        when(positionRepository.findById(1L)).thenReturn(Optional.of(p));

        Optional<Position> found = positionService.getById(1L);

        assertTrue(found.isPresent());
        assertEquals("QA Engineer", found.get().getTitle());
    }

    @Test
    void testGetById_NotFound() {
        when(positionRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Position> found = positionService.getById(99L);

        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById() {
        doNothing().when(positionRepository).deleteById(1L);

        positionService.deleteById(1L);

        verify(positionRepository, times(1)).deleteById(1L);
    }
}