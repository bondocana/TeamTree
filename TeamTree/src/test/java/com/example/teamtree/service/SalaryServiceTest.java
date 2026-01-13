package com.example.teamtree.service;

import com.example.teamtree.model.Employee;
import com.example.teamtree.model.Salary;
import com.example.teamtree.repository.SalaryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SalaryServiceTest {

    @Mock
    private SalaryRepository salaryRepository;

    @InjectMocks
    private SalaryService salaryService;

    @Test
    void testSaveSalary() {
        Salary salary = Salary.builder()
                .amount(new BigDecimal("5000"))
                .startDate(LocalDate.now())
                .build();

        // Metoda ta din service se numește 'save'
        when(salaryRepository.save(any(Salary.class))).thenReturn(salary);

        Salary saved = salaryService.save(salary);

        assertNotNull(saved);
        assertEquals(new BigDecimal("5000"), saved.getAmount());
        verify(salaryRepository, times(1)).save(salary);
    }

    @Test
    void testGetAllSalaries() {
        Salary s1 = Salary.builder().amount(new BigDecimal("1000")).build();
        Salary s2 = Salary.builder().amount(new BigDecimal("2000")).build();
        when(salaryRepository.findAll()).thenReturn(Arrays.asList(s1, s2));

        List<Salary> list = salaryService.getAll();

        assertEquals(2, list.size());
        verify(salaryRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        Salary s = Salary.builder().id(1L).amount(new BigDecimal("3000")).build();
        when(salaryRepository.findById(1L)).thenReturn(Optional.of(s));

        Optional<Salary> found = salaryService.getById(1L);

        assertTrue(found.isPresent());
        assertEquals(new BigDecimal("3000"), found.get().getAmount());
    }

    @Test
    void testGetById_NotFound() {
        when(salaryRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Salary> found = salaryService.getById(99L);

        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById() {
        // Service-ul tău are metoda deleteById
        doNothing().when(salaryRepository).deleteById(1L);

        salaryService.deleteById(1L);

        verify(salaryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetByEmployeeId() {
        Long empId = 5L;
        Salary s1 = Salary.builder().id(1L).build();
        // Repository-ul are metoda findByEmployeeId
        when(salaryRepository.findByEmployeeId(empId)).thenReturn(Arrays.asList(s1));

        List<Salary> result = salaryService.getByEmployeeId(empId);

        assertEquals(1, result.size());
        verify(salaryRepository, times(1)).findByEmployeeId(empId);
    }

    @Test
    void testGetCurrentSalary() {
        Long empId = 5L;
        Salary activeSalary = Salary.builder().id(2L).endDate(null).build();

        // Repository-ul are metoda findByEmployeeIdAndEndDateIsNull
        when(salaryRepository.findByEmployeeIdAndEndDateIsNull(empId)).thenReturn(Optional.of(activeSalary));

        Optional<Salary> result = salaryService.getCurrentSalary(empId);

        assertTrue(result.isPresent());
        assertNull(result.get().getEndDate());
    }
}