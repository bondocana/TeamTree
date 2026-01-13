package com.example.teamtree.service;

import com.example.teamtree.model.Contract;
import com.example.teamtree.repository.ContractRepository;
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
public class ContractServiceTest {

    @Mock
    private ContractRepository contractRepository;

    @InjectMocks
    private ContractService contractService;

    @Test
    void testSaveContract() {
        Contract contract = Contract.builder().type("Full-Time").build();
        Contract savedContract = Contract.builder().id(1L).type("Full-Time").build();

        // Metoda din service este 'save'
        when(contractRepository.save(any(Contract.class))).thenReturn(savedContract);

        Contract result = contractService.save(contract);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Full-Time", result.getType());
        verify(contractRepository, times(1)).save(contract);
    }

    @Test
    void testGetAllContracts() {
        Contract c1 = Contract.builder().type("Part-Time").build();
        Contract c2 = Contract.builder().type("Internship").build();

        when(contractRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Contract> list = contractService.getAll();

        assertEquals(2, list.size());
        verify(contractRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        Contract c = Contract.builder().id(1L).type("B2B").build();
        when(contractRepository.findById(1L)).thenReturn(Optional.of(c));

        Optional<Contract> found = contractService.getById(1L);

        assertTrue(found.isPresent());
        assertEquals("B2B", found.get().getType());
    }

    @Test
    void testGetById_NotFound() {
        when(contractRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Contract> found = contractService.getById(99L);

        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById() {
        doNothing().when(contractRepository).deleteById(1L);

        contractService.deleteById(1L);

        verify(contractRepository, times(1)).deleteById(1L);
    }
}