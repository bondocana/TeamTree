package com.example.teamtree.controller;

import com.example.teamtree.model.Employee;
import com.example.teamtree.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = Employee.builder().firstName("John").lastName("Doe").email("john@example.com").build();
        Employee savedEmployee = Employee.builder().id(1L).firstName("John").lastName("Doe").email("john@example.com").build();

        // Service-ul tău are metoda 'create', nu createEmployee
        when(employeeService.create(any(Employee.class))).thenReturn(savedEmployee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isCreated()) // Controllerul tău returnează HttpStatus.CREATED (201)
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    public void testGetAllEmployees() throws Exception {
        Employee e1 = Employee.builder().firstName("A").build();
        when(employeeService.getAll()).thenReturn(Arrays.asList(e1));

        mockMvc.perform(get("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    public void testGetEmployeeById_Found() throws Exception {
        Employee e = Employee.builder().id(1L).firstName("Test").build();
        // Service-ul returnează Optional<Employee>
        when(employeeService.getById(1L)).thenReturn(Optional.of(e));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Test"));
    }

    @Test
    public void testGetEmployeeById_NotFound() throws Exception {
        when(employeeService.getById(99L)).thenReturn(Optional.empty());

        // Controllerul tău are .orElse(ResponseEntity.notFound().build())
        mockMvc.perform(get("/api/employees/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee updateInfo = Employee.builder().firstName("UpdatedName").build();
        Employee updated = Employee.builder().id(1L).firstName("UpdatedName").build();

        // Metoda din controller setează ID-ul și apoi cheamă service.update()
        when(employeeService.update(any(Employee.class))).thenReturn(updated);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("UpdatedName"));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).delete(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent()); // Controllerul returnează 204 No Content
    }

    @Test
    public void testAssignManager() throws Exception {
        Employee subordonatCuManager = Employee.builder().id(1L).build();

        when(employeeService.assignManager(1L, 2L)).thenReturn(subordonatCuManager);

        mockMvc.perform(put("/api/employees/1/manager/2"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetSubordinates() throws Exception {
        when(employeeService.getSubordinates(1L)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/employees/1/subordinates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(0));
    }
}