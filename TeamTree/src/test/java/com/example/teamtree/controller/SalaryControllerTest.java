package com.example.teamtree.controller;

import com.example.teamtree.model.Salary;
import com.example.teamtree.service.SalaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SalaryController.class)
public class SalaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SalaryService salaryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllSalaries() throws Exception {
        Salary s1 = Salary.builder().amount(new BigDecimal("4000")).build();
        when(salaryService.getAll()).thenReturn(Arrays.asList(s1));

        mockMvc.perform(get("/api/salaries")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].amount").value(4000));
    }

    @Test
    public void testGetSalaryById_Found() throws Exception {
        Salary s = Salary.builder().id(1L).amount(new BigDecimal("5000")).build();
        when(salaryService.getById(1L)).thenReturn(Optional.of(s));

        mockMvc.perform(get("/api/salaries/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(5000));
    }

    @Test
    public void testGetSalaryById_NotFound() throws Exception {
        when(salaryService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/salaries/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateSalary() throws Exception {
        // Obiectul trimis (fără ID)
        Salary salaryRequest = Salary.builder()
                .amount(new BigDecimal("6000"))
                .startDate(LocalDate.now())
                .build();

        // Obiectul returnat (cu ID)
        Salary savedSalary = Salary.builder()
                .id(10L)
                .amount(new BigDecimal("6000"))
                .startDate(LocalDate.now())
                .build();

        // Service-ul tău folosește 'save'
        when(salaryService.save(any(Salary.class))).thenReturn(savedSalary);

        mockMvc.perform(post("/api/salaries")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(salaryRequest)))
                .andExpect(status().isOk()) // Controllerul tău returnează 200 OK, nu 201 Created
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.amount").value(6000));
    }

    @Test
    public void testDeleteSalary() throws Exception {
        doNothing().when(salaryService).deleteById(1L);

        mockMvc.perform(delete("/api/salaries/1"))
                .andExpect(status().isNoContent()); // 204
    }

    @Test
    public void testGetSalariesByEmployee() throws Exception {
        Long empId = 5L;
        Salary s1 = Salary.builder().id(1L).build();

        // Metoda mapată în controller este: /api/salaries/employee/{employeeId}
        when(salaryService.getByEmployeeId(empId)).thenReturn(Arrays.asList(s1));

        mockMvc.perform(get("/api/salaries/employee/" + empId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }
}