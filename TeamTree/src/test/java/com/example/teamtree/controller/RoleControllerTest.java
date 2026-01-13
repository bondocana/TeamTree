package com.example.teamtree.controller;

import com.example.teamtree.model.Role;
import com.example.teamtree.service.RoleService;
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

@WebMvcTest(RoleController.class)
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllRoles() throws Exception {
        Role r1 = Role.builder().id(1L).name("ADMIN").build();
        when(roleService.getAll()).thenReturn(Arrays.asList(r1));

        mockMvc.perform(get("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("ADMIN"));
    }

    @Test
    public void testGetRoleById_Found() throws Exception {
        Role r = Role.builder().id(1L).name("USER").build();
        when(roleService.getById(1L)).thenReturn(Optional.of(r));

        mockMvc.perform(get("/api/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("USER"));
    }

    @Test
    public void testGetRoleById_NotFound() throws Exception {
        when(roleService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/roles/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateRole() throws Exception {
        Role request = Role.builder().name("GUEST").build();
        Role response = Role.builder().id(1L).name("GUEST").build();

        // Simulăm serviciul
        when(roleService.save(any(Role.class))).thenReturn(response);

        mockMvc.perform(post("/api/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("GUEST"));
    }

    @Test
    public void testDeleteRole() throws Exception {
        doNothing().when(roleService).deleteById(1L);

        mockMvc.perform(delete("/api/roles/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }
}