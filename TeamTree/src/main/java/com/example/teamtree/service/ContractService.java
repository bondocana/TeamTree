package com.example.teamtree.service;

import com.example.teamtree.model.Contract;
import com.example.teamtree.repository.ContractRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContractService {

    private final ContractRepository contractRepository;

    public ContractService(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    // Creează sau actualizează contract
    public Contract save(Contract contract) {
        return contractRepository.save(contract);
    }

    // Obține toate contractele
    public List<Contract> getAll() {
        return contractRepository.findAll();
    }

    // Obține contract după ID
    public Optional<Contract> getById(Long id) {
        return contractRepository.findById(id);
    }

    // Șterge contract după ID
    public void deleteById(Long id) {
        contractRepository.deleteById(id);
    }


}
