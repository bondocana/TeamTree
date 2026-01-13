package com.example.teamtree.controller;

import com.example.teamtree.model.Position;
import com.example.teamtree.service.PositionService;
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

@WebMvcTest(PositionController.class)
public class PositionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PositionService positionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllPositions() throws Exception {
        Position p1 = Position.builder().id(1L).title("HR Manager").build();
        when(positionService.getAll()).thenReturn(Arrays.asList(p1));

        mockMvc.perform(get("/api/positions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("HR Manager"));
    }

    @Test
    public void testGetPositionById_Found() throws Exception {
        Position p = Position.builder().id(1L).title("CTO").build();
        when(positionService.getById(1L)).thenReturn(Optional.of(p));

        mockMvc.perform(get("/api/positions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("CTO"));
    }

    @Test
    public void testGetPositionById_NotFound() throws Exception {
        when(positionService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/positions/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreatePosition() throws Exception {
        Position request = Position.builder().title("Team Lead").build();
        Position response = Position.builder().id(1L).title("Team Lead").build();

        // Simulăm serviciul
        when(positionService.save(any(Position.class))).thenReturn(response);

        mockMvc.perform(post("/api/positions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Controllerul returnează 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Team Lead"));
    }

    @Test
    public void testDeletePosition() throws Exception {
        doNothing().when(positionService).deleteById(1L);

        mockMvc.perform(delete("/api/positions/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }
}