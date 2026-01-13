package com.example.teamtree.service;

import com.example.teamtree.model.Role;
import com.example.teamtree.repository.RoleRepository;
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
public class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @Test
    void testSaveRole() {
        Role role = Role.builder().name("ADMIN").build();
        Role savedRole = Role.builder().id(1L).name("ADMIN").build();

        // Metoda ta din service este 'save'
        when(roleRepository.save(any(Role.class))).thenReturn(savedRole);

        Role result = roleService.save(role);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("ADMIN", result.getName());
        verify(roleRepository, times(1)).save(role);
    }

    @Test
    void testGetAllRoles() {
        Role r1 = Role.builder().name("USER").build();
        Role r2 = Role.builder().name("MANAGER").build();

        when(roleRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<Role> list = roleService.getAll();

        assertEquals(2, list.size());
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testGetById_Found() {
        Role r = Role.builder().id(1L).name("HR").build();
        when(roleRepository.findById(1L)).thenReturn(Optional.of(r));

        Optional<Role> found = roleService.getById(1L);

        assertTrue(found.isPresent());
        assertEquals("HR", found.get().getName());
    }

    @Test
    void testGetById_NotFound() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<Role> found = roleService.getById(99L);

        assertFalse(found.isPresent());
    }

    @Test
    void testDeleteById() {
        doNothing().when(roleRepository).deleteById(1L);

        roleService.deleteById(1L);

        verify(roleRepository, times(1)).deleteById(1L);
    }
}