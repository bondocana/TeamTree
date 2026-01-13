package com.example.teamtree.controller;

import com.example.teamtree.model.Contract;
import com.example.teamtree.service.ContractService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/contracts")
public class ContractViewController {

    private final ContractService contractService;

    public ContractViewController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public String listContracts(Model model) {
        model.addAttribute("contracts", contractService.getAll());
        return "contracts"; // Thymeleaf template contracts.html
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("contract", new Contract());
        return "contract_form"; // Thymeleaf template contract_form.html
    }

    @PostMapping("/save")
    public String saveContract(@ModelAttribute Contract contract) {
        contractService.save(contract);
        return "redirect:/contracts";
    }
}
