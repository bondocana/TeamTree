package com.example.teamtree.service;

import com.example.teamtree.model.Role;
import com.example.teamtree.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Creează sau actualizează rol
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    // Obține toate rolurile
    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    // Obține rol după ID
    public Optional<Role> getById(Long id) {
        return roleRepository.findById(id);
    }

    // Șterge rol după ID
    public void deleteById(Long id) {
        roleRepository.deleteById(id);
    }
}
