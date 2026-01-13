package com.example.teamtree.service;

import com.example.teamtree.model.Employee;
import com.example.teamtree.repository.EmployeeRepository;
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
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void testCreateEmployee() {
        Employee employee = Employee.builder().firstName("Ion").lastName("Popescu").email("ion@test.com").build();
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee created = employeeService.create(employee);

        assertNotNull(created);
        assertEquals("Ion", created.getFirstName());
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void testGetAllEmployees() {
        Employee e1 = Employee.builder().firstName("A").build();
        Employee e2 = Employee.builder().firstName("B").build();
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Employee> list = employeeService.getAll();

        assertEquals(2, list.size());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        Employee e = Employee.builder().id(1L).firstName("Test").build();
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(e));

        Optional<Employee> found = employeeService.getById(1L);

        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getFirstName());
    }

    @Test
    void testGetById_NotFound() {
        when(employeeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Employee> found = employeeService.getById(99L);

        assertFalse(found.isPresent());
    }

    @Test
    void testUpdateEmployee() {
        Employee e = Employee.builder().id(1L).firstName("Updated").build();
        when(employeeRepository.save(e)).thenReturn(e);

        Employee updated = employeeService.update(e);

        assertEquals("Updated", updated.getFirstName());
        verify(employeeRepository, times(1)).save(e);
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.delete(1L);

        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testAssignManager_Success() {
        Employee angajat = Employee.builder().id(1L).firstName("Angajat").build();
        Employee manager = Employee.builder().id(2L).firstName("Sef").build();

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(angajat));
        when(employeeRepository.findById(2L)).thenReturn(Optional.of(manager));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Employee result = employeeService.assignManager(1L, 2L);

        assertNotNull(result.getManager());
        assertEquals(2L, result.getManager().getId());
    }

    @Test
    void testAssignManager_SelfAssignmentThrowsError() {
        Employee angajat = Employee.builder().id(1L).build();
        Employee manager = Employee.builder().id(1L).build(); // Același ID

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(angajat));
        // Repository returnează același obiect pentru ambele apeluri findById

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            employeeService.assignManager(1L, 1L);
        });

        assertEquals("Employee cannot be their own manager", exception.getMessage());
    }

    @Test
    void testGetSubordinates() {
        // Simulăm logica ta cu filter stream din service
        Employee manager = Employee.builder().id(10L).build();

        Employee sub1 = Employee.builder().id(1L).manager(manager).build();
        Employee sub2 = Employee.builder().id(2L).manager(manager).build();
        Employee independent = Employee.builder().id(3L).manager(null).build();

        when(employeeRepository.findAll()).thenReturn(Arrays.asList(sub1, sub2, independent));

        List<Employee> subordinates = employeeService.getSubordinates(10L);

        assertEquals(2, subordinates.size());
    }
}