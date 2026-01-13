package com.example.teamtree.controller;

import com.example.teamtree.model.Contract;
import com.example.teamtree.service.ContractService;
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

@WebMvcTest(ContractController.class)
public class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ContractService contractService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testGetAllContracts() throws Exception {
        Contract c1 = Contract.builder().id(1L).type("Full-Time").build();
        when(contractService.getAll()).thenReturn(Arrays.asList(c1));

        mockMvc.perform(get("/api/contracts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].type").value("Full-Time"));
    }

    @Test
    public void testGetContractById_Found() throws Exception {
        Contract c = Contract.builder().id(1L).type("Part-Time").build();
        when(contractService.getById(1L)).thenReturn(Optional.of(c));

        mockMvc.perform(get("/api/contracts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("Part-Time"));
    }

    @Test
    public void testGetContractById_NotFound() throws Exception {
        when(contractService.getById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/contracts/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateContract() throws Exception {
        Contract request = Contract.builder().type("B2B").build();
        Contract response = Contract.builder().id(1L).type("B2B").build();

        // Simulăm serviciul
        when(contractService.save(any(Contract.class))).thenReturn(response);

        mockMvc.perform(post("/api/contracts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk()) // Controllerul returnează 200 OK
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.type").value("B2B"));
    }

    @Test
    public void testDeleteContract() throws Exception {
        doNothing().when(contractService).deleteById(1L);

        mockMvc.perform(delete("/api/contracts/1"))
                .andExpect(status().isNoContent()); // 204 No Content
    }
}