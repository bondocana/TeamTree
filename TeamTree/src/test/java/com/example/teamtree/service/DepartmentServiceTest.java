package com.example.teamtree.service;

import com.example.teamtree.model.Department;
import com.example.teamtree.repository.DepartmentRepository;
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
public class DepartmentServiceTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Test
    void testSaveDepartment() {
        Department dept = Department.builder().name("HR").build();
        Department savedDept = Department.builder().id(1L).name("HR").build();

        // Metoda ta din service este 'save'
        when(departmentRepository.save(any(Department.class))).thenReturn(savedDept);

        Department result = departmentService.save(dept);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("HR", result.getName());
        verify(departmentRepository, times(1)).save(dept);
    }

    @Test
    void testGetAllDepartments() {
        Department d1 = Department.builder().name("IT").build();
        Department d2 = Department.builder().name("Finance").build();

        when(departmentRepository.findAll()).thenReturn(Arrays.asList(d1, d2));

        List<Department> list = departmentService.getAll();

        assertEquals(2, list.size());
        verify(departmentRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        Department d = Department.builder().id(1L).name("IT").build();
        when(departmentRepository.findById(1L)).thenReturn(Optional.of(d));

        Optional<Department> found = departmentService.getById(1L);

        assertTrue(found.isPresent());
        assertEquals("IT", found.get().getName());
    }

    @Test
    void testGetById_NotFound() {
        when(departmentRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Department> found = departmentService.getById(99L);

        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById() {
        doNothing().when(departmentRepository).deleteById(1L);

        departmentService.deleteById(1L);

        verify(departmentRepository, times(1)).deleteById(1L);
    }
}