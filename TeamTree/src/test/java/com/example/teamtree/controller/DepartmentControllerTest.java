package com.example.teamtree.controller;

import com.example.teamtree.model.Department;
import com.example.teamtree.service.DepartmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DepartmentController.class)
public class DepartmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DepartmentService departmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllDepartments() throws Exception {
        Department d1 = Department.builder().id(1L).name("IT").build();
        when(departmentService.getAll()).thenReturn(Arrays.asList(d1));

        mockMvc.perform(get("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("IT"));
    }

    @Test
    public void testGetDepartmentById_Found() throws Exception {
        Department d = Department.builder().id(1L).name("HR").build();
        when(departmentService.getById(1L)).thenReturn(Optional.of(d));

        mockMvc.perform(get("/api/departments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("HR"));
    }

    @Test
    public void testGetDepartmentById_NotFound() throws Exception {
        when(departmentService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/departments/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateDepartment() throws Exception {
        Department request = Department.builder().name("Sales").build();
        Department response = Department.builder().id(1L).name("Sales").build();

        // Simulăm serviciul
        when(departmentService.save(any(Department.class))).thenReturn(response);

        mockMvc.perform(post("/api/departments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Returnezi ResponseEntity.ok() în controller
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Sales"));
    }

    @Test
    public void testDeleteDepartment() throws Exception {
        doNothing().when(departmentService).deleteById(1L);

        mockMvc.perform(delete("/api/departments/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }
}